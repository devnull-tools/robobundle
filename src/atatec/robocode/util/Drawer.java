package atatec.robocode.util;

import atatec.robocode.util.drawing.CircleDrawer;
import atatec.robocode.util.drawing.CrossDrawer;
import atatec.robocode.util.drawing.MarkerDrawer;
import atatec.robocode.util.drawing.StringDrawer;

import java.awt.Color;
import java.awt.Graphics2D;

/** @author Marcelo Varella Barca Guimarães */
public class Drawer {

  private final Graphics2D g;

  public Drawer(Graphics2D g) {
    this.g = g;
  }

  public Graphics2D graphics() {
    return g;
  }

  public ShapeSelector draw(Color color) {
    g.setColor(color);
    return new ShapeSelector();
  }

  public class ShapeSelector {

    public CircleDrawer circle() {
      return new CircleDrawer(g);
    }

    public CrossDrawer cross() {
      return new CrossDrawer(g);
    }

    public MarkerDrawer marker() {
      return new MarkerDrawer(g);
    }

    public StringDrawer string(Object message, Object... args) {
      return new StringDrawer(g, String.format(message.toString(), args));
    }

  }

}
