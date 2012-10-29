package atatec.robocode;

import atatec.robocode.calc.BotMath;
import atatec.robocode.calc.Point;
import robocode.Robot;

/** @author Marcelo Varella Barca Guimarães */
public class BattleField implements Field {

  private final double height;

  private final double width;

  private Robot reference;

  public BattleField(Robot robot) {
    this.reference = robot;
    height = robot.getBattleFieldHeight();
    width = robot.getBattleFieldWidth();
  }

  @Override
  public double height() {
    return height;
  }

  @Override
  public double width() {
    return width;
  }

  @Override
  public Point center() {
    return new Point(width / 2, height / 2);
  }

  @Override
  public Point downRight() {
    return new Point(width, 0);
  }

  @Override
  public Point upRight() {
    return new Point(width, height);
  }

  @Override
  public Point upLeft() {
    return new Point(0, height);
  }

  @Override
  public Point downLeft() {
    return new Point(0, 0);
  }

  @Override
  public double diagonal() {
    return downLeft().bearingTo(upRight()).distance();
  }

  @Override
  public boolean contains(Point p) {
    return (p.x() > 0 && p.y() > 0) &&
      BotMath.compare(p.x(), width) <= 0 && BotMath.compare(p.y(), height) <= 0;
  }

}
