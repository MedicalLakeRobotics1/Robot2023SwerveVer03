package frc.robot.auto;

import java.util.HashMap;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.FollowPathWithEvents;

import edu.wpi.first.networktables.PubSubOption;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;
import frc.robot.auto.commands.AutoCmds;
import frc.robot.auto.commands.DelayCmd;
import frc.robot.trajectories.commands.TrajectoriesCmds;

public class AutoSetup {
    public static final SendableChooser<String> autoChooser = new SendableChooser<>();
    public static final SendableChooser<String> positionChooser = new SendableChooser<>();
    public static final SendableChooser<String> heightChooser = new SendableChooser<>();
    public static final SendableChooser<String> objectChooser = new SendableChooser<>();
    private static boolean autoMessagePrinted = true;
    private static double autoStart = 0;
    public static HashMap<String, Command> eventMap = new HashMap<>();
    public static String autoSelect;
    public static String positionSelect;
    public static String objectSelect;
    public static String heightSelect;
    public static double armPosition;
    public static double elevStartPos;
    public static double elevEndPos;

    
    static double maxVel = 1.5;
    static double maxAccel = 1.5;
    static PathPlannerTrajectory crossShortPath = PathPlanner.loadPath("CrossShort", maxVel, maxAccel);
    
    public AutoSetup(){
        setupSelectors();
        setupEventMap();
    }

    // A chooser for autonomous commands
    public static void setupSelectors() {

        // Selector for Routine
        autoChooser.setDefaultOption("Nothing", "Nothing");
        autoChooser.addOption("Place", "Place");
        autoChooser.addOption("Place and Cross", "Place and Cross");
        autoChooser.addOption("Place and Charge", "Place and Charge");
        autoChooser.addOption("Place and Cross and Charge", "Place and Cross and Charge");
        autoChooser.addOption("Place and Cross and Pickup", "Place and Cross and Pickup");
        autoChooser.addOption("Place and Cross and Pickup and Charge", "Place and Cross and Pickup and Charge");

        // Selector for Position
        positionChooser.setDefaultOption("Left", "Left");
        positionChooser.addOption("Left", "Left");
        positionChooser.addOption("Center Left", "Center Left");
        positionChooser.addOption("Center Right", "Center Right");
        positionChooser.addOption("Right", "Right");

        // Selector for Game Piece
        objectChooser.setDefaultOption("Cube", "Cube");
        objectChooser.addOption("Cube", "Cube");
        objectChooser.addOption("Cone", "Cone");

        // Selector for Height
        heightChooser.setDefaultOption("Low", "Low");
        heightChooser.addOption("Low", "Low");
        heightChooser.addOption("Mid", "Mid");
        heightChooser.addOption("High", "High");

        // Selector for starting Location on Field
        
    }

    // Adds event mapping to autonomous commands
    public static void setupEventMap(){
        eventMap.put("Marker1", new PrintCommand("Passed marker 1"));
        eventMap.put("Marker2", new PrintCommand("Passed marker 2"));
    }


    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public static void getAutoSelections() {
        // return new CharacterizeLauncher(Robot.launcher);
        autoSelect = autoChooser.getSelected();
        positionSelect = positionChooser.getSelected();
        objectSelect = objectChooser.getSelected();
        heightSelect = heightChooser.getSelected();
    }

    public static Command getAutonomousCommand() {
        if (autoSelect == "Nothing") {
            return new PrintCommand("Do Nothing");
        }
        if (autoSelect == "Place") {
            return AutoCmds.placeObject();
        }
        if (autoSelect == "Place And Cross") {
            // --- cross distances
            // if red:
                // if left: long
                // if center: charge
                // if right: short
            // if blue:
                // if left: short
                // if center: charge
                // if right: long
            if (((Robot.alliance == Alliance.Red) && (positionSelect == "Right")) ||
                ((Robot.alliance == Alliance.Blue) && (positionSelect == "Left")) ) {
                return new SequentialCommandGroup(
                    TrajectoriesCmds.IntializePathFollowingCmd(crossShortPath),
                    AutoCmds.placeObject(),
                    TrajectoriesCmds.FollowPathCmd(crossShortPath, 5.0)
                );
            } else if ((positionSelect == "Center Left") || (positionSelect == "Center Right")) {
                return new SequentialCommandGroup(
                    TrajectoriesCmds.IntializePathFollowingCmd(crossShortPath),
                    AutoCmds.placeObject(),
                    TrajectoriesCmds.FollowPathCmd(crossShortPath, 5.0)
                );
            } else if (((Robot.alliance == Alliance.Red) && (positionSelect == "Left")) ||
                       ((Robot.alliance == Alliance.Blue) && (positionSelect == "Right")) ) {
            return new SequentialCommandGroup(
                TrajectoriesCmds.IntializePathFollowingCmd(crossShortPath),
                AutoCmds.placeObject(),
                TrajectoriesCmds.FollowPathCmd(crossShortPath, 5.0)
            );
        } 
            return new SequentialCommandGroup(
                AutoCmds.placeObject()
                // follow path to cross line
            );
        }

        return new PrintCommand("Do Nothing");
    }

    public static void setPlacePositions() {
        if ( (objectSelect == "Cone") && (positionSelect == "Low") ) {
            elevStartPos = AutoConfig.coneLowElevStartPos;
            elevEndPos = AutoConfig.coneLowElevEndPos;
            armPosition = AutoConfig.coneLowArmPos;
            return;
        }
    }
    
    /** This method is called in AutonInit */
    public static void startAutonTimer() {
        autoStart = Timer.getFPGATimestamp();
        autoMessagePrinted = false;
    }

    /* ---------  Load Auto Path Group ----------------------
    This example will load a path file as a path group. The path group will be separated into paths
    based on which waypoints are marked stop points.


    ArrayList<PathPlannerTrajectory> pathGroup1 = PathPlanner.loadPathGroup(
                                                        "Example Path Group",
                                                        new PathConstraints(4, 3));

    // This will load the file "Example Path Group.path" and generate it with different path constraints for each segment
    ArrayList<PathPlannerTrajectory> pathGroup2 = PathPlanner.loadPathGroup(
        "Example Path Group", 
        new PathConstraints(4, 3), 
        new PathConstraints(2, 2), 
        new PathConstraints(3, 3));
    // -------------------------------------------------------
*/



    /** Called in RobotPeriodic and displays the duration of the auton command Based on 6328 code */
    // public static void printAutoDuration() {
        // Command autoCommand = AutoSetup.getAutonomousCommand();
        // if (autoCommand != null) {
        //     if (!autoCommand.isScheduled() && !autoMessagePrinted) {
        //         if (DriverStation.isAutonomousEnabled()) {
        //             RobotTelemetry.print(
        //                     String.format(
        //                             "*** Auton finished in %.2f secs ***",
        //                             Timer.getFPGATimestamp() - autoStart));
        //         } else {
        //             RobotTelemetry.print(
        //                     String.format(
        //                             "*** Auton CANCELLED in %.2f secs ***",
        //                             Timer.getFPGATimestamp() - autoStart));
        //         }
        //         autoMessagePrinted = true;
        //     }
        // }
    // }
}
