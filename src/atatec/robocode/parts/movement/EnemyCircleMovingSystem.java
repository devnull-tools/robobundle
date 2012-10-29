package atatec.robocode.parts.movement;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.calc.Angle;
import atatec.robocode.parts.MovingSystem;

import static java.lang.Math.cos;
import static java.lang.Math.random;

/** @author Marcelo Varella Barca Guimarães */
public class EnemyCircleMovingSystem implements MovingSystem {

  private static final double MOVEMENT_LENGTH = 75;

  private final Bot bot;

  public EnemyCircleMovingSystem(Bot bot) {
    this.bot = bot;
  }

  public void move() {
    double ahead = cos(bot.radar().time() >> 4) * MOVEMENT_LENGTH * random();
    Enemy enemy = bot.radar().lockedTarget();
    if (enemy != null) {
      Angle angle = enemy.bearing().plus(Angle.PI_OVER_TWO);
      bot.log("Turning %s", angle);
      bot.log("Body is at %s", bot.body().heading());
      bot.body().moveAndTurn(ahead, angle);
    }
  }

}
