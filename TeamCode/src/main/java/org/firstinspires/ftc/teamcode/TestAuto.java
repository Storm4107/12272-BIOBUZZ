package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous
public class TestAuto extends LinearOpMode {

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

    private void driveForward(double power, long timeMs) {
        setLeftPower(power);
        setRightPower(power);
        sleep(timeMs);
        stopDrive();
    }

    private void turn180(double power, long timeMs){
        setLeftPower(power);
        setRightPower(-power);
        sleep(timeMs);
        stopDrive();
    }
    private void turn90(double power, long timeMs){
        setLeftPower(-power);
        setRightPower(power);
        sleep(timeMs);
        stopDrive();
    }
    private void stopDrive(){
        setRightPower(0);
        setLeftPower(0);
    }

    /*private boolean seesTags(int minId, int maxId) {
        long startTime = System.currentTimeMillis();
        long timeout = 600;
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            telemetry.addData("Limelight", "No Valid Result");
            telemetry.update();
            return false;
        }
            for (LLResultTypes.FiducialResult fiducial : result.getFiducialResults()) {
                int id = fiducial.getFiducialId();
                telemetry.addData("SeenId", id);

                if (id >= minId && id <= maxId) {
                    telemetry.addData("Matched Range", minId + "-" + maxId);
                    telemetry.update();
                    return true;
                }
                telemetry.update();
                sleep(50);
            }
        telemetry.addData("No tags in range", minId +"-" + maxId);
        telemetry.update();
        return false;
    }

     */

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
        sleep(300);

        telemetry.addData("LeftSideTag", LeftSideTag);
        telemetry.addData("RightSideTag", RightSideTag);

        telemetry.update();

        waitForStart();

        /*boolean firstSetSeen = seesTags(38, 41);
        telemetry.addData("First set Seen", firstSetSeen);
        telemetry.update();

        if (!firstSetSeen) {
            driveForward(.5, 3700);
            turn180(.4, 2000);
            sleep(500);
        }

        boolean secondSetSeen = seesTags(42,45);
        telemetry.addData("Second set Seen", secondSetSeen);
        telemetry.update();

        if (secondSetSeen) {
            turn90(.4, 900);
        } else {
            telemetry.addData("Action", "Did not see 42-45");
            telemetry.update();
        }

        while (opModeIsActive()) {
            telemetry.addData("Status", "Auto Finished");
            telemetry.update();
            sleep(100);
        }
        */
        boolean firstSearchComplete = false;

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
                }
                if (id == 42 || id == 43 || id == 44 || id == 45) {
                    rightSeen = true;
                }
            }
            if (!firstSearchComplete && !leftSeen) {
                driveForward(.5,3700);
                turn180(.4,1800);

                firstSearchComplete = true;
            }

            if (firstSearchComplete && rightSeen) {
                turn90(.4,900);
                stopDrive();
                break;
            }
        }

    }


}

