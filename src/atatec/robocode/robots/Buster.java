package atatec.robocode.robots;

import atatec.robocode.BaseBot;
import atatec.robocode.parts.movement.GravitationalMovingSystem;

/** @author Marcelo Varella Barca Guimarães */
public class Buster extends BaseBot {

  private GravitationalMovingSystem movingSystem = new GravitationalMovingSystem(this);

  @Override
  protected void configure() {
    independentMovement();

    body().movingBehaviour()
      .use(movingSystem);
  }

  @Override
  protected void battle() {
    movingSystem.add(radar().battleField().center().gravitational().withValue(300));
    while(true) {
      body().move();
      execute();
    }
  }

}
