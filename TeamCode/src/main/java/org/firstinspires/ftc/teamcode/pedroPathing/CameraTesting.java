package org.firstinspires.ftc.teamcode.pedroPathing;


import com.qualcomm.hardware.limelightvision.LLFieldMap;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.hardware.motors.MotorGroup;

import java.util.List;

@TeleOp(name = "CameraTesting")
public class CameraTesting extends LinearOpMode {

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;
    private Limelight3A limelight;


    private boolean LeftSideTag = false;

    private boolean RightSideTag = false;



    private void setLeftPower(double power) {
        leftFront.setPower(power);
        leftBack.setPower(power);
    }

    private void setRightPower(double power) {
        rightFront.setPower(power);
        rightBack.setPower(power);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        limelight = hardwareMap.get(Limelight3A.class, "Limelight");

        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        limelight.pipelineSwitch(0);
        limelight.start();

        telemetry.addData("Astate", gamepad1.a);
        telemetry.addData("LeftSideTag", LeftSideTag);
        telemetry.addData("RightSideTag", RightSideTag);

        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            boolean leftSeen = false;
            boolean rightSeen = false;

            LLResult result = limelight.getLatestResult();

            result.getFiducialResults();

            for (LLResultTypes.FiducialResult fiducial : result.getFiducialResults()) {
                telemetry.addData("AprilTag Id:", fiducial.getFiducialId());

                int id = fiducial.getFiducialId();

                
                if (id == 38 || id == 39 || id == 40 || id == 41) {
                    leftSeen = true;
                    rightSeen = false;
                }
                else if (id == 42 || id == 43 || id == 44 || id == 45) {
                       rightSeen = true;
                        leftSeen = false;
                   }


                /*if (gamepad1.a && RightSideTag){
                        setRightPower(.5);
                        setLeftPower(-.5);
                        sleep(500);
                        setLeftPower(0);
                        setRightPower(0);
                } else if (!gamepad1.a && LeftSideTag){
                    setRightPower(-.5);
                    setLeftPower(.5);
                    sleep(1000);
                    setLeftPower(0);
                    setRightPower(0);
                }
                 */
            }
            LeftSideTag = leftSeen;
            RightSideTag = rightSeen;

            if (gamepad1.a && RightSideTag){
                setRightPower(.5);
                setLeftPower(-.5);
                sleep(500);
                setLeftPower(0);
                setRightPower(0);
            } else if (!gamepad1.a && LeftSideTag){
                setRightPower(-.5);
                setLeftPower(.5);
                sleep(500);
                setLeftPower(0);
                setRightPower(0);
            }


            telemetry.addData("Astate", gamepad1.a);
            telemetry.addData("LeftSideTag", LeftSideTag);
            telemetry.addData("RightSideTag", RightSideTag);
            telemetry.addData("LeftSeen", leftSeen);
            telemetry.addData("RightSeen", rightSeen);

            telemetry.update();

        }

    }

}
