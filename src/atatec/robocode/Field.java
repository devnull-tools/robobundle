package atatec.robocode;

import atatec.robocode.calc.Point;

/** @author Marcelo Varella Barca Guimarães */
public interface Field {
  double height();

  double width();

  Point center();

  Point downRight();

  Point upRight();

  Point upLeft();

  Point downLeft();

  double diagonal();

  boolean contains(Point p);
}
