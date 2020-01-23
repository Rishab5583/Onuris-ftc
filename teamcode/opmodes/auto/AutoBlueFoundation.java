package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name="AutoBlueFoundationBridge")
public class AutoBlueFoundation extends AbstractAutoMode
{
    int time = 0;
    int oldTime = 0;
    boolean setOldTime = true;
    boolean setOldTime2 = true;

    private void update() {
        robot.swerveDrive.control(-driveX, driveY, rotate, robot.gyro.getAdjustedHeading());
        robot.swerveDrive.drive();
        robot.swerveDrive.addData(telemetry);
    }

    private double rotationTowardsTarget(double max) {
        double diff = constrainRad(targetHeading)-constrainRad(robot.gyro.getAdjustedHeading());
        if (diff > 0)
            return max;
        else if (diff < 0)
            return -max;
        else
            return 0;
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
            if (currentStage == 1) {  // Move forward towards foundation
                driveX = 0;
                driveY = 1;
                targetHeading = Math.PI;
                rotate = rotationTowardsTarget(0);

                update();

                if (currentOffsetRight() >= 900)
                    nextStage();
            } else if (currentStage == 2) {//move backward pulling foundation
                driveX = 0;
                driveY = 0.5;
                targetHeading = -Math.PI;
                rotate = rotationTowardsTarget(0);

                update();

                if (currentOffsetLeft() <= 800)
                    nextStage();
            } else if(currentStage == 3) {
                if (setOldTime) {
                    oldTime = time;
                    setOldTime = false;
                }
                driveX = 0;
                driveY = 0;
                targetHeading = Math.PI;
                rotate = rotationTowardsTarget(.5);

                update();

                if (time - oldTime >= 5)
                    nextStage();
            } else if (currentStage == 4) {
                if (setOldTime2) {
                    oldTime = time;
                    setOldTime2 = false;
                }
                driveX = 0;
                driveY = 0;
                targetHeading = Math.PI;
                rotate = rotationTowardsTarget(0);

                robot.intake.setLiftUp();

                update();

                if (time - oldTime >= 20)
                    nextStage();
            } else if (currentStage == 5) {
                driveX = .5;
                driveY = 0;
                targetHeading = Math.PI;
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