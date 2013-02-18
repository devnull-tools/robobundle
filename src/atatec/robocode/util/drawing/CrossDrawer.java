package atatec.robocode.util.drawing;

import atatec.robocode.calc.Point;

import java.awt.Graphics2D;

/** @author Marcelo Varella Barca Guimarães */
public class CrossDrawer {

  private final Graphics2D g;

  private Point location;
  private int size = 4;

  public CrossDrawer(Graphics2D g) {
    this.g = g;
  }

  public void at(Point location) {
    this.location = location;
    draw();
  }

  public CrossDrawer ofSize(int size) {
    this.size = size;
    return this;
  }

  private void draw() {
    int x = location.toAwtPoint().x;
    int y = location.toAwtPoint().y;
    g.drawLine(x, y - size, x, y + size);
    g.drawLine(x - size, y, x + size, y);
  }

}
