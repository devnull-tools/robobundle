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

package tools.devnull.robocode;

import tools.devnull.robocode.calc.Point;

/**
 * Interface that defines a field.
 *
 * @author Marcelo Guimarães
 */
public interface Field {

  /**
   * @return the field height
   */
  double height();

  /**
   * @return the field width
   */
  double width();

  /**
   * @return the center of the field
   */
  Point center();

  /**
   * @return the point at the bottom right corner of the field
   */
  Point bottomRight();

  /**
   * @return the point at the top right corner of the field
   */
  Point topRight();

  /**
   * @return the point at the top left corner of the field
   */
  Point topLeft();

  /**
   * @return the point at the bottom left corner of the field
   */
  Point bottomLeft();

  /**
   * Returns the size of the diagonal. This is the maximum distance
   * between two point in the field.
   *
   * @return the size of the field diagonal
   */
  double diagonal();

  /**
   * Checks if the given point is in the field
   *
   * @param p the point to check
   * @return <code>true</code> if the field contains the given point
   */
  boolean isOnField(Point p);

  /**
   * Returns the closest point in the border to the given point.
   * Usefull when check for an incoming wall.
   *
   * @param p
   * @return
   */
  Point closestBorderPoint(Point p);

  /**
   * Returns the closest point to the given one that
   * belongs to the field. If the given point also
   * belongs to the field, returns it.
   *
   * @param p the point to check
   * @return the closest point to <code>p</code> that
   *         belongs to the field
   */
  Point normalize(Point p);

}
