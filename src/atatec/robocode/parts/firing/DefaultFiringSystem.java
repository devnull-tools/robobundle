package atatec.robocode.parts.firing;

import atatec.robocode.Bot;
import atatec.robocode.parts.FiringSystem;

/** @author Marcelo Varella Barca Guimarães */
public class DefaultFiringSystem implements FiringSystem {

  private final Bot bot;

  private final double power;

  public DefaultFiringSystem(Bot bot, double power) {
    this.bot = bot;
    this.power = power;
  }

  public DefaultFiringSystem(Bot bot) {
    this(bot, 1);
  }

  public double power() {
    return power;
  }

  @Override
  public void fire() {
    bot.gun().fire(power());
  }

}
