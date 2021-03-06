package org.firstinspires.ftc.teamcode.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.teamcode.HardwareMap.HardwareMap_example;

@TeleOp
public class HolonomicDrive_example extends HardwareMap_example {

    @Override
    public void runOpMode() throws InterruptedException {
        double rStickX;
        double rStickY;
        double lStickX;
        double targetAngle;
        double mag1;
        double mag2;
        double rotationPower;
        double maxPower;
        double scaleDown;

        rStickX = gamepad1.right_stick_x;
        rStickY = -gamepad1.right_stick_y;
        lStickX = gamepad1.left_stick_x;

        targetAngle = (Math.atan2(rStickY,rStickX));

        rotationPower = -lStickX;
        mag1 = Math.sqrt(Math.pow(rStickX,2) + Math.pow(rStickY,2)) * (Math.sin(targetAngle + Math.PI / 4));
        mag2 = Math.sqrt(Math.pow(rStickX,2) + Math.pow(rStickY,2)) * (Math.sin(targetAngle - Math.PI / 4));

        maxPower = Math.max(Math.abs(mag1) +  Math.abs(rotationPower) , Math.abs(mag2) +  Math.abs(rotationPower)) + 0.15;
        scaleDown = 1.0;

        if (maxPower > 1)
            scaleDown = 1.0 / maxPower;


        leftBack.setPower((mag1 - rotationPower) * scaleDown);
        rightBack.setPower((mag2 + rotationPower) * scaleDown);
        //leftFront.setPower((mag2 - rotationPower) * scaleDown);
        //rightFront.setPower((mag1 + rotationPower) * scaleDown);

    }
}
