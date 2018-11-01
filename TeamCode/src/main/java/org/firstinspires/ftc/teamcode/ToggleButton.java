package org.firstinspires.ftc.teamcode;

/**
 * Created by robot12 on 11/10/2016.
 */
// activates when you press up button
public class ToggleButton {
    private boolean previous;

    ToggleButton(){
        previous = false;
    }

    public boolean wasJustClicked(boolean state){
        if (state && !previous){
            previous = true;
            return true;
        } else if (!state){
            previous = false;
        }
        return false;
    }
}