package atatec.robocode;

import atatec.robocode.calc.Point;
import atatec.robocode.event.BulletFiredEvent;
import atatec.robocode.event.DefaultEventRegistry;
import atatec.robocode.event.EnemyScannedEvent;
import atatec.robocode.event.EventRegistry;
import atatec.robocode.parts.AimingSystem;
import atatec.robocode.parts.Body;
import atatec.robocode.parts.FiringSystem;
import atatec.robocode.parts.Gun;
import atatec.robocode.parts.MovingSystem;
import atatec.robocode.parts.Radar;
import atatec.robocode.parts.ScanningSystem;
import atatec.robocode.parts.Statistics;
import atatec.robocode.parts.body.DefaultBody;
import atatec.robocode.parts.gun.DefaultGun;
import atatec.robocode.parts.radar.DefaultRadar;
import atatec.robocode.util.Drawer;
import robocode.AdvancedRobot;
import robocode.Bullet;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.RoundEndedEvent;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import static atatec.robocode.event.Events.BULLET_FIRED;
import static atatec.robocode.event.Events.BULLET_HIT;
import static atatec.robocode.event.Events.BULLET_HIT_BULLET;
import static atatec.robocode.event.Events.BULLET_MISSED;
import static atatec.robocode.event.Events.DEATH;
import static atatec.robocode.event.Events.DRAW;
import static atatec.robocode.event.Events.ENEMY_SCANNED;
import static atatec.robocode.event.Events.HIT_BY_BULLET;
import static atatec.robocode.event.Events.HIT_ROBOT;
import static atatec.robocode.event.Events.HIT_WALL;
import static atatec.robocode.event.Events.PAINT;
import static atatec.robocode.event.Events.ROBOT_DEATH;
import static atatec.robocode.event.Events.ROUND_ENDED;
import static atatec.robocode.event.Events.ROUND_STARTED;
import static atatec.robocode.event.Events.WIN;

/** @author Marcelo Varella Barca Guimarães */
public abstract class BaseBot extends AdvancedRobot implements Bot {

  private Gun gun;

  private Body body;

  private Radar radar;

  private EventRegistry eventRegistry = new DefaultEventRegistry(this);

  private BotStatistics statistics = new BotStatistics();

  private Map<Class, Conditional> behavioursMap;

  protected final void initialize(Gun gun, Body body, Radar radar) {
    this.gun = gun;
    this.body = body;
    this.radar = radar;

    this.behavioursMap = new HashMap<Class, Conditional>();
    behavioursMap.put(AimingSystem.class, gun.aimingBehaviour());
    behavioursMap.put(FiringSystem.class, gun.firingBehaviour());
    behavioursMap.put(ScanningSystem.class, radar.scanningSystem());
    behavioursMap.put(MovingSystem.class, body.movingBehaviour());
  }

  protected void initialize() {
    initialize(new DefaultGun(this), new DefaultBody(this), new DefaultRadar(this));
  }

  protected void independentMovement() {
    setAdjustGunForRobotTurn(true);
    setAdjustRadarForGunTurn(true);
    setAdjustRadarForRobotTurn(true);
  }

  protected abstract void configure();

  protected abstract void battle();

  public final void run() {
    initialize();
    configure();
    events().register(body);
    events().register(gun);
    events().register(radar);
    events().register(this);
    events().send(ROUND_STARTED);
    battle();
  }

  public Point location() {
    return new Point(getX(), getY());
  }

  public Gun gun() {
    return gun;
  }

  public Body body() {
    return body;
  }

  public Radar radar() {
    return radar;
  }

  public EventRegistry events() {
    return eventRegistry;
  }

  @Override
  public void fire(double power) {
    if (getGunHeat() == 0) {
      Bullet bullet = super.setFireBullet(power);
      if (bullet != null) {
        statistics.shots++;
        eventRegistry.send(BULLET_FIRED, new BulletFiredEvent(bullet));
      }
    }
  }

  public void log(Object message, Object... params) {
    out.printf(message.toString(), params);
    out.println();
  }

  public void clearEvents() {
    clearAllEvents();
  }

  @Override
  public void onScannedRobot(ScannedRobotEvent event) {
    eventRegistry.send(ENEMY_SCANNED, new EnemyScannedEvent(this, event));
  }

  @Override
  public void onBulletHit(BulletHitEvent event) {
    statistics.hits++;
    eventRegistry.send(BULLET_HIT, event);
  }

  @Override
  public void onBulletHitBullet(BulletHitBulletEvent event) {
    statistics.missed++;
    eventRegistry.send(BULLET_HIT_BULLET, event);
  }

  @Override
  public void onBulletMissed(BulletMissedEvent event) {
    statistics.missed++;
    eventRegistry.send(BULLET_MISSED, event);
  }

  @Override
  public void onHitByBullet(HitByBulletEvent event) {
    statistics.taken++;
    eventRegistry.send(HIT_BY_BULLET, event);
  }

  @Override
  public void onRobotDeath(RobotDeathEvent event) {
    eventRegistry.send(ROBOT_DEATH, event);
  }

  @Override
  public void onHitRobot(HitRobotEvent event) {
    eventRegistry.send(HIT_ROBOT, event);
  }

  @Override
  public void onHitWall(HitWallEvent event) {
    eventRegistry.send(HIT_WALL, event);
  }

  @Override
  public void onPaint(Graphics2D g) {
    eventRegistry.send(PAINT, g);
    eventRegistry.send(DRAW, new Drawer(g));
  }

  @Override
  public void onDeath(DeathEvent event) {
    eventRegistry.send(DEATH, event);
  }

  @Override
  public void onWin(WinEvent event) {
    eventRegistry.send(WIN, event);
  }

  @Override
  public void onRoundEnded(RoundEndedEvent event) {
    eventRegistry.send(ROUND_ENDED, event);
  }

  @Override
  public void plug(Object behaviour) {
    eventRegistry.register(behaviour);
  }

  @Override
  public Statistics statistics() {
    return statistics;
  }

  @Override
  public boolean isActivated(Object component) {
    for (Map.Entry<Class, Conditional> entry : behavioursMap.entrySet()) {
      if (entry.getKey().isAssignableFrom(component.getClass())
        && entry.getValue().activated().equals(component)) {
        return true;
      }
    }
    return false;
  }

  private class BotStatistics implements Statistics {

    private long hits;

    private long missed;

    private long shots;

    private long taken;

    @Override
    public double accuracy() {
      long bullets = hits + missed;
      return bullets > 0 ? hits == 0 ? 0 : bullets / hits : 1;
    }

    @Override
    public long bulletsFired() {
      return shots;
    }

    @Override
    public long bulletsHited() {
      return hits;
    }

    @Override
    public long bulletsMissed() {
      return missed;
    }

    @Override
    public long bulletsTaken() {
      return taken;
    }
  }


}
