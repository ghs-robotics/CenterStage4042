package org.firstinspires.ftc.teamcode.cv.testing;

import static org.firstinspires.ftc.teamcode.cv.testing.TestingConstants.BLOCK_DARK_H;
import static org.firstinspires.ftc.teamcode.cv.testing.TestingConstants.BLOCK_DARK_S;
import static org.firstinspires.ftc.teamcode.cv.testing.TestingConstants.BLOCK_DARK_V;
import static org.firstinspires.ftc.teamcode.cv.testing.TestingConstants.BLOCK_LIGHT_H;
import static org.firstinspires.ftc.teamcode.cv.testing.TestingConstants.BLOCK_LIGHT_S;
import static org.firstinspires.ftc.teamcode.cv.testing.TestingConstants.BLOCK_LIGHT_V;
import static org.firstinspires.ftc.teamcode.cv.testing.TestingConstants.CANNY;
import static org.firstinspires.ftc.teamcode.cv.testing.TestingConstants.FILTER;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class Pipeline extends OpenCvPipeline {
    boolean viewportPaused = false;

    OpenCvCamera cam;

    Telemetry telemetry;

    Mat hsv = new Mat();
    Mat display = new Mat();

    int zone1count;
    int zone2count;
    int zone3count;

    int spikeZone;

    public static final int SPIKE_LEFT = 1;
    public static final int SPIKE_CENTER = 2;
    public static final int SPIKE_RIGHT = 3;

    public Pipeline (OpenCvCamera camera, Telemetry telemetry){
        cam = camera;
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_BGR2HSV, 3);

        display = processHSV(hsv);

        zone1count = countPixels(1);
        zone2count = countPixels(2);
        zone3count = countPixels(3);

        if (zone1count > zone2count && zone1count > zone3count)
            spikeZone = SPIKE_LEFT;
        else if (zone2count > zone1count && zone2count > zone3count)
            spikeZone = SPIKE_CENTER;
        else
            spikeZone = SPIKE_RIGHT;

        return display;
    }

    public int getZone(){
        return spikeZone;
    }

    private int countPixels(int z){
        int counter = 0;

        int row = (z - 1) * display.rows() / 3;

        for (int col = 0; col < display.cols(); col++){
            for (int r = row; r < (display.rows() * z) / 3; row++){
                double[] dataList = display.get(row, col);

                if (dataList[0] == 255)
                    counter++;
            }
        }

        return counter;
    }

    private Mat processHSV(Mat input){
        Scalar lightRange = new Scalar(BLOCK_LIGHT_H, BLOCK_LIGHT_S, BLOCK_LIGHT_V);
        Scalar darkRange = new Scalar(BLOCK_DARK_S, BLOCK_DARK_H, BLOCK_DARK_V);

        if (FILTER)
            Core.inRange(input, lightRange, darkRange, input);

        return input;
    }

    @Override
    public void onViewportTapped() {
        viewportPaused = !viewportPaused;

        if(viewportPaused)
            cam.pauseViewport();
        else
            cam.resumeViewport();

    }

    public void getTelemetry(){
        telemetry.addLine();
        telemetry.addLine("Pipeline telemetry");
        telemetry.addData("channels: ", display.channels());
        telemetry.addData("dump:     ", display.dump());
        telemetry.addData("type:     ", display.depth());
        telemetry.addLine();
        telemetry.addData("zone 1 counter", zone1count);
        telemetry.addData("zone 2 counter", zone2count);
        telemetry.addData("zone 3 counter", zone3count);
        telemetry.addLine();
    }

}