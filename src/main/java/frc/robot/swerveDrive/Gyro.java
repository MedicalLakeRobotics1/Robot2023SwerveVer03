package frc.robot.swerveDrive;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SPI;

public class Gyro {
        private final AHRS gyro = new AHRS(SPI.Port.kMXP);
        private double gyroOffset;      // Used to set gyro to different heading

    public Gyro() {
        gyro.reset();
        gyroOffset = 0;
    }

    /**
     * Get the raw yaw of the robot in Rotation2d without using the yawOffset
     *
     * @return the raw yaw of the robot in Rotation2d
     */
    public Rotation2d getRawYaw() {
        return Rotation2d.fromDegrees(gyro.getAngle());
    } 

    public Rotation2d getGyroHeadingRotation2d() {
        // this will return a Rotation2d object of the yaw value -180 to +180 degree
        return Rotation2d.fromDegrees(getHeadingDegrees());
    }

    public void zeroHeading() {
         gyro.reset();
         gyroOffset = 0;
    }

    public double getHeadingDegrees() {
        // This will return values from -180CW to +180CCW degrees of yaw
        return -Math.IEEEremainder(gyro.getAngle()+gyroOffset, 360);
    }

    public void setHeadingDegrees( double newHdg ){
        // new Heading +180 to - 180 degrees
        gyroOffset = newHdg - getHeadingDegrees();
        // Dont think these are really needed
        //if (gyroOffset > 180 )  { gyroOffset = gyroOffset - 360.0 ;}
        //if (gyroOffset < -180 ) { gyroOffset = gyroOffset + 360.0 ; }
    }

}