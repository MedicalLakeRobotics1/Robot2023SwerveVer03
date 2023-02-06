package frc.robot.elevator;

import java.util.function.DoubleSupplier;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.Rmath;

public class ElevatorSubSys extends SubsystemBase {
    public ElevatorConfig config;

    // Devices
    public final WPI_TalonSRX m_motor;
    public final DigitalInput elevLowerLimitSw, elevUpperLimitSw;

    // PID Controller
    public final PIDController elevPIDcontroller;

    // Elevator Variables
    public double target_height;    // Relative to Ground
    public double target_pos;       // Relative to zero

    public double mCurrPwr = 0;
    public double mCurrElevPwr;
    public double PID_output = 0;
    public double mCurrEncoderCnt = 0;
    public double mCurrElevPos;     // Elevator Height Zero at bottom (Inches)
    public double mCurrElevHt;      // Grabber Height off the floor (Inches)

    // -----------  Constructor --------------------
    public ElevatorSubSys() {
        config = new ElevatorConfig();
        m_motor = new WPI_TalonSRX(config.kMotorPort);
        elevLowerLimitSw = new DigitalInput(config.kLowerLimitSwitchPort);
        elevUpperLimitSw = new DigitalInput(config.kUpperLimitSwitchPort);
        elevPIDcontroller = new PIDController(config.elevKP, 0, 0);
        elevatorMotorConfig();
    }

   @Override
   public void periodic() {
       updateCurrentElevPosition();
    }

    // --------------------------------------------
    // ---------   Elevator Methods   -------------
    // --------------------------------------------

    // ------------ Lower Elevator ----------
    public void elevLower() {
        elevSetSpeed( config.KLowerSpeedDefault ); 
    }

    // ------------ Raise Elevator ----------
    public void elevRaise() {
        elevSetSpeed( config.KRaiseSpeedDefault ); 
    }

    // ------------ Hold Elevator Position ----------
    public void elevHoldMtr(){
        elevSetSpeed( config.KHoldSpeedDefault ); 
    }

    // ------------ Stop Elevator Motor  ----------
    public void elevStop() {
        mCurrElevPwr = 0;        
        m_motor.stopMotor();
    }

    // ------------ This does all the work (Non PID) to Drive the Elevator ----------
    public void elevSetSpeed(double speed){
        // Cap speed to max
        if ( speed > config.raiseMaxSpeed )  { speed = config.raiseMaxSpeed; }
        if ( speed < config.lowerMaxSpeed )  { speed = config.lowerMaxSpeed; }

        // Were Raising the elevator
        if ( speed > config.KHoldSpeedDefault ) {
            // Test for hitting Upper Limits
            if ( ( isUpperLimitSwitchPressed() ) || isUpperLimitReached() ) {
                elevHoldMtr();
                return;
            }
            //  This is for slowing down as we approach the top
            if ( mCurrElevPos >= config.KLimitElevTopSlowPos )  {
                speed = config.KRaiseSlowSpeed;
            }
        }

        // Were Lowering the elevator
        if ( speed <= config.KHoldSpeedDefault) {
            // Test if hitting Bottom limit switch
            if ( isLowerLimitSwitchPressed() ) {
                elevStop();
                return;
            }
            //  This is for slowing down as we approach the bottom    		
            if ( mCurrElevPos <= config.KLimitElevBottomSlowPos ) {
                speed = config.KLowerSlowSpeed;
            }
        }

        mCurrElevPwr = speed;
        m_motor.set(mCurrElevPwr);      // Send Power to motor  
    }

    public void elevSetSpeed(DoubleSupplier speed) {
        elevSetSpeed(speed.getAsDouble());
    }

    // ------------  Set Elevator to Position by PID  ----------
    public void setPIDposition( double pos ) {
        // All motion controlled relative to bottom of the elevator (not height from floor)
        target_pos = limit_target_pos (pos);
        target_height = convertPosToHeight(target_pos);
        mCurrElevPwr = getPidCalcOut( target_pos );
        m_motor.set(mCurrElevPwr);                       // Send Power to motor  Pwr -1 to +1
        //m_motor.setVoltage(mCurrElevPwr);              // Voltage -12 to +12 ???????        
    }

    // ------------  Set Elevator to Height by PID  ----------    
    public void setPIDheight( double ht ) {
        // All motion controlled relative to Floor
        target_pos = limit_target_pos(convertHeightToPos( ht ));
        target_height = convertPosToHeight( target_pos);
        mCurrElevPwr = getPidCalcOut( target_pos );
        System.out.println("    PID to " + ht + "   CurPos=" + mCurrElevPos + " out=" + mCurrElevPwr);
        m_motor.set(mCurrElevPwr);                       // Send Power to motor  Pwr -1 to +1
        //m_motor.setVoltage(mCurrElevPwr);              // Voltage -12 to +12 ???????  
    }

    // ---------  PID Out Calculator  --------------
    public double getPidCalcOut(double tgt_setpoint) {
        double tgt = limit_target_pos (tgt_setpoint);
        double out = elevPIDcontroller.calculate(mCurrElevPos, tgt );
        out = out + config.elevKF;             // Add feedforward Term
        // Limit max pwr
        if ( out > config.kMaxPwr )  { out = config.kMaxPwr; }
        if ( out < -config.kMaxPwr ) { out = -config.kMaxPwr; }
        return out;
    }

    // Test PID Calc routine
    public double getPidCalcTestOut(double test_setpoint) {
        return getPidCalcOut(test_setpoint);
    }

    // -----------------  Encoder Sensor Methods --------------------
    public void updateCurrentElevPosition() {
        mCurrEncoderCnt = m_motor.getSelectedSensorPosition();
        mCurrElevPos = Rmath.mRound((mCurrEncoderCnt * config.ELEV_ENCODER_CONV) , 2);
        mCurrElevHt =  Rmath.mRound( convertPosToHeight(mCurrElevPos) , 2 );
    }

    public double getElevEncoderCnt()               { return mCurrEncoderCnt;}
    public double getElevPosInches()                { return mCurrElevPos; }
    public double getElevHeightInches()             { return mCurrElevHt; }

    public double convertHeightToPos( double tgt)   { return tgt - config.ELEV_INCH_ABOVE_GROUND; }
    public double convertPosToHeight( double tgt)   { return tgt + config.ELEV_INCH_ABOVE_GROUND; }

    // ------------- Other Misc Methods  ---------------
    public double getTargetHeight()         { return target_height; }
    public double getElevMotorPwr()         { return mCurrElevPwr; }

    public double limit_target_pos( double pos ){
        if (pos > config.KElevMaxTopPos)    { pos = config.KElevMaxTopPos; }
        if (pos < 0)                        { pos = 0; }
        return pos;
    }

    // -----------------  Lower/Upper Limits ----------------
    public boolean isLowerLimitSwitchPressed() {
        if (elevLowerLimitSw.get() == config.lowerLimitTrue) {
            return true;
        }
        return false;
    }

    public boolean isUpperLimitSwitchPressed() {
        if (elevUpperLimitSw.get() == config.lowerLimitTrue) {
            return true;
        }
        return false;
    }

    public boolean isUpperLimitReached() {
        if ( mCurrElevPos >= config.KElevMaxTopPos ) {
            return true;
        }
        return false;
    }

    public boolean isLowerLmtSwNotPressed() { return !isLowerLimitSwitchPressed(); }
    public boolean isUpperLmtNotReached()   { return !isUpperLimitSwitchPressed(); }

    public String getUpperLimitSwStatus(){
        if ( isUpperLimitSwitchPressed() ) {
            return "Pressed"; 
        } else {
            return "Not Pressed";
        }
    }

    public String getLowerLimitSwStatus(){
        if ( isLowerLimitSwitchPressed() ) {
            return "Pressed"; 
        } else {
            return "Not Pressed";
        }
    }

    //public double getTargetTestPos()               { return target_test_pos; }
    public double getTargetPos()                   { return target_pos; }

    // --------------------------------------------------------
    // ---------------- Configure Elev Motor ------------------
    // --------------------------------------------------------
    public void elevatorMotorConfig(){
        // This config is for the Talon SRX Controller
        m_motor.configFactoryDefault();
        m_motor.configAllSettings(ElevatorConfig.elevSRXConfig);
        m_motor.setInverted(ElevatorConfig.elevMotorInvert);
        m_motor.setNeutralMode(ElevatorConfig.elevNeutralMode);
        m_motor.setSelectedSensorPosition(0);                     // Reset Encoder to zero
    }
}
