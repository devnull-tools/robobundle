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

package tools.devnull.robobundle.calc;

/** @author Marcelo Guimarães */
public class TemporaryGravityPoint {

  private int duration;
  private int delay = 0;
  private GravityPoint point;

  public TemporaryGravityPoint(GravityPoint point, int duration) {
    this.duration = duration;
    this.point = point;
  }

  public TemporaryGravityPoint delay(int turns) {
    this.delay = turns;
    return this;
  }

  public GravityPoint point() {
    return point;
  }

  public int duration() {
    return duration;
  }

  public boolean delayed() {
    return delay > 0;
  }

  public GravityPoint pull() {
    if (--delay > 0) {
      return new GravityPoint(point(), 0);
    }
    duration--;
    return point;
  }

  public boolean expired() {
    return duration < 0;
  }

  @Override
  public String toString() {
    return String.format("%s | %d", point.toString(), duration);
  }

}
