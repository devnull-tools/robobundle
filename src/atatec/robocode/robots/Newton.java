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
    setBodyColor(new Color(39, 40, 34));
    setGunColor(new Color(230, 219, 116));
    setRadarColor(new Color(39, 40, 34));

    moveAllPartsSeparated();

    gun().aimingBehaviour()
      .use(predictionAiming());

    gun().firingBehaviour()
      .use(energyBasedFiring());

    radar().scanningBehaviour()
      .use(enemyLockScanning().lockClosestEnemy());

    behaveAs(dodger());
    behaveAs(
      bulletPainter()
        .use(new Color(255, 84, 84)).forStrong()
        .use(new Color(253, 151, 31)).forMedium()
        .use(new Color(54, 151, 255)).forWeak()
    );
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
