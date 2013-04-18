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
import atatec.robocode.annotation.When;
import atatec.robocode.calc.Point;
import atatec.robocode.event.Events;
import atatec.robocode.util.Drawer;

import java.awt.Color;
import java.awt.event.MouseEvent;

/** @author Marcelo Guimarães */
public class Buster extends BaseBot {

  private Point p;

  @Override
  protected void configure() {

  }

  @Override
  public void onMouseClicked(MouseEvent e) {
    p = new Point(
      e.getX(),
      e.getY()
    );
    log("Clicked on %s", p);
    body().moveTo(p, 20);
  }

  @When(Events.PAINT)
  public void paint(Drawer drawer) {
    if (p != null) {
      drawer.draw(Color.WHITE).circle().at(p);
      drawer.draw(Color.RED).cross().at(p);
    }
  }

}
