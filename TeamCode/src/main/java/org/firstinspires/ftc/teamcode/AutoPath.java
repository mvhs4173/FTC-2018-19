package org.firstinspires.ftc.teamcode;

public class AutoPath {
    State state;
    Start start;
    Team team;
    Hardware hardware;
    DriveTrain driveTrain;
    Hanger hanger;
    Compass compass;
    boolean isTurningDone = false;
    Timer t = new Timer();

    public AutoPath(Hardware hardware, DriveTrain driveTrain, Hanger hanger, Compass compass){
        this.hardware = hardware;
        this.driveTrain = driveTrain;
        this.hanger = hanger;
        this.compass = compass;
    }

    enum State {
        DROP, DRIVE, FINDGOLD, CLAIM, PARK, DONE

    }

    enum Start {
        GOLD,
        SILVER
    }

    enum Team {
        BLUE,
        RED
    }

    void setStart(Start desiredStart){
        this.start = desiredStart;
    }

    void setTeamColor(Team team){
        this.team = team;
    }

    void init(){
        state = State.DROP;
    }

    void execute(){
        switch (state){
            case DROP:
                hanger.drop();
                break;
            case DRIVE:
                if (driveTrain.driveDistance(compass, 10)) state = State.FINDGOLD;
                break;
            case FINDGOLD:
                break;
            case CLAIM:
                break;
            case PARK:
                break;
            case DONE:
                break;
            default:
        }
    }
}
