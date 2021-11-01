package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import java.lang.Math;
import java.util.Arrays;

@TeleOp(name= "test Mecanum", group="TeleOp")
public class MecanumTeleop extends LinearOpMode {
    ElapsedTime runtime = new ElapsedTime();
    CustomMotor[] motors = {
            new CustomMotor("leftFront",    new PIDCoefficients(    15, 0, 1)),
            new CustomMotor("leftBack",     new PIDCoefficients(    15, 0, 1)),
            new CustomMotor("rightFront",   new PIDCoefficients(    15, 0, 1)),
            new CustomMotor("rightBack",    new PIDCoefficients(    15, 0, 1)),
            new CustomMotor("cascadeMotor", new PIDCoefficients(    1,  1, 1)),
            new CustomMotor("carouselMotor",                        null)
    };

    Servo leftArm  = null;
    Servo rightArm = null;

    @Override
    public void runOpMode(){
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        motors[0].motor  = hardwareMap.get(DcMotor.class,           "left_Front");
        motors[1].motor  = hardwareMap.get(DcMotor.class,           "right_Front");
        motors[2].motor  = hardwareMap.get(DcMotor.class,           "left_Back");
        motors[3].motor  = hardwareMap.get(DcMotor.class,           "right_Back");
        motors[4].motor  = hardwareMap.get(DcMotor.class,           "cascade");
        motors[5].motor  = hardwareMap.get(DcMotor.class,           "car");
        leftArm          = hardwareMap.get(Servo.class,             "leftArm");
        rightArm         = hardwareMap.get(Servo.class,             "rightArm");

        motors[0].motor.setDirection(DcMotor.Direction.FORWARD);
        motors[1].motor.setDirection(DcMotor.Direction.FORWARD);
        motors[2].motor.setDirection(DcMotor.Direction.FORWARD);
        motors[3].motor.setDirection(DcMotor.Direction.FORWARD);
        motors[4].motor.setDirection(DcMotor.Direction.FORWARD);
        motors[5].motor.setDirection(DcMotor.Direction.FORWARD);

        leftArm.setDirection(Servo.Direction.FORWARD);
        rightArm.setDirection(Servo.Direction.REVERSE);

        motors[0].motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors[1].motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors[2].motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors[3].motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motors[4].motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            //drivetrain
            double leftPower;
            double rightPower;
            double y  = -gamepad1.left_stick_y;
            double x  =  gamepad1.left_stick_x;
            double rx =  gamepad1.left_stick_x;

            double[] velocity = {
                    y + x - rx,
                    y - x + rx,
                    y - x - rx,
                    y + x - rx
            };
            double highestValue = 0;
            for (double ix : velocity) {
                if (Math.abs(ix) > highestValue) {
                    highestValue = Math.abs(ix);
                }
            }
            if (highestValue > 1) {
                for (double ix : velocity) {
                    ix /= highestValue;
                }
            }

            for (int i = 0; i < 4; i++) {
                motors[i].controller.setTargetPosition(velocity[i]);
                motors[i].motor.setPower(motors[i].controller.update(motors[i].motor.getCurrentPosition()));
            }


            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", Arrays.toString(velocity));

            //Cascade
            if (gamepad1.a && !gamepad1.b) {
                motors[4].motor.setPower(0.2);
            } else if (gamepad1.b && !gamepad1.a) {
                motors[4].motor.setPower(-0.2);
            } else {
                motors[4].motor.setPower(0);
            }

            //Carousel
            if (gamepad1.left_bumper) {
                motors[5].motor.setPower(0.5);
            } else {
                motors[5].motor.setPower(0.0);
            }

            //Claw
            if (gamepad1.dpad_up && !gamepad1.dpad_down) {
                leftArm.setPosition(0.5);
                rightArm.setPosition(0.5);
            } else if (!gamepad1.dpad_up && gamepad1.dpad_down) {
                leftArm.setPosition(0);
                rightArm.setPosition(0);
            }

            telemetry.addData("Cascade Motor power: ",    motors[4].motor.getPower());
            telemetry.addData("Cascade Motor position: ", motors[4].motor.getCurrentPosition());
            telemetry.update();

        }
    }
}
