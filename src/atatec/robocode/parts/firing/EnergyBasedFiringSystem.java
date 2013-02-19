package atatec.robocode.parts.firing;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.parts.FiringSystem;
import robocode.Rules;

import static robocode.Rules.MAX_BULLET_POWER;
import static robocode.Rules.MIN_BULLET_POWER;

/** @author Marcelo Varella Barca Guimarães */
public class EnergyBasedFiringSystem implements FiringSystem {

  private static final double MEDIUM_BULLET_POWER = (MAX_BULLET_POWER + MIN_BULLET_POWER) / 2;

  private final Bot bot;

  private double energyToFireMax = 75;
  private double energyToFireMin = 20;

  public EnergyBasedFiringSystem(Bot bot) {
    this.bot = bot;
  }

  public EnergyBasedFiringSystem fireMaxAt(double energy) {
    energyToFireMax = energy;
    return this;
  }

  public EnergyBasedFiringSystem fireMinAt(double energy) {
    energyToFireMin = energy;
    return this;
  }

  public double power() {
    return bestPower(bestPowerBasedOnBot(), bestPowerBasedOnEnemy());
  }

  public void execute() {
    bot.gun().fire(power());
  }

  private double bestPowerBasedOnBot() {
    double energy = bot.body().energy();
    if (energy >= energyToFireMax) {
      return MAX_BULLET_POWER;
    } else if (energy < energyToFireMin) {
      return (MEDIUM_BULLET_POWER + MIN_BULLET_POWER) / 2;
    } else {
      return MEDIUM_BULLET_POWER;
    }
  }

  private double bestPowerBasedOnEnemy() {
    Enemy enemy = bot.radar().lockedTarget();
    if(enemy == null) {
      return Double.MAX_VALUE;
    }
    double energy = enemy.energy();
    if (energy > 75) {
      return Rules.MAX_BULLET_POWER;
    } else if (energy < 30) {
      return MEDIUM_BULLET_POWER * 1.5;
    } else {
      return MEDIUM_BULLET_POWER;
    }
  }

  private double bestPower(double basedOnBot, double basedOnEnemy) {
    return Math.min(basedOnBot, basedOnEnemy);
  }

}
