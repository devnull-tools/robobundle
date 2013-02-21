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

package atatec.robocode.robots;

import atatec.robocode.BaseBot;
import atatec.robocode.calc.Point;
import atatec.robocode.parts.aiming.PredictionAimingSystem;
import atatec.robocode.parts.movement.GravitationalMovingSystem;
import atatec.robocode.parts.scanner.EnemyLockScanningSystem;

import java.awt.event.MouseEvent;

/** @author Marcelo Varella Barca Guimarães */
public class Buster extends BaseBot {

  @Override
  protected void configure() {
    body().forMoving()
      .use(new GravitationalMovingSystem(this));

    gun().forAiming()
      .use(new PredictionAimingSystem(this));

    /*gun().forFiring()
      .use(new EnergyBasedFiringSystem(this));*/

    radar().forScanning()
      .use(new EnemyLockScanningSystem(this));
  }

  protected void onNextTurn() {
    radar().scan();
    body().move();
    gun().aim().fireIfTargetLocked();
  }

  long time;

  @Override
  public void onMousePressed(MouseEvent e) {
    time = e.getWhen();
  }

  @Override
  public void onMouseReleased(MouseEvent e) {
    long power = e.getWhen() - time;
    if (e.getButton() == MouseEvent.BUTTON1) {
      events().send(GravitationalMovingSystem.ADD_GRAVITY_POINT,
        new Point(e.getPoint().getX(), e.getPoint().getY())
          .gravitational()
          .withValue(power)
          .during(50)
      );
    } else {
      events().send(GravitationalMovingSystem.ADD_GRAVITY_POINT,
        new Point(e.getPoint().getX(), e.getPoint().getY())
          .antiGravitational()
          .withValue(power)
      );
    }
  }

}
