package frc.robot.arm;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Robot;

//the funny code

public class ArmTelemeter {
    protected ShuffleboardTab tab;

    public ArmTelemeter() {
        tab = Shuffleboard.getTab("Arm");
        tab.addString("Lower Limit Switch",()-> Robot.arm.lowerLimitSwitchStatus());
        tab.addString("Upper Limit Switch",()-> Robot.arm.UpperLimitSwitchStatus());
    }
}
