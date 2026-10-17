package org.firstinspires.ftc.teamcode;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;


@TeleOp
public class ShooterTesting extends LinearOpMode {

    private DcMotorEx Shooter;

    private PIDFController shooterPID = new PIDFController(new PIDFCoefficients(30,0,0,10));

    private double targetVelocity = 0.0;


    private static final double NOMINAL_VOLTAGE = 13.0;

    public void setTargetVelocity(double velocity) {
        targetVelocity = velocity;
    }


    @Override
    public void runOpMode() {
            Shooter = hardwareMap.get(DcMotorEx.class, "Shooter");

            Shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            waitForStart();

            while (opModeIsActive()) {


                if (gamepad2.x) {
                    setTargetVelocity(1200);
                }

                double currentVel = Shooter.getVelocity();

                shooterPID.setTargetPosition(targetVelocity);
                shooterPID.updatePosition(currentVel);
                double power = shooterPID.run();


                power = Math.max(0.0, Math.min(1, power));
                Shooter.setPower(power);

                telemetry.addData("Velocity", currentVel);
                telemetry.update();
            }
    }

}

