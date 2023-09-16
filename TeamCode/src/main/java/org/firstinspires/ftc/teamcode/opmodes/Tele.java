package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.bot.Robot;
import org.firstinspires.ftc.teamcode.opmodes.input.Controller;

@TeleOp
public class Tele extends LinearOpMode {
    Robot robot;
    Controller gp1;
    Controller gp2;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap, telemetry);
        gp1 = new Controller(gamepad1);
        gp2 = new Controller(gamepad2);

        robot.init();

        telemetry.addLine("Initializing");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            gp1.update();
            gp2.update();
            //-------------------------------------------------------------------------------------
            //                                  GAMEPAD 1
            //-------------------------------------------------------------------------------------
            robot.drive.calculateDrivePowers(gp1.left_stick_x, gp1.left_stick_y, gp1.right_stick_x);


            //-------------------------------------------------------------------------------------
            //                                  GAMEPAD 2
            //-------------------------------------------------------------------------------------
            robot.lift.driveLift(gp2.left_stick_y);


//            if (gp2.dpad_right) {
//                outtake.pixelOut();
//            }

//            if (gp2.a) {
//                lift.setLow();
//            }
//
//            if (gp2.x) {
//                lift.setMid();
//            }
//
//            if (gp2.y) {
//                lift.setHigh();
//            }

            //-------------------------------------------------------------------------------------
            //                                  TELEMETRY
            //-------------------------------------------------------------------------------------
            robot.update();
            robot.getTelemetry();
            telemetry.update();
            telemetry.update();
        }
    }
}