package org.firstinspires.ftc.teamcode.control.cv.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.control.cv.Camera;
import org.firstinspires.ftc.teamcode.opmodes_teleop.input.Controller;

@TeleOp
public class CameraTest extends LinearOpMode {

    Controller gp1;
    Controller gp2;

    Camera cam;
    FtcDashboard dashboard;

    @Override
    public void runOpMode() throws InterruptedException {
        dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        cam = new Camera(hardwareMap, telemetry, true);

        gp1 = new Controller(gamepad1);
        gp2 = new Controller(gamepad2);

        dashboard.startCameraStream(cam.camera, 0);

        cam.setCamera();

        waitForStart();

        telemetry.addLine("Initializing");

        while (opModeIsActive()){
            gp1.update();
            gp2.update();

            cam.switchCamera(gp1.a.pressed());

            cam.getTelemetry();
            telemetry.update();
        }
        cam.closeCamera();

    }
}
