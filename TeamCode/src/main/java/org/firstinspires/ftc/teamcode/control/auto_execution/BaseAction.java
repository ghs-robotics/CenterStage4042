package org.firstinspires.ftc.teamcode.control.auto_execution;

import static org.firstinspires.ftc.teamcode.control.auto_execution.AutoActions.MOVE;
import static org.firstinspires.ftc.teamcode.control.cv.Camera.SPIKE_ZONE;
import static org.firstinspires.ftc.teamcode.control.presets.Position.BACKDROP_POS;
import static org.firstinspires.ftc.teamcode.control.presets.Position.BLUE_SPIKE_POS;
import static org.firstinspires.ftc.teamcode.control.presets.Position.RED_SPIKE_POS;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.bot.Robot;
import org.firstinspires.ftc.teamcode.control.PIDControllers.NavigationPID;

public class BaseAction {

    protected int identity;
    protected boolean endAction;

    protected ElapsedTime timer;
    protected boolean timerReset;

    protected String description;

    double x;
    double y;
    double heading; // in degrees

    int intakeLevel;
    int liftLevel = 500;

    double waitTime;
    boolean moveXAxis;

    protected NavigationPID xPID;
    protected NavigationPID yPID;

    double[] pidValues = {.152, .00165765, .0016622};

    protected Robot robot;

    public BaseAction(int id, Robot robot) {

        this.identity = id;
        this.robot = robot;
        timerReset = false;
        timer = new ElapsedTime();
    }


    protected void moveTo(){
        resetTimer();
        if (x == 0){
            xPID.setError(0);
        }
        if (y == 0)
            yPID.setError(0);

        boolean there = robot.drive.runToPosition(xPID, yPID);
        boolean timeOut = timer.milliseconds() > 4550;
        endAction = there || timeOut;
    }

    protected void moveToSpike(){
        setNavPID();

        if (robot.RED){
            x = RED_SPIKE_POS[SPIKE_ZONE][0];
            y = RED_SPIKE_POS[SPIKE_ZONE][1];

        }else {
            x = BLUE_SPIKE_POS[SPIKE_ZONE][0];
            y = BLUE_SPIKE_POS[SPIKE_ZONE][1];
        }

        checkXSign();

        xPID.setTarget(x);

        identity = MOVE;
    }

    protected void moveToBackdrop(){
        setNavPID();

        x = BACKDROP_POS[SPIKE_ZONE][0];
        y = BACKDROP_POS[SPIKE_ZONE][1];

        checkXSign();

        xPID.setTarget(x);

        identity = MOVE;
    }

    protected void dropPixels(){
        resetTimer();

        robot.delivery.autoDropPixels(0.4);
        if (timer.milliseconds() > 550)
            robot.delivery.autoDropPixels(0.15);

        endAction = timer.milliseconds() > 600;
    }

    protected void runIntake(){
        robot.intake.setIntakeHeight(intakeLevel);

        resetTimer();

        if (timer.milliseconds() < 2550)
            robot.intake.pixelIn(1);
        else
            robot.intake.pixelIn(0);
        endAction = timer.milliseconds() > 2700;
    }

    /**
     * Runs the lift - good to go
     */
    protected void runLift(){
        // same as intake
        resetTimer();
        boolean atPos = robot.delivery.driveLiftToPosition(liftLevel, (int) timer.milliseconds());
        endAction = timer.milliseconds() > 2550 || atPos;
        if (endAction)
            robot.delivery.autoDriveLift(0);
    }

    // good to go
    protected void retractDelivery(){
        resetTimer();

        robot.delivery.autoDropPixels(0.15);
        robot.delivery.autoExtend(0);
        boolean atPos = robot.delivery.driveLiftToPosition(12, (int) timer.milliseconds());
        endAction = timer.milliseconds() > 2200 || atPos;
    }

    /**
     * drops pixel by reversing Intake
     */
    protected void placePixel(){
        robot.intake.setIntakeHeight(3);
        robot.intake.autoPixelOut();

        robot.drive.calculateDrivePowers(0,0,0);

        resetTimer();
        if(timer.milliseconds() > 750) {
            endAction = true;
            robot.intake.pixelIn(0);
        }
    }

    protected void extendDropper(){
        resetTimer();
        if(timer.milliseconds() < 1400)
            robot.delivery.autoExtend(0.4);
        endAction = timer.milliseconds() > 2000;
    }

    protected void detectSpikeMark(){
        if (!timerReset)
            robot.cam.detectProp();
        resetTimer();
        robot.cam.getSpikeZone();
        endAction = timer.milliseconds() > 500;
    }

    protected void alignBotToTag(){
        // looks for the required tag
        // requires the use of moving to align itself
        // runs the delivery
    }

    /**
     * waits out timer until timer is greater than or equal to the parameter wait time
     */
    protected void waiting() {
        resetTimer();
        robot.drive.calculateDrivePowers(0,0,0);
        robot.intake.pixelIn(0);
        robot.delivery.driveLift(0);

        endAction = timer.milliseconds() > (waitTime * 1000);
    }

    protected void shutOffBot(){
        robot.shutOff();
    }

    /**
     * @return whether or not this action has been completed
     */
    public boolean isFinished(){
        return endAction;
    }

    private void resetTimer(){
        if (!timerReset){
            timer.reset();
            timerReset = true;
        }
    }

    protected void checkXSign(){
        if (robot.RED)
            this.x *= -1;

    }

    protected void setNavPID(){
        double outPutLimit = 2;
        double integralLimit = 3650;

        xPID = new NavigationPID(pidValues);
        yPID = new NavigationPID(pidValues);

        xPID.setOutputLimits(outPutLimit);
        yPID.setOutputLimits(outPutLimit);

        xPID.setMaxIOutput(integralLimit);
        yPID.setMaxIOutput(integralLimit);
    }
}
