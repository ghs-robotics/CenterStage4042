package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import static org.firstinspires.ftc.teamcode.bot.control.auto_execution.AutoActions.MOVE;
import static org.firstinspires.ftc.teamcode.bot.control.auto_execution.AutoActions.MOVE_TO_SPIKE;
import static org.firstinspires.ftc.teamcode.bot.control.auto_execution.AutoActions.WAIT;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.bot.Robot;
import org.firstinspires.ftc.teamcode.bot.control.auto_execution.AutoActionHandler;
import org.firstinspires.ftc.teamcode.bot.control.auto_execution.presets.AutoPresets;

@Autonomous
public class AutoRed extends LinearOpMode {
    Robot robot;
    AutoActionHandler actionHandler;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap, telemetry, true);
        actionHandler = new AutoActionHandler(robot, telemetry);
        robot.init();


        // create list of actions to run
//                0.0, true));
        //left
        actionHandler.add(MOVE_TO_SPIKE);
//        actionHandler.add(MOVE, 0, 200, 0.0);
//        actionHandler.add(WAIT, 3);
//        actionHandler.add(MOVE, 0, 0, 0.0);
//        actionHandler.add(WAIT, 20);


//        telemetry.addLine("queuing actions");
//        telemetry.addLine(actionHandler.getTotalActions() + " total actions");
        robot.getAutoTelemetry();

        actionHandler.init();
        waitForStart();
        //actionHandler.findAndSetZone();

        while (opModeIsActive()){
            actionHandler.run();
            robot.update();
            robot.getAutoTelemetry();
            telemetry.addLine();
            actionHandler.status();
            //robot.getAutoTelemetry();
        }
    }
}