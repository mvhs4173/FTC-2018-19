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
    Imu imu;
    Timer sigma = new Timer();
    Timer omikron = new Timer();
    boolean isTurningDone = false;

    /**
     * The Constructor
     * @param hardware The set of Hardware you are using
     * @param driveTrain The DriveTrain you are using
     * @param hanger The Hanging mechanism you are using
     * @param imu the imu being used
     * @param markerArm the markerArm assembly being used
     */
    public AutoPath(Hardware hardware, DriveTrain driveTrain, Hanger hanger, Imu imu, MarkerArm markerArm){
        this.hardware = hardware;
        this.driveTrain = driveTrain;
        this.hanger = hanger;
        this.imu = imu;
        this.markerArm = markerArm;
    }

    /**
     * our list of tasks for the Autonomous
     */
    enum Task {
        DROP, DRIVE, FINDGOLD, CLAIM, PARK, DONE

    }

    /**
     * Our start position on the field
     * Gold side of the lander towards depot
     * Silver side of lander towards crater
     */
    enum Start {
        GOLD,
        SILVER
    }

    /**
     * Which team are we in the match
     */
    enum Team {
        BLUE,
        RED
    }

    /**
     * Where is the gold mineral in the sampling field
     * Robots perspective
     */
    enum SamplePosition {
        LEFT,MID,RIGHT
    }

    /**
     * Used to set the start position externally
     * @param desiredStart Which position are you starting Gold side or Silver side
     */
    void setStart(Start desiredStart){
        this.start = desiredStart;
    }

    /**
     * To set the team color externally
     * @param team Blue or Red team
     */
    void setTeamColor(Team team){
        this.team = team;
    }

    /**
     * sets the initial variables to start the switch statements
     * we start out hanging and want to drop
     */
    void init(){
        task = Task.DROP;
        hanger.dropOrder = Hanger.Order.INIT; // tells the robot we is ready
    }

    /**
     * our main call function.
     * Put this in your Auto loop method
     */
    void execute(){
        switch (task){
            case DROP:
                hanger.task = Hanger.Task.DROP; // starts the command to drop
                hanger.execute(false); // set to false for auto, button is used in teleop
                if (hanger.dropOrder == Hanger.Order.DONE){ // once the drop command finishes
                    sigma.init(6.5/3.0); // TODO: 12/14/2018 Implement distance driving
                    task = Task.DRIVE; //goto the next step
                }
                break;
            case DRIVE:
                if (!sigma.isTimerUp()) { // when the timer is still running drive forwards
                    driveTrain.left.setPower(1); // TODO: 12/14/2018 figure out why forwards is negative
                    driveTrain.right.setPower(1);
                } else if (sigma.isTimerUp()){ // have we moved far enough
                    driveTrain.stopMotors();
                    task = Task.FINDGOLD;
                }
                break;
            case FINDGOLD:
                samplePosition = SamplePosition.MID; // temporary fo no null objects
                markerArm.intiTime(2.0/3.0); // the amount of time to allow servo to move
                task = Task.CLAIM; // next task
                sigma.disable();
                break;
            case CLAIM:
                switch (start) {
                    case GOLD:
                        switch (samplePosition){ // TODO: 12/14/2018 finish the implementation of finding the cube
                            case MID:
                                break;
                            case LEFT:
                                break;
                            case RIGHT:
                                break;
                            default:
                        }
                        if (markerArm.release()) { //once the markerArm is finished
                            task = Task.PARK;      //move on to getting out of the way
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
                if (!omikron.isTimerUp()) { // drive back until desired distance
                    driveTrain.left.setPower(-1); //same wonder about negative forward
                    driveTrain.right.setPower(-1);
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
            case DONE: //do nothing once done
                break;
            default:
        }
    }
}
