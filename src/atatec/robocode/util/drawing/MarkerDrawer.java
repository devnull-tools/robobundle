package atatec.robocode.util.drawing;

import atatec.robocode.calc.Point;

import java.awt.Graphics2D;

/** @author Marcelo Varella Barca Guimarães */
public class MarkerDrawer {

  private final Graphics2D g;

  private Point location;
  private int size = 8;

  public MarkerDrawer(Graphics2D g) {
    this.g = g;
  }

  public void at(Point location) {
    this.location = location;
    draw();
  }

  public MarkerDrawer ofSize(int size) {
    this.size = size;
    return this;
  }

  private void draw() {
    int x = location.toAwtPoint().x;
    int y = location.toAwtPoint().y;
    int lineSize = size / 2;
    g.drawLine(x - lineSize, y - lineSize, x + lineSize, y + lineSize);
    g.drawLine(x - lineSize, y + lineSize, x + lineSize, y - lineSize);
  }

}
