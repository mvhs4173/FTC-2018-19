package org.firstinspires.ftc.teamcode;

public class AutoPath {
    Task task;
    Start start;
    Team team;
    SamplePosition samplePosition;
    Hardware hardware;
    DriveTrain driveTrain;
    Hanger hanger;
    MarkerArm markerArm;
    Compass compass;
    boolean isTurningDone = false;

    public AutoPath(Hardware hardware, DriveTrain driveTrain, Hanger hanger, Compass compass, MarkerArm markerArm){
        this.hardware = hardware;
        this.driveTrain = driveTrain;
        this.hanger = hanger;
        this.compass = compass;
        this.markerArm = markerArm;
    }

    enum Task {
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

    enum SamplePosition {
        LEFT,MID,RIGHT
    }

    void setStart(Start desiredStart){
        this.start = desiredStart;
    }

    void setTeamColor(Team team){
        this.team = team;
    }

    void init(){
        task = Task.DROP;
    }

    void execute(){
        switch (task){
            case DROP:
                hanger.task = Hanger.Task.DROP;
                if (hanger.dropOrder == Hanger.Order.DONE)task = Task.DRIVE;
                break;
            case DRIVE:
                break;
            case FINDGOLD:
                samplePosition = SamplePosition.MID;
                switch (samplePosition){
                    case MID:
                        break;
                    case LEFT:
                        break;
                    case RIGHT:
                        break;
                    default:
                }
                break;
            case CLAIM:
                if (markerArm.release()) task = Task.PARK;
                switch (start) {
                    case GOLD:
                        switch (samplePosition){
                            case MID:
                                break;
                            case LEFT:
                                break;
                            case RIGHT:
                                break;
                            default:
                        }
                        if (markerArm.release()) task = Task.PARK;
                        break;
                    case SILVER:
                        switch (samplePosition){
                            case MID:
                                break;
                            case LEFT:
                                    break;
                            case RIGHT:
                                    break;
                            default:
                        }
                        if (markerArm.release()) task = Task.PARK;
                        break;
                    default:
                }
                break;
            case PARK:
                switch (start) {
                    case GOLD:
                        break;
                    case SILVER:
                        break;
                    default:
                }
                break;
            case DONE:
                break;
            default:
        }
    }
}
