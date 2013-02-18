package atatec.robocode.util.drawing;

import atatec.robocode.calc.Point;

import java.awt.Graphics2D;

/** @author Marcelo Varella Barca Guimarães */
public class LineDrawer {

  private final Graphics2D g;

  private Point origin;
  private Point dest;

  public LineDrawer(Graphics2D g) {
    this.g = g;
  }

  public LineDrawer from(Point origin) {
    this.origin = origin;
    return this;
  }

  public void to(Point dest) {
    this.dest = dest;
    draw();
  }

  private void draw() {
    g.drawLine(
      origin.toAwtPoint().x, origin.toAwtPoint().y,
      dest.toAwtPoint().x, dest.toAwtPoint().y
    );
  }

}
