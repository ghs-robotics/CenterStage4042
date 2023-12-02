package org.firstinspires.ftc.teamcode.cv.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cv.Camera;
import org.firstinspires.ftc.teamcode.opmodes.input.Controller;

@Autonomous
public class TestCameraAuto extends LinearOpMode {

    Controller gp1;
    Controller gp2;

    Camera cam;
    FtcDashboard dashboard;

    @Override
    public void runOpMode() throws InterruptedException {
        dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        cam = new Camera(hardwareMap, telemetry, false);

        gp1 = new Controller(gamepad1);
        gp2 = new Controller(gamepad2);

        dashboard.startCameraStream(cam.camera1, 0);
        cam.initCamera();
        waitForStart();

        telemetry.addLine("Initializing");

        while (opModeIsActive()){
            gp1.update();
            gp2.update();

//            cam.testCounters();

            cam.getTelemetry();
            telemetry.update();
        }
        cam.closeCamera();

    }
}