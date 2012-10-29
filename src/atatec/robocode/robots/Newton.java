package atatec.robocode.robots;

import atatec.robocode.AbstractBot;
import atatec.robocode.annotation.When;
import atatec.robocode.event.EnemyFireEvent;
import atatec.robocode.event.Events;

import java.awt.Color;

/** @author Marcelo Varella Barca Guimarães */
public class Newton extends AbstractBot {

  @Override
  public void configure() {
    setBodyColor(Color.GRAY);
    setGunColor(Color.LIGHT_GRAY);
    setRadarColor(Color.RED);
    setBulletColor(Color.ORANGE);

    moveAllPartsSeparated();

    gun().aimingBehaviour()
      .use(predictionAiming());

    gun().firingBehaviour()
      .use(energyBasedFiring());

    radar().scanningBehaviour()
      .use(enemyLockScanning().lockClosestEnemy());

    behaveAs(dodger());
  }

  @When(Events.ENEMY_FIRE)
  public void onEnemyFire(EnemyFireEvent event) {
    body().moveAndTurn(100 * Math.pow(-1, radar().time()),
      event.enemy().bearing().inverse());
  }

  protected void battle() {
    while (true) {
      gun().aim();
      gun().fire();
      radar().scan();
      execute();
    }
  }

}
