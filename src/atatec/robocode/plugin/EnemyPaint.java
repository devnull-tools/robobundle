package atatec.robocode.plugin;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.Point;
import atatec.robocode.util.Drawer;

import java.awt.Graphics2D;

import static atatec.robocode.event.Events.PAINT;
import static java.awt.Color.GREEN;
import static java.awt.Color.YELLOW;

/** @author Marcelo Varella Barca Guimarães */
public class EnemyPaint {

  private final Bot bot;

  public EnemyPaint(Bot bot) {
    this.bot = bot;
  }

  @When(PAINT)
  public void paint(Graphics2D g) {
    for (Enemy enemy : bot.radar().knownEnemies()) {
      paintEnemy(g, enemy);
    }
  }

  private void paintEnemy(Graphics2D g, Enemy enemy) {
    Point location = enemy.location();
    Drawer drawer = new Drawer(g);
    if(enemy == bot.radar().lockedTarget()) {
      drawer.draw(GREEN).circle().at(location);
    } else {
      drawer.draw(YELLOW).circle().at(location);
    }
  }

}
