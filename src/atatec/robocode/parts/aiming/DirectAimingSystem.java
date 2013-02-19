package atatec.robocode.parts.aiming;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;
import atatec.robocode.parts.AimingSystem;
import atatec.robocode.util.Drawer;

import static atatec.robocode.event.Events.DRAW;
import static java.awt.Color.RED;

/** @author Marcelo Varella Barca Guimarães */
public class DirectAimingSystem implements AimingSystem {

  private final Bot bot;
  private Point enemyLocation;

  public DirectAimingSystem(Bot bot) {
    this.bot = bot;
  }

  public void execute() {
    Enemy enemy = bot.radar().lockedTarget();
    if (enemy != null) {
      enemyLocation = enemy.location();
      Angle angle = enemy.bearing()
        .plus(bot.body().heading())
        .minus(bot.gun().heading());
      bot.gun().turn(angle);
    }
  }

  @When(DRAW)
  public void paint(Drawer drawer) {
    if (enemyLocation != null) {
      drawer.draw(RED).cross().at(enemyLocation);
    }
  }

}
