package org.firstinspires.ftc.teamcode.opmodes_teleop.input;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Controller {
    private Gamepad gamepad;

    public Button a = new Button();
    public Button b = new Button();
    public Button x = new Button();
    public Button y = new Button();
    public Button left_bumper = new Button();
    public Button right_bumper = new Button();
    public Button dpad_left = new Button();
    public Button dpad_right = new Button();
    public Button dpad_up = new Button();
    public Button dpad_down = new Button();

    public double left_stick_x;
    public double left_stick_y;
    public double right_stick_x;
    public double right_stick_y;
    public double left_trigger;
    public double right_trigger;

    public Controller(Gamepad gamepad){
        this.gamepad = gamepad;
        update();
    }

    public void update(){
        a.update(gamepad.a);
        b.update(gamepad.b);
        x.update(gamepad.x);
        y.update(gamepad.y);
        left_bumper.update(gamepad.left_bumper);
        right_bumper.update(gamepad.right_bumper);
        dpad_left.update(gamepad.dpad_left);
        dpad_right.update(gamepad.dpad_right);
        dpad_up.update(gamepad.dpad_up);
        dpad_down.update(gamepad.dpad_down);

        left_stick_x = gamepad.left_stick_x;
        left_stick_y = gamepad.left_stick_y;
        right_stick_x = gamepad.right_stick_x;
        right_stick_y = gamepad.right_stick_y;
        left_trigger = gamepad.left_trigger;
        right_trigger = gamepad.right_trigger;

    }
}
