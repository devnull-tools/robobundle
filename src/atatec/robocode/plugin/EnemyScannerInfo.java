package atatec.robocode.plugin;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.Point;
import atatec.robocode.util.Drawer;

import static atatec.robocode.event.Events.DRAW;
import static java.awt.Color.GREEN;
import static java.awt.Color.ORANGE;
import static java.awt.Color.YELLOW;

/** @author Marcelo Varella Barca Guimarães */
public class EnemyScannerInfo {

  private final Bot bot;

  private boolean showAttributes;

  public EnemyScannerInfo(Bot bot) {
    this.bot = bot;
  }

  public EnemyScannerInfo showAttributes() {
    this.showAttributes = true;
    return this;
  }

  @When(DRAW)
  public void paint(Drawer drawer) {
    for (Enemy enemy : bot.radar().knownEnemies()) {
      paintEnemy(drawer, enemy);
    }
  }

  private void paintEnemy(Drawer drawer, Enemy enemy) {
    Point location = enemy.location();
    if(enemy == bot.radar().lockedTarget()) {
      drawer.draw(GREEN).circle().at(location);
      drawer.draw(ORANGE).string(enemy.name()).at(bot.location().down(18));
    } else {
      drawer.draw(YELLOW).circle().at(location);
    }
    if(showAttributes) {
      drawer.draw(GREEN).string("%.3f", enemy.distance()).at(enemy.location());
      drawer.draw(GREEN).string("%.3f", enemy.velocity()).at(enemy.location().down(15));
      drawer.draw(GREEN).string("%s", enemy.location()).at(enemy.location().down(30));
    }
  }

}
