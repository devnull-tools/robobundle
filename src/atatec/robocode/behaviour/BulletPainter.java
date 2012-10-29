package atatec.robocode.behaviour;

import atatec.robocode.annotation.When;
import atatec.robocode.event.BulletFiredEvent;
import atatec.robocode.event.Events;
import robocode.Robot;
import robocode.Rules;

import java.awt.Color;

import static robocode.Rules.MAX_BULLET_POWER;
import static robocode.Rules.MIN_BULLET_POWER;

/** @author Marcelo Varella Barca Guimarães */
public class BulletPainter {

  private static final double MEDIUM_BULLET_POWER = (MAX_BULLET_POWER + MIN_BULLET_POWER) / 2;

  private final Robot bot;

  private Color strongColor = Color.RED;
  private Color mediumColor = Color.BLUE;
  private Color weakColor = Color.ORANGE;

  private Color selected;

  public BulletPainter(Robot bot) {
    this.bot = bot;
  }

  public BulletPainter use(Color color) {
    this.selected = color;
    return this;
  }

  public BulletPainter forStrong() {
    this.strongColor = selected;
    return this;
  }
  public BulletPainter forMedium() {
    this.mediumColor = selected;
    return this;
  }

  public BulletPainter forWeak() {
    this.weakColor = selected;
    return this;
  }

  @When(Events.BULLET_FIRED)
  public void onBulletFired(BulletFiredEvent event) {
    double power = event.bullet().getPower();
    if (power >= Rules.MAX_BULLET_POWER) {
      bot.setBulletColor(strongColor);
    } else if (power >= MEDIUM_BULLET_POWER) {
      bot.setBulletColor(mediumColor);
    } else {
      bot.setBulletColor(weakColor);
    }
  }

}
