package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.HardwareMap.HardwareMap_CompetitionBot;


/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="B1 Park Warehouse ShippingHub", group="Blue")
//@Disabled
public class B1_Park_Warehouse_Dump_ShippingHub extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareMap_CompetitionBot robot   = new HardwareMap_CompetitionBot();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        int state = 0;
        if (state == 0){
            //init robot
            robot.init(hardwareMap);


            // Send telemetry message to signify robot waiting;
            telemetry.addData("Status", "Resetting Encoders");    //
            telemetry.update();

            robot.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // Send telemetry message to indicate successful Encoder reset
            telemetry.addData("Path0",  "Starting at %7d :%7d",
                    robot.leftFront.getCurrentPosition(),
                    robot.rightFront.getCurrentPosition());
            telemetry.update();


            waitForStart();
            state = 1;
        }
        //raise lift to level 1 prior to moving
        if (state == 1){
            telemetry.addData("State","1");
            telemetry.update();

            state = 2;
        }
        //move forward to shipping hub
        if (state == 1){
            telemetry.addData("State","1");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, 4, 4, 4, 4, 4.0);
            state = 2;
        }
        //turn right so that intake system faces shipping hub
        if (state == 2) {
            telemetry.addData("State","2");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, 5, -5, 5, -5, 4.0);
            state = 3;
        }
        //move forward so that robot is right in front of shipping hub
        if (state == 3) {
            telemetry.addData("State", "3");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, 5, 5, 5, 5, 4.0);
            state = 4;
        }
        //turn left so that the intake system is facing shipping hub
        if (state == 4) {
            telemetry.addData("State","4");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, -5, 5, -5, -5, 4.0);
            state = 5;
        }
        //move forward so that intake system is right up against shipping hub
        if (state == 5) {
            telemetry.addData("State", "5");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, 2, 2, 2, 2, 4.0);
            state = 6;
        }
        //deposit freight into bottom level of shipping hub
        if(state == 6){
            telemetry.addData("State", "6");
            telemetry.update();
            retractFreight(3, 1);
            state = 7;
        }
        //stop intake motors
        if(state == 7){
            telemetry.addData("State", "7");
            telemetry.update();
            robot.leftIntake.setPower(0);
            robot.rightIntake.setPower(0);
            state = 8;
        }
        //back up so that robot has room to turn towards warehouse
        if (state == 8) {
            telemetry.addData("State", "8");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, -1, -1, -1, -1, 4.0);
            state = 9;
        }
        //turn left so the robot's front is pointed towards warehouse
        if (state == 9) {
            telemetry.addData("State","9");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, -5, 5, -5, -5, 4.0);
            state = 10;
        }
        //move forward straight into the warehouse
        if (state == 10) {
            telemetry.addData("State", "10");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, 14, 14, 14, 14, 4.0);
            state = 11;
        }
        //stop robot
        if(state == 11){
            telemetry.addData("State", "11");
            telemetry.update();
            robot.leftFront.setPower(0);
            robot.rightFront.setPower(0);
            robot.leftBack.setPower(0);
            robot.rightBack.setPower(0);
            state = 12;
        }
        sleep(1000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();




    }

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches, double leftBackInches, double rightBackInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;
        int newLeftBackTarget;
        int newRightBackTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftFront.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.rightFront.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newLeftBackTarget = robot.leftBack.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightBackTarget = robot.rightBack.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.leftFront.setTargetPosition(newLeftTarget);
            robot.rightFront.setTargetPosition(newRightTarget);
            robot.leftBack.setTargetPosition(newLeftBackTarget);
            robot.rightBack.setTargetPosition(newRightBackTarget);

            // Turn On RUN_TO_POSITION
            robot.leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftFront.setPower(Math.abs(speed));
            robot.rightFront.setPower(Math.abs(speed));
            robot.leftBack.setPower(Math.abs(speed));
            robot.rightBack.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftFront.isBusy() && robot.rightFront.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.leftFront.getCurrentPosition(),
                        robot.rightFront.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.leftFront.setPower(0);
            robot.rightFront.setPower(0);
            robot.leftBack.setPower(0);
            robot.rightBack.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }


    /*
    METHODS FOR ALL AUTONOMOUS
  */
    public void retractFreight(double freightTime, double freightSpeed) {
        while (opModeIsActive() &&
                (runtime.seconds() < freightTime)) {
            robot.leftIntake.setPower(freightSpeed);
            robot.rightIntake.setPower(freightSpeed);
        }
    }
    public void spinWheel(double wheelTime, double wheelSpeed) {
        while (opModeIsActive() &&
                (runtime.seconds() < wheelTime)) {
            robot.duckMotor.setPower(wheelSpeed);
        }
    }

    public void setLiftPosition(int position, double speed) {
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setTargetPosition(position);
        robot.lift.setPower(speed);

        while((robot.lift.getCurrentPosition() > robot.lift.getTargetPosition() + 1
                || robot.lift.getCurrentPosition() < robot.lift.getTargetPosition() - 1)
                && opModeIsActive()) {
            telemetry.addData("Encoder Position",robot.lift.getCurrentPosition());
            telemetry.update();
            idle();
        }
        robot.lift.setPower(0);
    }



    public void gyroTurn(double power, double target) {
        Orientation angles;
        double error;
        double k = 6 / 360.0;
        double kInt = 3 / 3600.0;
        double eInt = 0;
        double prevTime = System.currentTimeMillis();
        double globalAngle = 0;
        double lastAngle = 0;
        double deltaAngle = 0;
        while (opModeIsActive()) {
            double currentTime = System.currentTimeMillis();
            double loopTime = (currentTime - prevTime) / 1000.0; // In seconds
            prevTime = currentTime;
            angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            //finds the angle given by the imu [-180, 180]
            double angle = angles.firstAngle;
            deltaAngle = angle - lastAngle;

            //adjusts the change in angle (deltaAngle) to be the actual change in angle
            if (deltaAngle < -180) {
                deltaAngle += 360;
            } else if (deltaAngle > 180) {
                deltaAngle -= 360;
            }
            globalAngle += deltaAngle;
            lastAngle = angle;

            error = target - globalAngle;
            eInt += loopTime * error;
            telemetry.addData("Heading", angles.firstAngle + " degrees");
            telemetry.addData("GlobalAngle", globalAngle + " degrees");
            telemetry.addData("Error", error + " degrees");
            telemetry.addData("Loop time: ", loopTime + " ms");
            telemetry.update();
            if (error == 0) {
                stopMotors();
                break;
            }
            turnLeft(k * error + kInt * eInt);
            idle();
        }
    }
    public void stopMotors() {
        robot.leftFront.setPower(0);
        robot.leftBack.setPower(0);
        robot.rightFront.setPower(0);
        robot.rightBack.setPower(0);
    }
    public void turnLeft(double power) {
        robot.leftFront.setPower(-power);
        robot.rightFront.setPower(power);
        robot.leftBack.setPower(-power);
        robot.rightBack.setPower(power);
    }
    public void strafeLeft(double power, int distance) {
        Orientation angles;
        double error;
        double k = 3/360.0;
        double startAngle = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        int leftFrontTarget = robot.leftFront.getCurrentPosition() - (int)(distance * COUNTS_PER_INCH);
        int rightFrontTarget = robot.rightFront.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
        int leftBackTarget = robot.leftBack.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
        int rightBackTarget = robot.rightBack.getCurrentPosition() - (int)(distance * COUNTS_PER_INCH);
        robot.leftFront.setTargetPosition(leftFrontTarget);
        robot.rightFront.setTargetPosition(rightFrontTarget);
        robot.leftBack.setTargetPosition(leftBackTarget);
        robot.rightBack.setTargetPosition(rightBackTarget);

        while (opModeIsActive()
                &&(robot.leftFront.getCurrentPosition() > leftFrontTarget && robot.rightFront.getCurrentPosition() < rightFrontTarget && robot.leftBack.getCurrentPosition() < leftBackTarget && robot.rightBack.getCurrentPosition() > rightBackTarget)) {
            angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            //finds the angle given by the imu [-180, 180]
            double angle = angles.firstAngle;
            error = startAngle - angle;
            robot.leftFront.setPower(-(power + (error * k)));
            robot.rightFront.setPower((power + (error * k)));
            robot.leftBack.setPower((power - (error * k)));
            robot.rightBack.setPower(-(power - (error * k)));
            telemetry.addData("error: ",error);
            telemetry.addData("leftfront dest: ", leftFrontTarget);
            telemetry.addData("leftFront pos: ",robot.leftFront.getCurrentPosition());


            telemetry.update();
        }
        stopMotors();
    }

    public void strafeRight(double power, int distance) {
        Orientation angles;
        double error;
        double k = 3/360.0;
        double startAngle = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        int leftFrontTarget = robot.leftFront.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
        int rightFrontTarget = robot.rightFront.getCurrentPosition() - (int)(distance * COUNTS_PER_INCH);
        int leftBackTarget = robot.leftBack.getCurrentPosition() - (int)(distance * COUNTS_PER_INCH);
        int rightBackTarget = robot.rightBack.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
        robot.leftFront.setTargetPosition(leftFrontTarget);
        robot.rightFront.setTargetPosition(rightFrontTarget);
        robot.leftBack.setTargetPosition(leftBackTarget);
        robot.rightBack.setTargetPosition(rightBackTarget);

        while (opModeIsActive()
                &&(robot.leftFront.getCurrentPosition() < leftFrontTarget && robot.rightFront.getCurrentPosition() > rightFrontTarget && robot.leftBack.getCurrentPosition() > leftBackTarget && robot.rightBack.getCurrentPosition() < rightBackTarget)) {
            angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            //finds the angle given by the imu [-180, 180]
            double angle = angles.firstAngle;
            error = startAngle - angle;
            robot.leftFront.setPower((power - (error * k)));
            robot.rightFront.setPower(-(power - (error * k)));
            robot.leftBack.setPower(-(power + (error * k)));
            robot.rightBack.setPower((power + (error * k)));
            telemetry.addData("error: ",error);
            telemetry.addData("leftfront dest: ", leftFrontTarget);
            telemetry.addData("leftFront pos: ",robot.leftFront.getCurrentPosition());


            telemetry.update();
        }
        stopMotors();
    }
}
