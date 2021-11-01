package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.DcMotor;
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
public class MecanumTeleop extends OpMode {

    ElapsedTime runtime = new ElapsedTime();
    CustomMotor[] motors = {
            new CustomMotor("leftFront", new PIDCoefficients(15, 0, 1)),
            new CustomMotor("leftBack", new PIDCoefficients(15, 0, 1)),
            new CustomMotor("rightFront", new PIDCoefficients(15, 0, 1)),
            new CustomMotor("rightBack", new PIDCoefficients(15, 0, 1)),
            new CustomMotor("cascadeMotor", new PIDCoefficients(1, 1, 1)),
            /*new CustomMotor("carouselMotor",                        null)*/
    };

    Servo leftArm = null;
    Servo rightArm = null;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        motors[0].motor = hardwareMap.get(DcMotorEx.class, "left Front");
        motors[1].motor = hardwareMap.get(DcMotorEx.class, "right Front");
        motors[2].motor = hardwareMap.get(DcMotorEx.class, "left Back");
        motors[3].motor = hardwareMap.get(DcMotorEx.class, "right Back");
        /* motors[4].motor  = hardwareMap.get(DcMotor.class,           "cascade");
        motors[5].motor  = hardwareMap.get(DcMotor.class,           "car");
        leftArm          = hardwareMap.get(Servo.class,             "left Arm");
        rightArm         = hardwareMap.get(Servo.class,             "right Arm"); */

        motors[0].motor.setDirection(DcMotorEx.Direction.FORWARD);
        motors[1].motor.setDirection(DcMotorEx.Direction.REVERSE);
        motors[2].motor.setDirection(DcMotorEx.Direction.FORWARD);
        motors[3].motor.setDirection(DcMotorEx.Direction.REVERSE);
        /* motors[4].motor.setDirection(DcMotor.Direction.FORWARD);
        motors[5].motor.setDirection(DcMotor.Direction.FORWARD);

        leftArm.setDirection(Servo.Direction.FORWARD);
        rightArm.setDirection(Servo.Direction.REVERSE);

        left front - forward
        right front - backward
        left back - forward
        right back - backward
*/
//        motors[0].motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
//        motors[1].motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
//        motors[2].motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
//        motors[3].motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        /*motors[4].motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/
        motors[0].motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motors[1].motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motors[2].motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motors[3].motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motors[0].motor.setVelocityPIDFCoefficients(15, 0, 0, 0);
        motors[1].motor.setVelocityPIDFCoefficients(15, 0, 0, 0);
        motors[2].motor.setVelocityPIDFCoefficients(15, 0, 0, 0);
        motors[3].motor.setVelocityPIDFCoefficients(15, 0, 0, 0);
    }
    @Override
    public void start() {

        runtime.reset();
    }

    @Override
    public void loop(){
        // setting pid




            //drivetrain
            double leftPower;
            double rightPower;
            double  y =  gamepad1.left_stick_y;
            double  x =  gamepad1.left_stick_x;
            double rx =  gamepad1.right_stick_x;

            double[] velocity = {
                    -y + x + rx, // left front
                    -y - x - rx, // right front
                    -y - x + rx, // left back
                    -y + x - rx // left back
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
                //motors[i].controller.setTargetPosition(velocity[i]);
                motors[i].motor.setVelocity(velocity[i]*5000);
            }


            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", Arrays.toString(velocity));
            telemetry.addData("FRONT LEFT Motor", motors[0].motor.getVelocity());
            telemetry.addData("FRONT RIGHT Motor", motors[1].motor.getVelocity());
            telemetry.addData("BACK LEFT Motor", motors[2].motor.getVelocity());
            telemetry.addData("BACK RIGHT Motor", motors[3].motor.getVelocity());
/*
            //Cascade
            if (gamepad1.a && !gamepad1.b) {
                motors[4].motor.setPower(0.2);
            } else if (gamepad1.b && !gamepad1.a) {
                motors[4].motor.setPower(-0.2);
            } else {
                motors[4].motor.setPower(0);
            }
/*
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

    */
        
    }
}
