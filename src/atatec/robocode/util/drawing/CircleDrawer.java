package atatec.robocode.util.drawing;

import atatec.robocode.calc.Point;

import java.awt.Graphics2D;

/** @author Marcelo Varella Barca Guimarães */
public class CircleDrawer {

  private final Graphics2D g;

  private Point location;
  private int size = 30;

  public CircleDrawer(Graphics2D g) {
    this.g = g;
  }

  public void at(Point location) {
    this.location = location;
    draw();
  }

  public CircleDrawer ofSize(int size) {
    this.size = size;
    return this;
  }

  private void draw() {
    int x = (int) location.x() - (size / 2);
    int y = (int) location.y() - (size / 2);
    g.drawOval(x, y, size, size);
  }

}
