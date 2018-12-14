package org.firstinspires.ftc.teamcode;
/*
    this class is an Autonomous opmode. We select this code on the driver phone before the match.
    After that we start the code.
 */
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

// This assigns the class to the autonomous list of opmodes on the driver phone
// It also gives a name to appear on the list, and a group.
// You cannot have identical names or the code will give an error.
@Autonomous(name = "Auto", group = "Auto")
public class AutonomousOpMode extends OpMode { // classifies that this class is an opmode.
    //Objects used on the robot
    Hardware hardware = new Hardware();
    // these are the buttons to control selecting team and start
    ToggleButton buttonA = new ToggleButton(), buttonB = new ToggleButton();
    DriveTrain driveTrain;
    Hanger hanger;
    AutoPath auto;
    AutoPath.Start start;
    AutoPath.Team team;
    MarkerArm markerArm;

    /**
     * This code is called once when the init button is pressed on the driver phone.
     */
    @Override
    public void init() {
        hardware.init(hardwareMap);
        driveTrain = new DriveTrain(hardware.leftMotor, hardware.rightMotor, hardware.compass);
        hanger = new Hanger(hardware.hookServo, hardware.extensionMotor, hardware.extenderStop, hardware.extenderLowerLim);
        markerArm = new MarkerArm(hardware.markerServo);
        auto = new AutoPath(hardware, driveTrain, hanger, hardware.compass, markerArm);
        auto.init();
    }

    /**
     * This code is called every 20 ms before start but after init.
     * We use this to set our team and start position.
     */
    @Override
    public void init_loop() {
        if (start == AutoPath.Start.GOLD) {
            if (buttonA.wasJustClicked(gamepad1.a)) {
                start = AutoPath.Start.SILVER;
            }
        } else if (start == AutoPath.Start.SILVER) {
            if (buttonA.wasJustClicked(gamepad1.a)) {
                start = AutoPath.Start.GOLD;
            }
        }

        if (team == AutoPath.Team.RED) {
            if (buttonB.wasJustClicked(gamepad1.b)) {
                team = AutoPath.Team.BLUE;
            }
        } else if (team == AutoPath.Team.BLUE) {
            if (buttonB.wasJustClicked(gamepad1.b)) {
                team = AutoPath.Team.RED;
            }
        }
        hardware.compass.resetHeading(); // sets the gyro to zero
        //these are returned to the driver phone to tell us what the current values they are
        telemetry.addData("Team", team);
        telemetry.addData("Start", start);
        telemetry.addData("Raw Compass", hardware.compass.getRawHeading());
        telemetry.addData("heading", hardware.compass.getHeading());
        telemetry.addData("OriginAngle", hardware.compass.getOriginalRawHeading());
        telemetry.update();
    }

    /**
     * This code is called once when start is pressed.
     * Sets the team and position to the last values.
     */
    @Override
    public void start(){
        auto.setStart(start);
        auto.setTeamColor(team);
    }

    double desiredAngle = 0; // this value is for when we want stay going in a direction

    /**
     * This code is called every 20 ms after start.
     * Our main program is ran here
     */
    @Override
    public void loop() {
        auto.execute();// this is the main programs run function
        //these are the values we want to see on the driver phone
        telemetry.addData("Team", team);
        telemetry.addData("Start", start);
        telemetry.addData("Raw Compass", hardware.compass.getRawHeading());
        telemetry.addData("heading", hardware.compass.getHeading());
        telemetry.addData("OriginAngle", hardware.compass.getOriginalRawHeading());
        telemetry.addData("DesiredAngle", desiredAngle);
        telemetry.addData("newSpeed", driveTrain.left.getPower());
        telemetry.update();
    }
}