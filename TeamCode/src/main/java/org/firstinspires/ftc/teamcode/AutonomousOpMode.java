package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "Auto", group = "Auto")
public class AutonomousOpMode extends OpMode {
    //Objects
    Hardware hardware = new Hardware();
    ToggleButton buttonA = new ToggleButton(), buttonB = new ToggleButton(), omega = new ToggleButton();
    DriveTrain driveTrain;
    Hanger hanger;
    AutoPath auto;
    AutoPath.Start start;
    AutoPath.Team team;
    MarkerArm markerArm;

    //Vision vision;

    @Override
    public void init() {
        hardware.init(hardwareMap);
        driveTrain = new DriveTrain(hardware.leftMotor, hardware.rightMotor, hardware.compass);
        hanger = new Hanger(hardware.hookServo, hardware.extensionMotor, hardware.extenderStop, hardware.extenderLowerLim);
        markerArm = new MarkerArm(hardware.markerServo);
        auto = new AutoPath(hardware, driveTrain, hanger, hardware.compass, markerArm);
        auto.init();
    }

    @Override
    public void init_loop(){
        if (start == AutoPath.Start.GOLD) {
            if (buttonA.wasJustClicked(gamepad1.a)) {
                start = AutoPath.Start.SILVER;
                auto.setStart(start);
            }
        } else if (start == AutoPath.Start.SILVER) {
            if (buttonA.wasJustClicked(gamepad1.a)) {
                start = AutoPath.Start.GOLD;
                auto.setStart(start);
            }
        }

        if (team == AutoPath.Team.RED) {
            if (buttonB.wasJustClicked(gamepad1.b)) {
                team = AutoPath.Team.BLUE;
                auto.setTeamColor(team);
            }
        } else if (team == AutoPath.Team.BLUE) {
            if (buttonB.wasJustClicked(gamepad1.b)) {
                team = AutoPath.Team.RED;
                auto.setTeamColor(team);
            }
        }
        hardware.compass.resetHeading();
        telemetry.addData("Team", team);
        telemetry.addData("Start", start);
        telemetry.addData("Raw Compass", hardware.compass.getRawHeading());
        telemetry.addData("heading", hardware.compass.getHeading());
        telemetry.addData("OriginAngle", hardware.compass.getOriginalRawHeading());
        telemetry.update();
    }

    double desiredAngle = 0;
    @Override
    public void loop() {
        //auto.execute();
        //driveTrain.maintainHeading(0);

        if (omega.wasJustClicked(gamepad1.a)){
            desiredAngle += 20;
        } else if (omega.wasJustClicked(gamepad1.b)) {
            desiredAngle -= 20;
        }
        driveTrain.maintainHeading(desiredAngle);

        driveTrain.rotateToAngle(desiredAngle);
        telemetry.addData("Team", team);
        telemetry.addData("Start", start);
        telemetry.addData("Raw Compass", hardware.compass.getRawHeading());
        telemetry.addData("heading", hardware.compass.getHeading());
        telemetry.addData("OriginAngle", hardware.compass.getOriginalRawHeading());
        telemetry.addData("DesiredAngle", desiredAngle);
        telemetry.addData("newSpeed", driveTrain.left.getPower());
        telemetry.update();
        /*
        if (vision == null) {
            vision = new Vision();
        }
        */

    }
}