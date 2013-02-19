package atatec.robocode.parts.radar;

import atatec.robocode.AbstractBot;
import atatec.robocode.BattleField;
import atatec.robocode.Conditional;
import atatec.robocode.Bot;
import atatec.robocode.BotCommand;
import atatec.robocode.Enemy;
import atatec.robocode.Field;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.Angle;
import atatec.robocode.event.EnemyScannedEvent;
import atatec.robocode.event.Events;
import atatec.robocode.parts.BasePart;
import atatec.robocode.parts.ConditionalSystem;
import atatec.robocode.parts.Radar;
import atatec.robocode.parts.ScanningSystem;
import atatec.robocode.parts.scanner.DefaultScanningSystem;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** @author Marcelo Varella Barca Guimarães */
public class DefaultRadar extends BasePart implements Radar {

  private final ConditionalSystem<ScanningSystem> behaviour;

  private final AbstractBot bot;

  private Enemy target;

  private Map<String, Enemy> enemies = new HashMap<String, Enemy>();

  public DefaultRadar(AbstractBot bot) {
    this.bot = bot;
    this.behaviour = new ConditionalSystem<ScanningSystem>(
      bot, this, new RadarCommand()
    );
    this.behaviour.use(new DefaultScanningSystem());
  }

  @Override
  public void setColor(Color color) {
    bot.setRadarColor(color);
  }

  public Conditional<ScanningSystem> scanningBehaviour() {
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

  public boolean hasLockedTarget() {
    return target != null;
  }

  public Collection<Enemy> knownEnemies() {
    return new ArrayList<Enemy>(enemies.values());
  }

  public void lockTarget(Enemy e) {
    this.target = e;
  }

  @Override
  public void unlockTarget() {
    this.target = null;
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

  private static class RadarCommand implements BotCommand<ScanningSystem> {

    public void execute(Bot bot, ScanningSystem scanningSystem) {
      scanningSystem.scan();
    }

  }

}