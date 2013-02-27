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

package atatec.robocode.plugin;

import atatec.robocode.Bot;
import atatec.robocode.annotation.When;
import atatec.robocode.event.BulletFiredEvent;
import atatec.robocode.event.Events;
import robocode.Rules;

import java.awt.Color;

import static robocode.Rules.MAX_BULLET_POWER;
import static robocode.Rules.MIN_BULLET_POWER;

/** @author Marcelo Guimarães */
public class BulletPaint {

  private static final double MEDIUM_BULLET_POWER = (MAX_BULLET_POWER + MIN_BULLET_POWER) / 2;

  private final Bot bot;

  private Color strongColor = Color.RED;
  private Color mediumColor = Color.BLUE;
  private Color weakColor = Color.ORANGE;

  private Color selected;

  public BulletPaint(Bot bot) {
    this.bot = bot;
  }

  public BulletPaint use(Color color) {
    this.selected = color;
    return this;
  }

  public BulletPaint forStrong() {
    this.strongColor = selected;
    return this;
  }
  public BulletPaint forMedium() {
    this.mediumColor = selected;
    return this;
  }

  public BulletPaint forWeak() {
    this.weakColor = selected;
    return this;
  }

  @When(Events.BULLET_FIRED)
  public void onBulletFired(BulletFiredEvent event) {
    double power = event.bullet().getPower();
    if (power >= Rules.MAX_BULLET_POWER) {
      bot.gun().setBulletColor(strongColor);
    } else if (power >= MEDIUM_BULLET_POWER) {
      bot.gun().setBulletColor(mediumColor);
    } else {
      bot.gun().setBulletColor(weakColor);
    }
  }

}
