// Based on code from - Spectrum 3847
package frc.lib.gamepads;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.lib.gamepads.AxisButton.ThresholdType;
import frc.lib.gamepads.XboxGamepad.XboxAxis;
import frc.lib.gamepads.XboxGamepad.XboxDpad;

public class Dpad {
    public final Joystick joy;
    // public AxisButton Up;
    // public AxisButton Down;
    // public AxisButton Left;
    // public AxisButton Right;
    // public AxisButton UpLeft;
    // public AxisButton UpRight;
    // public AxisButton DownLeft;
    // public AxisButton DownRight;

    public Trigger Left;
    public Trigger Right;
    public Trigger Up;
    public Trigger Down;
    public Trigger DownLeft;
    public Trigger DownRight;
    public Trigger UpLeft;
    public Trigger UpRight;

    public Dpad(Joystick joystick) {
        this.joy = joystick;

        this.Up =           new Trigger(() ->(joy.getPOV() == 0));
        this.Right =        new Trigger(() ->(joy.getPOV() == 45));
        this.Right =        new Trigger(() ->(joy.getPOV() == 90));
        this.DownRight =    new Trigger(() ->(joy.getPOV() == 135));
        this.Down =         new Trigger(() ->(joy.getPOV() == 180));
        this.DownLeft =     new Trigger(() ->(joy.getPOV() == 225));
        this.Left =         new Trigger(() ->(joy.getPOV() == 270));
        this.UpLeft =       new Trigger(() ->(joy.getPOV() == 315));

        // this.Up         = new AxisButton(joy, XboxAxis.DPAD, XboxDpad.UP.value,         ThresholdType.POV);
        // this.Down       = new AxisButton(joy, XboxAxis.DPAD, XboxDpad.DOWN.value,       ThresholdType.POV);
        // this.Left       = new AxisButton(joy, XboxAxis.DPAD, XboxDpad.LEFT.value,       ThresholdType.POV);
        // this.Right      = new AxisButton(joy, XboxAxis.DPAD, XboxDpad.RIGHT.value,      ThresholdType.POV);
        // this.UpLeft     = new AxisButton(joy, XboxAxis.DPAD, XboxDpad.UP_LEFT.value,    ThresholdType.POV);
        // this.UpRight    = new AxisButton(joy, XboxAxis.DPAD, XboxDpad.UP_RIGHT.value,   ThresholdType.POV);
        // this.DownLeft   = new AxisButton(joy, XboxAxis.DPAD, XboxDpad.DOWN_LEFT.value,  ThresholdType.POV);
        // this.DownRight  = new AxisButton(joy, XboxAxis.DPAD, XboxDpad.DOWN_RIGHT.value, ThresholdType.POV);
    }

    public double getValue() {
        return joy.getRawAxis(XboxAxis.DPAD.value);
    }
}
