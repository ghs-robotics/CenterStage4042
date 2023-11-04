package org.firstinspires.ftc.teamcode.bot.components.pixel_delivery;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Delivery {
    private DcMotor liftMotor1;
    private DcMotor liftMotor2;

    private CRServo extensionServo;
    private Servo droppingServo;

    private int[] liftMotorPos = {0, 200, 400, 600, 1000};
    private double[] dropServoPos = {0.1, 0.5, 0.6};

    public static final double DROPPER_INTAKING = 0.1;
    public static final double DROPPER_FIRST = 0.5;
    public static final double DROPPER_SECOND = 0.6;

    private double sentPower;

    private int liftLvl = 60;
    private int dropLvl = 60;

    private boolean runLiftToPosition;

    public Delivery (HardwareMap hardwareMap) {
        liftMotor1 = hardwareMap.get(DcMotor.class, "lift1");
        liftMotor2 = hardwareMap.get(DcMotor.class, "lift2");
        extensionServo = hardwareMap.get(CRServo.class, "extend");
        droppingServo = hardwareMap.get(Servo.class, "drop");

        liftMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor2.setDirection(DcMotorSimple.Direction.FORWARD); // currently polarity is reversed
        liftMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        extensionServo.setDirection(DcMotorSimple.Direction.REVERSE);
        droppingServo.setPosition(0);

        runLiftToPosition = false;
    }

    //-------------------------------------------------------------------------------------
    //                                   Auto Functions
    //-------------------------------------------------------------------------------------

    public boolean autoRunExtension(double dir, double curMillisecond){
        if (curMillisecond < 600){
            extensionServo.setPower(dir);
        }else
            extensionServo.setPower(0);

        return curMillisecond > 700;
    }

    public boolean autoDropPixels(double targetPos){
        droppingServo.setPosition(targetPos);
        return droppingServo.getPosition() == targetPos;
    }

    public void setLiftPosition() {
        liftMotor1.setTargetPosition(liftMotorPos[Math.abs(liftLvl % liftMotorPos.length)]);
//        droppingServo.setPosition(dropServoPos[Math.abs(dropLvl % dropServoPos.length)]);
    }

    public boolean driveLiftToPosition(int target){
        if (getLiftPosition() <= target)
            driveLift(getLiftPosition() - target);
        else
            driveLift(-0.2);
        return getLiftPosition() > target;
    }

    //-------------------------------------------------------------------------------------
    //                                   Lift Functions
    //-------------------------------------------------------------------------------------


    public void preventDropperDamage() {
    }

    public void driveLift (double power) {

        liftMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        sentPower = power;

        if (!runLiftToPosition && Math.abs(power) > 0.1) {
            setLiftPower(power);
        } else if (Math.abs(power) < 0.1) {
            setLiftPower(0);
        }
    }

    public void setRunLiftToPosition(boolean button){
        if (button)
            runLiftToPosition = !runLiftToPosition;

        runLiftToPosition();
    }

    public void changeLiftHeight (boolean decrease, boolean increase) {
        if (decrease) {
            liftLvl -= 1;
        }
        if (increase) {
            liftLvl += 1;
        }
        //setLiftPositions();
    }

    public void resetEncoders() {
        liftMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * @param power sets the power of both motors on the lift
     */
    private void setLiftPower(double power){
        int limit = 1430;

        if (getLiftPosition() > limit && power > 0) {
            power = 0;
        }else if (getLiftPosition() > limit - 150){
            power *= (limit - getLiftPosition()) / 200.0;
        } else if (getLiftPosition() < 0 && power > 0){
            power = 0;
        }

        liftMotor1.setPower(power * 0.75);
        liftMotor2.setPower(power * 0.75);
    }

    private void runLiftToPosition(){
        if (runLiftToPosition) {
            setLiftPosition();
            liftMotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftMotor2.setPower(liftMotor1.getPower());
        }
    }

    //-------------------------------------------------------------------------------------
    //                                   Drop Functions
    //-------------------------------------------------------------------------------------

    public void changeDropPosition (boolean increase) {
        if (increase) {
            dropLvl += 1;
        }
        setLiftPosition();
    }

    public double getDropPosition () {
        return droppingServo.getPosition();
    }

    //-------------------------------------------------------------------------------------
    //                                   Outtake Functions
    //-------------------------------------------------------------------------------------

    public void extendOuttake (double power) {
        extensionServo.setPower(power);
    }

    //-------------------------------------------------------------------------------------
    //                                   Telemetry Functions
    //-------------------------------------------------------------------------------------

    public int getLiftPosition() {
        return liftMotor1.getCurrentPosition();
    }

    public boolean getLiftMode(){
        return runLiftToPosition;
    }

    public int getLiftLvl(){
        return Math.abs(liftLvl % liftMotorPos.length);
    }

    public double getSentPower() {
        return sentPower;
    }

    public int getExtensionLvl(){
        return Math.abs(dropLvl % dropServoPos.length);
    }
}
