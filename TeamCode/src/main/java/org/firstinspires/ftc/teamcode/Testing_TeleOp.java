package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.sun.source.tree.IfTree;

@TeleOp(name = "Testing_TeleOp", group = "Auto")
public class Testing_TeleOp extends OpMode {

    //Objects
    Hardware hardware = new Hardware();
    DriveTrain driveTrain;
    Hanger extender;
    MarkerArm markerArm;
    ToggleButton up = new ToggleButton(), down = new ToggleButton();

    public String FormatStackTrace(Exception e) {
        String msg = "Exception: " + e.toString();
        for(StackTraceElement ste : e.getStackTrace()) {
            msg = msg + " -> " + ste.toString();
        }
        return msg;
    }

    @Override
    public void init() {
        try {
            hardware.init(hardwareMap);
            driveTrain = new DriveTrain(hardware.leftMotor, hardware.rightMotor, hardware.compass);
            extender = new Hanger(hardware.hookServo, hardware.extensionMotor, hardware.extenderStop, hardware.extenderLowerLim);
            markerArm = new MarkerArm(hardware.markerServo);
            markerArm.setPosition(0);
            extender.task = Hanger.Task.Float;
        }catch(Exception e) {
            telemetry.addData("Say", FormatStackTrace(e));
            telemetry.update();
            throw e;
        }
    }

    @Override
    public void start(){

    }

    @Override
    public void loop() {
        try {
            if (gamepad1.y){
                extender.extendHook();
            } else if (gamepad1.a) {

            } else {
                extender.stopHook();
            }

            telemetry.addData("stop state", extender.getState()[0]);
            telemetry.addData("lower state", extender.getState()[1]);
            telemetry.addData("POS", extender.getPosition());
            telemetry.addData("pos", extender.getEncoder());
            telemetry.addData("mode", extender.getMode());
            telemetry.update();
        }catch(Exception e) {
            telemetry.addData("Say", FormatStackTrace(e));
            telemetry.update();
            Log.getStackTraceString(e);
            Log.e("null Pointer", FormatStackTrace(e), e);
            throw e;
        }
    }
}