package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@SuppressWarnings("unused")
public class GamepadCheck extends OpMode {

    @Override
    public void init(){

    }

    @Override
    public void loop(){
        telemetry.addData("X Button", gamepad1.x);
        telemetry.addData("Y Button", gamepad1.y);
        telemetry.addData("B Button", gamepad1.b);
        telemetry.addData("A Button", gamepad1.a);
        telemetry.addData("Right Joystick X", gamepad1.right_stick_x);
        telemetry.addData("Right Joystick Y", gamepad1.right_stick_y);
        telemetry.addData("Left Joystick X", gamepad1.left_stick_x);
        telemetry.addData("Left Joystick Y", gamepad1.left_stick_y);
        telemetry.addData("Right Joystick Button", gamepad1.right_stick_button);
        telemetry.addData("Left Joystick Button", gamepad1.left_stick_button);
        telemetry.addData("Dpad Up", gamepad1.dpad_up);
        telemetry.addData("Dpad Down",gamepad1.dpad_down);
        telemetry.addData("Dpad Left",gamepad1.dpad_left);
        telemetry.addData("Dpad Right",gamepad1.dpad_right);
        telemetry.addData("Right Trigger", gamepad1.right_trigger);
        telemetry.addData("Left Trigger",gamepad1.left_trigger);
        telemetry.addData("Right Bumper",gamepad1.right_bumper);
        telemetry.addData("Left Bumper", gamepad1.left_bumper);
        telemetry.addData("Back Button", gamepad1.back);
        telemetry.update();
    }
}
