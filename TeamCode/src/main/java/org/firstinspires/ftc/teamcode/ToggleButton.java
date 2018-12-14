package org.firstinspires.ftc.teamcode;

/**
 * Created by robot12 on 11/10/2016.
 * This class was created as a wrapper to a boolean value, mainly a button on the gamepad.
 * We found that in the main loop of the opmode, when we use the button for executing a command,
 * it was being every 20 ms. The wrapper allows the boolean to be registered for one iteration
 * This helped to fine tune positions and other variables using a button.
 */
// activates when you press a button
public class ToggleButton {
    private boolean previous; // this is the temporary value to tell the loop the previous state

    //constructs the class initialized as false
    ToggleButton(){
        previous = false;
    }

    /**
     *
     * @param state what button or boolean are you checking.
     * @return Outputs The value of the last state change.
     */
    public boolean wasJustClicked(boolean state){
        if (state && !previous){ // when the value is true and the previous was false
            previous = true; // Changes to true so another call returns true
            return true;
        } else if (!state){ // what to return when the robot is not pressed
            previous = false;
        }
        return false; // returns false when the previous is true
    }
}