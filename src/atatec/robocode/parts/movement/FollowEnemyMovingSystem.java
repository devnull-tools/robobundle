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
public class FollowEnemyMovingSystem implements MovingSystem {

  private static final double MOVEMENT_LENGTH = 100;

  private final Bot bot;

  public FollowEnemyMovingSystem(Bot bot) {
    this.bot = bot;
  }

  public void execute() {
    double ahead = MOVEMENT_LENGTH * random();
    Enemy target = bot.radar().lockedTarget();
    if (target != null) {
      bot.body().moveAndTurn(ahead, target.bearing().plus(Angle.inDegrees(22.5)));
    }
  }

  @When(Events.HIT_ROBOT)
  public void onHitRobot(HitRobotEvent event) {
    double ahead = MOVEMENT_LENGTH * random();
    bot.body().move(-ahead);
  }

}
