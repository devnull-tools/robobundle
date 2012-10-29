package atatec.robocode.parts.radar;

import atatec.robocode.AbstractBot;
import atatec.robocode.BattleField;
import atatec.robocode.Bot;
import atatec.robocode.BotCommand;
import atatec.robocode.Enemy;
import atatec.robocode.Field;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.Angle;
import atatec.robocode.event.EnemyScannedEvent;
import atatec.robocode.event.Events;
import atatec.robocode.parts.BasePart;
import atatec.robocode.parts.Behaviour;
import atatec.robocode.parts.BehaviouralSystem;
import atatec.robocode.parts.Radar;
import atatec.robocode.parts.ScanningSystem;
import atatec.robocode.parts.scanner.DefaultScanningSystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** @author Marcelo Varella Barca Guimarães */
public class DefaultRadar extends BasePart implements Radar {

  private final BehaviouralSystem<ScanningSystem> behaviour;

  private final AbstractBot bot;

  private Enemy target;

  private Map<String, Enemy> enemies = new HashMap<String, Enemy>();

  public DefaultRadar(AbstractBot bot) {
    this(bot, new DefaultScanningSystem());
  }

  public DefaultRadar(AbstractBot bot, ScanningSystem scanningSystem) {
    this.bot = bot;
    this.behaviour = new BehaviouralSystem<ScanningSystem>(
      bot, this, new RadarCommand()
    );
    this.behaviour.use(scanningSystem);
  }

  public Behaviour<ScanningSystem> scanningBehaviour() {
    return behaviour;
  }

  @Override
  public int enemiesCount() {
    return bot.getOthers();
  }

  @Override
  public Field battleField() {
    return new BattleField(bot);
  }

  @Override
  public long time() {
    return bot.getTime();
  }

  public void scan() {
    behaviour.behave();
  }

  public Enemy lockedTarget() {
    return target;
  }

  @Override
  public Collection<Enemy> knownEnemies() {
    return new ArrayList<Enemy>(enemies.values());
  }

  public void lockTarget(Enemy e) {
    this.target = e;
  }

  @Override
  public Angle heading() {
    return new Angle(bot.getRadarHeadingRadians());
  }

  @Override
  public Angle turnRemaining() {
    return new Angle(bot.getRadarTurnRemainingRadians());
  }

  @Override
  public void turnLeft(Angle angle) {
    bot.setTurnRadarLeftRadians(angle.radians());
  }

  @Override
  public void turnRight(Angle angle) {
    bot.setTurnRadarRightRadians(angle.radians());
  }

  @When(Events.ENEMY_SCANNED)
  public void onEnemyScanned(EnemyScannedEvent event) {
    Enemy enemy = event.enemy();
    enemies.put(enemy.name(), enemy);
  }

  private class RadarCommand implements BotCommand<ScanningSystem> {

    public void execute(Bot bot, ScanningSystem scanningSystem) {
      scanningSystem.scan();
    }

  }

}
