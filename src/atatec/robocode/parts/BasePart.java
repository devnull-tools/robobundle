package atatec.robocode.parts;

import atatec.robocode.calc.Angle;

/** @author Marcelo Varella Barca Guimarães */
public abstract class BasePart implements Part {

  private boolean on = true;

  public void on() {
    on = true;
  }

  public void off() {
    on = false;
  }

  public boolean isOn() {
    return on;
  }

  public boolean isOff() {
    return !on;
  }

  @Override
  public void turn(Angle angle) {
    if (angle.radians() < 0) {
      turnLeft(angle.inverse());
    } else {
      turnRight(angle);
    }
  }

  protected abstract void turnRight(Angle angle);

  protected abstract void turnLeft(Angle angle);

}
