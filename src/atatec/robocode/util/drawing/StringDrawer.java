package atatec.robocode.util.drawing;

import atatec.robocode.calc.Point;

import java.awt.Graphics2D;

/** @author Marcelo Varella Barca Guimarães */
public class StringDrawer {

  private final Graphics2D g;
  private final String text;

  public StringDrawer(Graphics2D g, String text) {
    this.g = g;
    this.text = text;
  }

  public void at(Point location) {
    g.drawString(text, (float) location.x(), (float) location.y());
  }

}
