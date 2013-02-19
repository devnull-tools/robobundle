package atatec.robocode.parts.aiming;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;
import atatec.robocode.parts.AimingSystem;
import atatec.robocode.util.Drawer;

import java.awt.Graphics2D;

import static atatec.robocode.event.Events.PAINT;
import static java.awt.Color.RED;

/** @author Marcelo Varella Barca Guimarães */
public class DirectAimingSystem implements AimingSystem {

  private final Bot bot;
  private Point enemyLocation;

  public DirectAimingSystem(Bot bot) {
    this.bot = bot;
  }

  public void aim() {
    Enemy enemy = bot.radar().lockedTarget();
    if (enemy != null) {
      enemyLocation = enemy.location();
      Angle angle = enemy.bearing()
        .plus(bot.body().heading())
        .minus(bot.gun().heading());
      bot.gun().turn(angle);
    }
  }

  @When(PAINT)
  public void paint(Graphics2D g) {
    if (enemyLocation != null) {
      Drawer drawer = new Drawer(g);
      drawer.draw(RED).cross().at(enemyLocation);
    }
  }

}
