package atatec.robocode;

/** @author Marcelo Varella Barca Guimarães */
public abstract class BaseEnemy implements Enemy {

  @Override
  public boolean isMoving() {
    return velocity() > 0;
  }

  @Override
  public boolean isStopped() {
    return !isMoving();
  }

  @Override
  public double distance() {
    return position().distance();
  }

}
