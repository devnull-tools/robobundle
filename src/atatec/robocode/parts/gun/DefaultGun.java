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

package atatec.robocode.parts.gun;

import atatec.robocode.BaseBot;
import atatec.robocode.ConditionalCommand;
import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;
import atatec.robocode.condition.Condition;
import atatec.robocode.condition.RadarConditions;
import atatec.robocode.event.Events;
import atatec.robocode.exception.SystemException;
import atatec.robocode.parts.AimingSystem;
import atatec.robocode.parts.BasePart;
import atatec.robocode.parts.DefaultConditionalCommand;
import atatec.robocode.parts.FiringSystem;
import atatec.robocode.parts.Gun;
import robocode.util.Utils;

import java.awt.Color;

/** @author Marcelo Guimarães */
public class DefaultGun extends BasePart implements Gun {

  private final BaseBot bot;

  private final DefaultConditionalCommand<AimingSystem> aimingSystem;

  private final DefaultConditionalCommand<FiringSystem> firingSystem;

  public DefaultGun(BaseBot bot) {
    this.bot = bot;
    this.aimingSystem = new DefaultConditionalCommand<AimingSystem>(bot);
    this.firingSystem = new DefaultConditionalCommand<FiringSystem>(bot);
  }

  @Override
  public void setColor(Color color) {
    bot.setGunColor(color);
  }

  @Override
  public void setBulletColor(Color color) {
    bot.setBulletColor(color);
  }

  public Gun aim() {
    try {
      aimingSystem.execute();
      bot.events().send(Events.GUN_AIMED);
    } catch (SystemException e) {
      bot.log(e);
    }
    return this;
  }

  @Override
  public void fireIfTargetSet() {
    fireIf(new RadarConditions(bot.radar()).hasTargetSet());
  }

  @Override
  public void fireIf(Condition condition) {
    if (condition.evaluate()) {
      fire();
    }
  }

  @Override
  public void fire(double power) {
    bot.fire(power);
  }

  public void fire() {
    firingSystem.execute();
  }

  public double power() {
    FiringSystem activated = firingSystem.activated();
    return activated != null ? activated.firePower() : 0;
  }

  @Override
  public Angle heading() {
    return new Angle(bot.getGunHeadingRadians());
  }

  @Override
  public Angle turnRemaining() {
    return new Angle(bot.getGunTurnRemainingRadians());
  }

  @Override
  public double heat() {
    return bot.getGunHeat();
  }

  @Override
  public double coolingRate() {
    return bot.getGunCoolingRate();
  }

  public void turnLeft(Angle angle) {
    if (angle.radians() < 0) {
      turnRight(angle.inverse());
    } else if (angle.radians() > Math.PI) {
      turnRight(Angle.TWO_PI.minus(angle));
    } else if (angle.radians() >= 1E-5) {
      bot.log("Turning gun %s left from %s", angle, heading());
      bot.setTurnGunLeftRadians(angle.radians());
    }
  }

  public void turnRight(Angle angle) {
    if (angle.radians() < 0) {
      turnLeft(angle.inverse());
    } else if (angle.radians() > Math.PI) {
      turnLeft(Angle.TWO_PI.minus(angle));
    } else if (angle.radians() >= 1E-5) {
      bot.log("Turning gun %s right from %s", angle, heading());
      bot.setTurnGunRightRadians(angle.radians());
    }
  }


  public ConditionalCommand<AimingSystem> forAiming() {
    return aimingSystem;
  }

  public ConditionalCommand<FiringSystem> forFiring() {
    return firingSystem;
  }

  @Override
  public Gun aimTo(Point point) {
    bot.log("Aiming gun to %s", point);
    Point diff = point.minus(bot.location());
    double theta = Utils.normalAbsoluteAngle(Math.atan2(diff.x(), diff.y()));
    Angle angle = new Angle(Utils.normalRelativeAngle(theta - bot.gun().heading().radians()));
    turn(angle);
    return this;
  }

}
