package atatec.robocode.robots;

import atatec.robocode.BaseBot;
import atatec.robocode.parts.aiming.DirectAimingSystem;
import atatec.robocode.parts.firing.DefaultFiringSystem;
import atatec.robocode.parts.movement.EnemyCircleMovingSystem;
import atatec.robocode.parts.scanner.EnemyLockScanningSystem;

/** @author Marcelo Varella Barca Guimarães */
public class Buster extends BaseBot {

  @Override
  protected void configure() {
    independentMovement();

    body().movingBehaviour()
      .use(new EnemyCircleMovingSystem(this));

    gun().aimingBehaviour()
      .use(new DirectAimingSystem(this));

    radar().scanningSystem()
      .use(new EnemyLockScanningSystem(this));

    gun().firingBehaviour()
      .use(new DefaultFiringSystem(this));
  }

  @Override
  protected void battle() {
    while(true) {
      log("************************************");
      radar().scan();
      body().move();
      if(radar().hasLockedTarget()) {
        gun().fire();
      }
      execute();
    }
  }

}
