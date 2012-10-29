package atatec.robocode.parts.movement;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.Angle;
import atatec.robocode.event.Events;
import atatec.robocode.parts.MovingSystem;
import robocode.HitRobotEvent;

import static java.lang.Math.random;

/** @author Marcelo Varella Barca Guimarães */
public class FleeEnemyMovingSystem implements MovingSystem {

  private static final double MOVEMENT_LENGTH = -100;

  private final Bot bot;

  private double distance;
  private Angle angle;

  public FleeEnemyMovingSystem(Bot bot) {
    this.bot = bot;
  }

  public void move() {
    Enemy target = bot.radar().lockedTarget();
    distance = MOVEMENT_LENGTH * random();
    angle = target.bearing().plus(Angle.inDegrees(22.5));
    bot.body().moveAndTurn(distance, angle);
  }

  @When(Events.HIT_ROBOT)
  public void onHitRobot(HitRobotEvent event) {
    double ahead = MOVEMENT_LENGTH * random();
    bot.body().move(-ahead);
  }

}
