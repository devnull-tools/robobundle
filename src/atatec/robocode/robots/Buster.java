package atatec.robocode.robots;

import atatec.robocode.AbstractBot;
import atatec.robocode.calc.Angle;

/** @author Marcelo Varella Barca Guimarães */
public class Buster extends AbstractBot {

  @Override
  protected void configure() {
    independentMovement();
  }

  @Override
  protected void battle() {
    body().moveAndTurn(300, Angle.PI);
  }

}
