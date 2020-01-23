package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="AutoRedFoundationWall")
public class AutoRedFoundation2 extends AbstractAutoMode
{
    boolean setOldTime = true;
    boolean setOldTime2 = true;
    int oldTime = 0;
int time = 0;


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
            if (currentStage == 1) {  // Move backward towards foundation
                driveX = 0;
                driveY = 1;
                targetHeading = -Math.PI;
                rotate = rotationTowardsTarget(0);

                update();

                if (currentOffsetRight() >= 900)
                    nextStage();
            } else if (currentStage == 2) {
                driveX = 0;
                driveY = -0.5;
                targetHeading = Math.PI;
                rotate = rotationTowardsTarget(0);

                update();

                if (currentOffsetLeft() <= 1100)
                    nextStage();
//            } else if (currentStage == 3) {
//                if (setOldTime) {
//                    oldTime = time;
//                    setOldTime = false;
//                }
//                driveX = 0;
//                driveY = 0;
//                targetHeading = -Math.PI;
//                rotate = rotationTowardsTarget(-.5);
//
//                update();
//
//                if (time - oldTime >= 5)
//                    nextStage();
            } else if (currentStage == 3) {
                if (setOldTime2) {
                    oldTime = time;
                    setOldTime2 = false;
                }
                driveX = 0;
                driveY = 0;
                targetHeading = -Math.PI;
                rotate = rotationTowardsTarget(0);

                robot.intake.setLiftUp();

                update();

                if (time - oldTime >= 20)
                    nextStage();
            } else if (currentStage == 4) {
                driveX = 0.5;
                driveY = 0;
                targetHeading = -Math.PI;
                rotate = rotationTowardsTarget(0);

                update();

                if (currentOffsetRight() >= 1200)
                    nextStage();
            } else {
                update();
            }
        }

        telemetry.update();
        time ++;
    }

}