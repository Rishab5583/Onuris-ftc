package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="AutoRedParkBridge")
public class AutoRed2 extends AbstractAutoMode
{

    private void update() {
        robot.swerveDrive.control(driveX, driveY, rotate, robot.gyro.getAdjustedHeading());
        robot.swerveDrive.drive();
        robot.swerveDrive.addData(telemetry);
    }

    private double rotationTowardsTarget(double max) {
        double diff = constrainRad(targetHeading)-constrainRad(robot.gyro.getAdjustedHeading());
        return Range.clip(diff,-max, max);
    }

    @Override
    public void loop() {
        super.loop();

        currentRightPos = robot.swerveDrive.right.getMotorPosition();
        currentLeftPos = robot.swerveDrive.left.getMotorPosition();

        telemetry.addData("STAGE", "Stage %s, rightPosition: (%f), leftPosition: (%f)", currentStage, currentRightPos, currentLeftPos);

        driveX = 0;
        driveY = 0;
        rotate = 0;

        if (waiting > 0) {
            waiting -= 1;
            update();
        } else {
            if (currentStage == 1) {
                driveX = 0;
                driveY = .5;
                targetHeading = -Math.PI;
                rotate = rotationTowardsTarget(0);

                update();

                if (currentOffsetRight() >= 500) {
                    nextStage();
                }
            } else if (currentStage == 2) {  // Move backward towards foundation
                driveX = .5;
                driveY = 0;
                targetHeading = -Math.PI;
                rotate = rotationTowardsTarget(0);

                update();

                if (currentOffsetRight() >= 1000) {
                    nextStage();
                }
            } else {
                update();
            }
        }

        telemetry.update();
    }

}