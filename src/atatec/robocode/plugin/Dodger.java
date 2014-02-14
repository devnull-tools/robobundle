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
import atatec.robocode.Enemy;
import atatec.robocode.annotation.When;
import atatec.robocode.event.EnemyFireEvent;
import atatec.robocode.event.Events;
import robocode.BulletHitEvent;
import robocode.Rules;

import java.util.HashMap;
import java.util.Map;

/** @author Marcelo Guimarães */
public class Dodger {

  private final Bot bot;

  private final Map<String, Enemy> energyMap;

  public Dodger(Bot bot) {
    this.bot = bot;
    this.energyMap = new HashMap<String, Enemy>();
  }

  @When(Events.ENEMY_SCANNED)
  public void onEnemyScanned(Enemy enemy) {
    String name = enemy.name();
    if (energyMap.containsKey(name)) {
      checkFire(enemy, energyMap.get(name));
    }
    energyMap.put(name, enemy);
  }

  private void checkFire(Enemy enemy, Enemy lastSeen) {
    double bulletPower = lastSeen.energy() - enemy.energy();
    //assumes a bullet fired based on the energy differences
    if (bulletPower >= Rules.MIN_BULLET_POWER && bulletPower <= Rules.MAX_BULLET_POWER) {
      bot.dispatch(
        Events.ENEMY_FIRE, new EnemyFireEvent(enemy, bulletPower)
      );
    }
  }

  @When(Events.BULLET_HIT)
  public void onBulletHit(BulletHitEvent event) {
    // removes the last target registry because if the bullet hits the target, its energy
    // will drop a bit and may cause a wrong interpretation of a bullet being fired
    energyMap.remove(event.getName());
  }

}
