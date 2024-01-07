package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import static org.firstinspires.ftc.teamcode.bot.control.auto_execution.AutoActions.DROP;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.bot.Robot;
import org.firstinspires.ftc.teamcode.bot.control.auto_execution.AutoActionHandler;
import org.firstinspires.ftc.teamcode.bot.control.auto_execution.presets.AutoPresets;

@Autonomous
public class AutoRedLong extends LinearOpMode {
    Robot robot;
    AutoActionHandler actionHandler;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap, telemetry);
        actionHandler = new AutoActionHandler(robot, telemetry);
        robot.init();

        // create list of actions to run
        actionHandler.add(AutoPresets.getBeginningNearPixels(robot, telemetry));
//        actionHandler.add(LIFT);
//        actionHandler.add(EXTEND);
        actionHandler.add(DROP);
//        actionHandler.add(RETRACT);




        telemetry.addLine("queuing actions");
        telemetry.addLine(actionHandler.getTotalActions() + " total actions");

        waitForStart();
        //actionHandler.findAndSetZone();
        actionHandler.init();

        while (opModeIsActive()){
            actionHandler.run();
            actionHandler.status();
            robot.update();
//            robot.getTelemetry();
        }
    }
}