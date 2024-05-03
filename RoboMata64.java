package mata64;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class RoboMata64  extends AdvancedRobot {

        private final int SECURITY_WALL_DISNTANCE = 50;
        private final int SECURITY_ENEMY_DISNTANCE = 100;
        private double enemyPreviousEnergy = 100;

        public void run() {
            while(true) {
                setAdjustRadarForGunTurn(true);
                setAdjustGunForRobotTurn(true);
                turnRadarRightRadians(Double.POSITIVE_INFINITY);
            }
        }

        public void onScannedRobot(ScannedRobotEvent e) {
            checkAndChangeDirectionIfNearWall();
            avoidColisionsWithEnemies(e);
            lockAndControlShot(e);
            changePositionForExitTargetByEnemy(e);
            lockAndControlShot(e);
        }


        public void onHitByBullet(HitByBulletEvent e) {
            back(10);
        }

        public void onHitWall(HitWallEvent e) {
            avoidOnWall(e);
        }

        // Customs Functions
        public void changePositionForExitTargetByEnemy(ScannedRobotEvent e){
            double changeRobotEnergy = enemyPreviousEnergy - e.getEnergy();

            double angle = 90;

            if (changeRobotEnergy > 0 && changeRobotEnergy <= 3) {

                double changeDirection = Math.random() < 0.5 ? - angle : angle;
                setTurnRight(changeDirection);
                setAhead((Math.random() * 200) + 100);
            }

            enemyPreviousEnergy = e.getEnergy();
        }

        public void avoidColisionsWithEnemies(ScannedRobotEvent e) {
            if (e.getDistance() < SECURITY_ENEMY_DISNTANCE) {
                double angleAdjust = Math.random() > 0.5 ? 90 : -90;
                setTurnRight(e.getBearing() + angleAdjust);
                back(100);
            }

        }

        public void checkAndChangeDirectionIfNearWall() {
            double positionX = getX();
            double positionY = getY();

            double fieldWidth = getBattleFieldWidth();
            double fieldHeight = getBattleFieldHeight();

            if (positionX <= SECURITY_WALL_DISNTANCE ||
                    positionY <= SECURITY_WALL_DISNTANCE ||
                    positionX >= fieldWidth - SECURITY_WALL_DISNTANCE ||
                    positionY >= fieldHeight - SECURITY_WALL_DISNTANCE) {

                setTurnRight(90);
                setAhead(100);
            }
        }

        public void lockAndControlShot (ScannedRobotEvent e) {
            double firePower = Math.min(400 / e.getDistance(), 3);
            double bulletSpeed = 20 - firePower * 3;

            double absoluteBearing = getHeadingRadians() + e.getBearingRadians();

            double enemyHeading = e.getHeadingRadians();
            double enemyVelocity = e.getVelocity();
            double time = e.getDistance() / bulletSpeed;

            double futureX = e.getDistance() * Math.sin(absoluteBearing) + getX();
            double futureY = e.getDistance() * Math.cos(absoluteBearing) + getY();
            double predictedX = futureX + Math.sin(enemyHeading) * enemyVelocity * time;
            double predictedY = futureY + Math.cos(enemyHeading) * enemyVelocity * time;

            double deltaX = predictedX - getX();
            double deltaY = predictedY - getY();
            double predictedBearing = Math.atan2(deltaX, deltaY);

            setTurnGunRightRadians(Utils.normalRelativeAngle(predictedBearing - getGunHeadingRadians()));

            double radarTurn = Utils.normalRelativeAngle(absoluteBearing - getRadarHeadingRadians());
            setTurnRadarRightRadians(2 * radarTurn);

            if (getGunHeat() == 0) {
                fire(firePower);
            }
        }

        public void avoidOnWall(HitWallEvent e) {
            double bearing = e.getBearing();

            turnRight(-bearing);
            ahead(100);
        }
    }