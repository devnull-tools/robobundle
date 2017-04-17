/************************************************************************************
 * The MIT License                                                                  *
 *                                                                                  *
 * Copyright (c) 2013 Marcelo Guimarães <ataxexe at gmail dot com>                  *
 * -------------------------------------------------------------------------------- *
 * Permission  is hereby granted, free of charge, to any person obtaining a copy of *
 * this  software  and  associated documentation files (the "Software"), to deal in *
 * the  Software  without  restriction,  including without limitation the rights to *
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of *
 * the  Software, and to permit persons to whom the Software is furnished to do so, *
 * subject to the following conditions:                                             *
 *                                                                                  *
 * The  above  copyright notice and this permission notice shall be included in all *
 * copies or substantial portions of the Software.                                  *
 *                            --------------------------                            *
 * THE  SOFTWARE  IS  PROVIDED  "AS  IS",  WITHOUT WARRANTY OF ANY KIND, EXPRESS OR *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS *
 * FOR  A  PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR *
 * COPYRIGHT  HOLDERS  BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER *
 * IN  AN  ACTION  OF  CONTRACT,  TORT  OR  OTHERWISE,  ARISING  FROM, OUT OF OR IN *
 * CONNECTION  WITH  THE  SOFTWARE  OR  THE  USE OR OTHER DEALINGS IN THE SOFTWARE. *
 ************************************************************************************/

package tools.devnull.robobundle.parts.firing;

import tools.devnull.robobundle.Bot;
import tools.devnull.robobundle.Enemy;
import tools.devnull.robobundle.parts.FiringSystem;
import robocode.Rules;

import static robocode.Rules.MAX_BULLET_POWER;
import static robocode.Rules.MIN_BULLET_POWER;

/** @author Marcelo Guimarães */
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

  public double firePower() {
    return bestPower(bestPowerBasedOnBot(), bestPowerBasedOnEnemy());
  }

  public void execute() {
    bot.gun().fire(firePower());
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
    Enemy enemy = bot.radar().target();
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
