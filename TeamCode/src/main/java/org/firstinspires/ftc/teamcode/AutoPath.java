package org.firstinspires.ftc.teamcode;

public class AutoPath {
    Task task;
    Start start;
    Team team;
    SamplePosition samplePosition;
    Hardware hardware;
    DriveTrain driveTrain;
    Hanger hanger;
    MinionArm minionArm;
    Compass compass;
    boolean isTurningDone = false;

    public AutoPath(Hardware hardware, DriveTrain driveTrain, Hanger hanger, Compass compass, MinionArm minionArm){
        this.hardware = hardware;
        this.driveTrain = driveTrain;
        this.hanger = hanger;
        this.compass = compass;
        this.minionArm = minionArm;
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
                if (hanger.drop()) task = Task.DRIVE;
                break;
            case DRIVE:
                if (driveTrain.driveDistance(compass, 7.5, 90)) task = Task.FINDGOLD;
                break;
            case FINDGOLD:
                samplePosition = SamplePosition.MID;
                switch (samplePosition){
                    case MID:
                        if (driveTrain.driveDistance(compass, 17, 90)) task = Task.CLAIM;
                        break;
                    case LEFT:
                        if (driveTrain.driveDistance(compass, 19, 135)) task = Task.CLAIM;
                        break;
                    case RIGHT:
                        if (driveTrain.driveDistance(compass, 19, 45)) task = Task.CLAIM;
                        break;
                    default:
                }
                break;
            case CLAIM:
                switch (start) {
                    case GOLD:
                        switch (samplePosition){
                            case MID:
                                if (driveTrain.driveDistance(compass, 34, 90))
                                break;
                            case LEFT:
                                if (driveTrain.driveDistance(compass, 37, 66.77))
                                break;
                            case RIGHT:
                                if (driveTrain.driveDistance(compass, 37, 113.23))
                                break;
                            default:
                        }
                        if (minionArm.release()) task = Task.PARK;
                        break;
                    case SILVER:
                        if (minionArm.release()) task = Task.PARK;
                        break;
                    default:
                }
                break;
            case PARK:
                switch (start) {
                    case GOLD:
                        if (driveTrain.driveDistance(compass, 8*12, -45)) task = Task.DONE;
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
