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
    Timer sigma = new Timer();
    Timer omikron = new Timer();
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
        setStart(Start.GOLD);
        setTeamColor(Team.BLUE);
        task = Task.DROP;
        hanger.dropOrder = Hanger.Order.INIT;
    }

    void execute(){
        switch (task){
            case DROP:
                hanger.task = Hanger.Task.DROP;
                hanger.execute(false);
                if (hanger.dropOrder == Hanger.Order.DONE){
                    sigma.init(6.5/3.0);
                    task = Task.DRIVE;
                }
                break;
            case DRIVE:
                if (!sigma.isTimerUp()) {
                    driveTrain.left.setPower(-1);
                    driveTrain.right.setPower(-1);
                } else if (sigma.isTimerUp()){
                    driveTrain.stopMotors();
                    task = Task.FINDGOLD;
                }
                break;
            case FINDGOLD:
                samplePosition = SamplePosition.MID;
                markerArm.intiTime(2.0/3.0);
                task = Task.CLAIM;
                sigma.disable();
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
                        if (markerArm.release()) {
                            task = Task.PARK;
                            omikron.init(4.0/3.0);
                        }
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
                        if (markerArm.release()) {
                            task = Task.PARK;
                            omikron.init(4.0/3.0);
                        }
                        break;
                    default:
                }
                break;
            case PARK:
                if (!omikron.isTimerUp()) {
                    driveTrain.left.setPower(1);
                    driveTrain.right.setPower(1);
                } else if (omikron.isTimerUp()){
                    driveTrain.stopMotors();
                    task = Task.DONE;
                }
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
