package atatec.robocode.robots;

import atatec.robocode.BaseBot;
import atatec.robocode.annotation.When;
import atatec.robocode.plugin.BulletPaint;
import atatec.robocode.plugin.Dodger;
import atatec.robocode.event.EnemyFireEvent;
import atatec.robocode.event.Events;
import atatec.robocode.parts.aiming.PredictionAimingSystem;
import atatec.robocode.parts.firing.EnergyBasedFiringSystem;
import atatec.robocode.parts.scanner.EnemyLockScanningSystem;
import atatec.robocode.plugin.EnemyScannerInfo;

import java.awt.Color;

/** @author Marcelo Varella Barca Guimarães */
public class Newton extends BaseBot {

  @Override
  public void configure() {
    body().setColor(new Color(39, 40, 34));
    gun().setColor(new Color(230, 219, 116));
    radar().setColor(new Color(39, 40, 34));

    independentMovement();

    gun().aimingBehaviour()
      .use(new PredictionAimingSystem(this));

    gun().firingBehaviour()
      .use(new EnergyBasedFiringSystem(this));

    radar().scanningSystem()
      .use(new EnemyLockScanningSystem(this).lockClosestEnemy());

    plug(new Dodger(this));
    plug(new EnemyScannerInfo(this));
    plug(new BulletPaint(this)
      .use(new Color(255, 84, 84)).forStrong()
      .use(new Color(253, 151, 31)).forMedium()
      .use(new Color(54, 151, 255)).forWeak());
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
