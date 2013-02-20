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

package atatec.robocode.calc;

/** @author Marcelo Varella Barca Guimarães */
public class Triangle {

  public enum ID {
    A, B, C
  }

  private final double a;
  private final double b;
  private final double c;

  private final Angle alpha;
  private final Angle beta;
  private final Angle gama;

  public Triangle(double sideA, double sideB, double sideC) {
    this.a = sideA;
    this.b = sideB;
    this.c = sideC;
    //cosine rule
    double cosGama = (Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2 * a * b);
    this.gama = new Angle(Math.acos(cosGama));
    //sine rule
    double sinAlpha = a * gama.sin() / c;
    this.alpha = new Angle(Math.asin(sinAlpha));
    //triangle rule (the sum of the internal angles equals 180 degres
    this.beta = Angle.PI.minus(alpha).minus(gama);
  }

  public double side(ID id) {
    switch (id) {
      case A:
        return a;
      case B:
        return b;
      case C:
        return c;
      default:
        throw new IllegalArgumentException();
    }
  }

  public Angle angle(ID id) {
    switch (id) {
      case A:
        return alpha;
      case B:
        return beta;
      case C:
        return gama;
      default:
        throw new IllegalArgumentException();
    }
  }

}
