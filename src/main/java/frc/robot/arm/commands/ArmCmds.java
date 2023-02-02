package frc.robot.arm.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;

//the funny code

public class ArmCmds {
    
    // Default Command
    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
        Robot.arm.setDefaultCommand(holdArmCmd());
    }

    public static Command holdArmCmd() {
        return new RunCommand(() -> Robot.arm.holdArm(), Robot.arm )
            .withName("HoldArmCmd");
    }

    public static Command stopArmCmd() {
        return new InstantCommand( () -> Robot.arm.stopArm(), Robot.arm)
            .withName("StopArmCmd");
    }

    public static Command raiseArmCmd() {
        return new RunCommand(() -> Robot.arm.raiseArm(), Robot.arm )
            .withName("RaiseArmCmd");
    }

    public static Command lowerArmCmd() {
        return new RunCommand(() -> Robot.arm.lowerArm(), Robot.arm )
            .withName("LowerArmCmd");
    }

    public static Command armGoToBottomCmd() {
        return new RunCommand( () -> Robot.arm.lowerArm(), Robot.elevator)
            .until(() ->Robot.arm.isLowerLimitSwitchPressed())
            .withName("armToBottomCmd");
    }

    public static Command armToPIDPositionCmd(double pos) {
        return new RunCommand(() -> Robot.arm.lowerArm(), Robot.arm )
            .withName("ArmToPIDPosistionCmd");
    }
    
}
