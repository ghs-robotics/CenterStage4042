package org.firstinspires.ftc.teamcode.bot.control.auto_execution;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.bot.Robot;
import org.firstinspires.ftc.teamcode.bot.components.pixel_delivery.Delivery;

public class AutoActions {
    // identities
    public static final int DONE = -1;
    public static final int MOVE = 0;
    public static final int INTAKE = 1;
    public static final int LIFT = 2;
    public static final int PLACE = 3;
    public static final int ALIGN = 4;
    public static final int WAIT = 5;
    public static final int DROP = 6;
    public static final int EXTEND = 7;
    public static final int RETRACT = 8;

    private Robot robot;

    private int identity;
    private boolean endAction;

    private ElapsedTime timer;
    private boolean timerReset;

    private int zone;
    private String description;

    private int moveToCycle;

    ParamHandler params;

    public AutoActions(int id, Robot robot){
        this.identity = id;
        this.robot = robot;
        timerReset = false;
        timer = new ElapsedTime();

        setDescription();
        moveToCycle = 3;
    }

    public AutoActions(int id, Robot robot, ParamHandler params){
        this(id, robot);
        this.params = params;
    }

    /**
     * Driving the rob
     */
    private void moveTo(){
        resetTimer();
        moveToCycle = robot.nav.runToPosition(params.x, params.y, params.heading,
                params.driveXFirst, moveToCycle);

        endAction = moveToCycle == -1||
                    timer.milliseconds() > 10000;
    }

    private void dropPixels(){
        resetTimer();

        robot.delivery.autoDropPixels(Delivery.DROPPER_SECOND);
        if (timer.milliseconds() > 700)
            robot.delivery.autoDropPixels(Delivery.DROPPER_INTAKING);

        endAction = timer.milliseconds() > 1000;
    }

    private void runIntake(){
        robot.intake.setIntakeHeight(params.intakeLevel);

        resetTimer();

        if (timer.milliseconds() < 2550)
            robot.intake.pixelIn(1);
        else
            robot.intake.pixelIn(0);
        endAction = timer.milliseconds() > 2700;
    }

    /**
     * Runs the lift
     */

    private void runLift(){
        // same as intake
        robot.delivery.driveLiftToPosition(params.liftLevel);
        resetTimer();
        endAction = timer.milliseconds() > 750;
    }

    private void extendDropper(){
        resetTimer();
        if(timer.milliseconds() < 1400)
            robot.delivery.setExtensionPower(-1);
        else
            robot.delivery.setExtensionPower(0);
        endAction = timer.milliseconds() > 2000;
    }

    private void retractDropper(){
        resetTimer();

        endAction = robot.delivery.autoRunExtension(1, timer.milliseconds());
    }

    /**
     * drops pixel by reversing Intake
     */
    private void placePixel(){
        robot.intake.autoPixelOut();
        resetTimer();
        if(timer.milliseconds() > 4000)
            endAction = true;
    }

    private void alignBotToTag(){
        // looks for the required tag
        // requires the use of moving to align itself
        // runs the delivery
    }

    /**
     * waits out timer until timer is greater than or equal to the parameter wait time
     */
    private void waiting() {
        resetTimer();

        endAction = timer.milliseconds() > (params.waitTime * 1000);
    }

    private void shutOffBot(){
        robot.shutOff();
    }

    /**
     * @return whether or not this action has been completed
     */
    public boolean isFinished(){
        return endAction;
    }

    /**
     * determines the action and what this specific action will do.
     */
    public void runAction(){
        switch (identity){
            case DONE:
                shutOffBot();
                break;
            case MOVE:
                moveTo();
                break;
            case INTAKE:
                runIntake();
                break;
            case LIFT:
                runLift();
                break;
            case PLACE:
                placePixel();
                break;
            case ALIGN:
                alignBotToTag();
                break;
            case WAIT:
                waiting();
                break;
            case DROP:
                dropPixels();
                break;
            case EXTEND:
                extendDropper();
                break;
            case RETRACT:
                retractDropper();
                break;
        }
    }

    /**
     * @return description of the specific object's action and status
     */
    public String getDescription(){
        return description;
    }

    /**
     * helper method to get telemetry text
     */
    private void setDescription() {
        description = "";
        switch (identity){
            case MOVE:
                description = "Driving";
                break;
            case INTAKE:
                description = "Running Intake for " + (timer.milliseconds() / 1000.0) + " sec";
                break;
            case LIFT:
                description = "Delivering pixel to backdrop";
                break;
            case PLACE:
                description = "Placing pixel on spike mark";
                break;
            case ALIGN:
                description = "Aligning with the AprilTag";
                break;
            case WAIT:
                description = "waiting";
                break;
            case EXTEND:
                description = "Extending the Outtake";
                break;
            case RETRACT:
                description = "retracting the Outtake";
                break;
        }
    }

    /**
     * sets the zone read by the camera
     */
    public void setZone(int zone){
        this.zone = zone;
    }

    /**
     * @return the identity of this action
     */
    public int getIdentity(){
        return identity;
    }

    private void resetTimer(){
        if (!timerReset){
            timer.reset();
            timerReset = true;
        }
    }

    public int getTimer() {
        return (int) (timer.milliseconds() / 1000);
    }
}
