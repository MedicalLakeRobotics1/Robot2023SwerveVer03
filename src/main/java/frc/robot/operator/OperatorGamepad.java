package frc.robot.operator;

import frc.lib.gamepads.Gamepad;
import frc.robot.arm.commands.ArmCmds;
import frc.robot.intake.commands.IntakeCmds;
import frc.robot.intake.commands.IntakeConeCmd;
import frc.robot.operator.commands.OperatorGamepadCmds;

public class OperatorGamepad extends Gamepad {
    public OperatorGamepad() {
        super("Operator", OperatorGamepadConfig.port);
    }
    
    public void setupTeleopButtons() {
        gamepad.aButton.onTrue(IntakeCmds.IntakeEjectCmd());
        gamepad.bButton.onTrue(IntakeCmds.IntakeCubeRetractCmd());
        gamepad.xButton.onTrue(IntakeCmds.IntakeStopCmd());
        gamepad.yButton.onTrue(new IntakeConeCmd());
        // gamepad.yButton.onTrue(new PrintCommand("Y Pressed"));

        gamepad.startButton.onTrue(ArmCmds.ResetArmEncoderCmd());
        gamepad.selectButton.onTrue(OperatorGamepadCmds.SetArmElevToFullBackPosCmd());

        gamepad.Dpad.Down.onTrue(OperatorGamepadCmds.SetArmElevToLowPosCmd());
        gamepad.Dpad.Right.onTrue(OperatorGamepadCmds.SetArmElevToStorePosCmd());
        gamepad.Dpad.Left.onTrue(OperatorGamepadCmds.SetArmElevToMidPosCmd());
        gamepad.Dpad.Up.onTrue(OperatorGamepadCmds.SetArmElevToHighPosCmd());

        gamepad.rightBumper.whileTrue(OperatorGamepadCmds.ControlArmElevByJoysticksCmd());
    }

    public void setupDisabledButtons() {
    }

    public void setupTestButtons() {
    }

    public double getElevInput() {
        double yValue = gamepad.rightStick.getY();
        if (Math.abs(yValue) < 0.05) {
            yValue = 0.0;
        }
        if (OperatorGamepadConfig.elevYInvert) {
            return yValue * -0.33;
        } else {
            return yValue * 0.33;
        }
    }

    public double getArmInput() {
        double yValue = gamepad.leftStick.getY();
        if (Math.abs(yValue) < 0.175) {
            yValue = 0.0;
        }
        if (OperatorGamepadConfig.armYInvert) {
            return yValue * -0.33;
        } else {
            return yValue * 0.33;
        }
    }

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }
}
