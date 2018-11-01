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
        hanger.task = Hanger.Task.DROP;
    }

    void execute(){
        switch (task){
            case DROP:
                hanger.execute();
                if (hanger.task == Hanger.Task.Float) task = Task.DRIVE;
                break;
            case DRIVE:
                if (driveTrain.driveDistance(5, 0)) task = Task.FINDGOLD;
                break;
            case FINDGOLD:
                samplePosition = SamplePosition.MID;
                switch (samplePosition){
                    case MID:
                        if (driveTrain.driveDistance(17, 0)) task = Task.CLAIM;
                        break;
                    case LEFT:
                        if (driveTrain.driveDistance(24, 45)) task = Task.CLAIM;
                        break;
                    case RIGHT:
                        if (driveTrain.driveDistance(24, -45)) task = Task.CLAIM;
                        break;
                    default:
                }
                break;
            case CLAIM:
                switch (start) {
                    case GOLD:
                        switch (samplePosition){
                            case MID:
                                if (driveTrain.driveDistance(34, 0))
                                break;
                            case LEFT:
                                if (driveTrain.driveDistance(38, -26.32))
                                break;
                            case RIGHT:
                                if (driveTrain.driveDistance(38, 26.32))
                                break;
                            default:
                        }
                        if (markerArm.release()) task = Task.PARK;
                        break;
                    case SILVER:
                        switch (samplePosition){
                            case MID:
                                driveTrain.driveDistance(0, -135);
                                driveTrain.driveDistance(24, -135);
                                driveTrain.driveDistance(0, -90);
                                driveTrain.driveDistance(34, -90);
                                driveTrain.driveDistance(0, -135);
                                driveTrain.driveDistance(48, -135);
                                break;
                            case LEFT:
                                driveTrain.driveDistance(0, -126.88);
                                driveTrain.driveDistance(84.85, -126.88);
                                    break;
                            case RIGHT:
                                driveTrain.driveDistance(0, -135);
                                driveTrain.driveDistance(24, -135);
                                driveTrain.driveDistance(0, -90);
                                driveTrain.driveDistance(51, -90);
                                driveTrain.driveDistance(0, -135);
                                driveTrain.driveDistance(48, -135);
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
                        driveTrain.driveDistance(0,135);
                        driveTrain.driveDistance(8*12, 135);// task = Task.DONE;
                        break;
                    case SILVER:
                        driveTrain.driveDistance(0, 45);
                        driveTrain.driveDistance(96, 45);
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
