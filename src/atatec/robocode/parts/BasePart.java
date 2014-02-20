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

package atatec.robocode.parts;

import atatec.robocode.BaseBot;
import atatec.robocode.Localizable;
import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;
import atatec.robocode.calc.ViewPoint;

/**
 * @author Marcelo Guimarães
 */
public abstract class BasePart implements Part {

  protected final BaseBot bot;

  protected BasePart(BaseBot bot) {
    this.bot = bot;
  }

  public abstract void turn(Angle angle);

  @Override
  public boolean isAtLeft(Localizable target) {
    ViewPoint viewPoint = new ViewPoint(heading(), bot.location());
    Point viewedPoint = viewPoint.transform(target.location());
    return viewedPoint.x() < 0;
  }

  @Override
  public boolean isAtRight(Localizable target) {
    return !isAtLeft(target);
  }

  @Override
  public boolean isInFront(Localizable target) {
    return !isInBack(target);
  }

  @Override
  public boolean isInBack(Localizable target) {
    ViewPoint viewPoint = new ViewPoint(heading(), bot.location());
    Point viewedPoint = viewPoint.transform(target.location());
    return viewedPoint.y() < 0;
  }

}
