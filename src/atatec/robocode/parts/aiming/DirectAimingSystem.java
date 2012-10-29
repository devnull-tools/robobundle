package atatec.robocode.parts.aiming;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.calc.Angle;
import atatec.robocode.parts.AimingSystem;

/** @author Marcelo Varella Barca Guimarães */
public class DirectAimingSystem implements AimingSystem {

  private final Bot bot;

  public DirectAimingSystem(Bot bot) {
    this.bot = bot;
  }

  public void aim() {
    Enemy enemy = bot.radar().lockedTarget();
    if (enemy != null) {
      Angle angle = angleToAim(enemy.bearing());
      bot.gun().turn(angle);
    }
  }

  public Angle angleToAim(Angle bearing) {
    return bearing.plus(bot.body().heading()).minus(bot.gun().heading());
  }

}
