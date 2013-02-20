package atatec.robocode.robots;

import atatec.robocode.BaseBot;
import atatec.robocode.parts.aiming.PredictionAimingSystem;
import atatec.robocode.parts.firing.EnergyBasedFiringSystem;
import atatec.robocode.parts.movement.EnemyCircleMovingSystem;
import atatec.robocode.parts.scanner.EnemyLockScanningSystem;

/** @author Marcelo Varella Barca Guimarães */
public class Buster extends BaseBot {

  @Override
  protected void configure() {
    body().movingSystem()
      .use(new EnemyCircleMovingSystem(this));

    gun().aimingSystem()
      .use(new PredictionAimingSystem(this));

    gun().firingSystem()
      .use(new EnergyBasedFiringSystem(this));

    radar().scanningSystem()
      .use(new EnemyLockScanningSystem(this));
  }

  @Override
  protected void doTurnMoves() {
    radar().scan();
    body().move();
    gun().aim().fireIfTargetLocked();
  }

}
