package frc.robot.elevator;

import edu.wpi.first.math.util.Units;

public class ElevatorConfig {
    // Declare and Initialize needed class objects

    public final boolean lowerLimitTrue = false;
    public final boolean upperLimitTrue = false;


    //public final double kElevatorGearing = 10.0;
    public final double kElevatorDrumRadius = Units.inchesToMeters(2.0);

    public final double kElevatorEncoderDistPerPulse = 2.0 * Math.PI * kElevatorDrumRadius / 4096;

    // ------ Orig Elevator Speed Constants -----
    public final double kMaxPwr = 1.00;

    public final static double zeroSpeed = +0.1;
    public final double lowerMaxSpeed  = -0.30;
    public final double raiseMaxSpeed  = +1.00;

    public final double KRaiseSpeedDefault = +0.40;
    public final double KRaiseSlowSpeed =    +0.25;
    public final double KHoldSpeedDefault =  +0.08;  // +0.15;
    public final double KLowerSlowSpeed =    -0.10;
    public final double KLowerSpeedDefault = -0.25;

    // ------ Position Constatnts ------
    public final double KheightDeadBand =    +0.15;

    public final static double ElevClearPos = 10.0;
    public final static double ElevStorePos = 7.0;
    public final static double ElevMidPos = 20.0;
    public final static double ElevHighPos = 30.0;

    public final double KElevMaxTopPos =             30.0;      // This is the top GO NO FURTHER!
    public final double KLimitElevTopSlowPos =       25.5;		// Start slowing the raise at this position   
    public final double KLimitElevBottomSlowPos =    5.0;       // Start slowing the lower at this position

    public final double ELEV_INCH_ABOVE_GROUND =     2.875;     // Inches claw is above ground when fully lowered

    public final double ELEV_ENCODER_CONV = 0.03461;            // Inches the elevator rises for each encoder count

    
    // All these are made up Encoder Count positions and need to be changed
    public final static double cubeIntake = 5000;
    public final static double cubeMid = 60000;
    public final static double cubeTop = 100000;

    public final static double coneIntake = 0;
    public final static double coneStandingIntake = 0;
    public final static double coneShelf = 130000;

    public final static double coneMid = 130000;
    public final static double coneTop = 150000;

    public final static double diameterInches = 2.0; // changed from int, 4
    public final static double gearRatio = 62 / 8;
    public final static double maxUpFalconPos = 162116;

    public final static double safePositionForFourBar = 0; // TODO: find safe position for four bar
    public final static double startingHeight = 0; // TODO: find starting height
    public final static double startingHorizontalExtension = 0; // TODO: find starting horizontal extension
    public final static double maxExtension = 80000; // TODO: find max relative extension
    public final static double angle = 60;

}
