package atatec.robocode.parts.aiming;

import atatec.robocode.Bot;
import atatec.robocode.Enemy;
import atatec.robocode.annotation.When;
import atatec.robocode.calc.Angle;
import atatec.robocode.event.EnemyScannedEvent;
import atatec.robocode.event.Events;
import atatec.robocode.parts.AimingSystem;
import robocode.Rules;

import java.util.HashMap;
import java.util.Map;

/** @author Marcelo Guimarães */
public class SymbolicPatternMatcher implements AimingSystem {

  private final PatternCodec codec;
  private final Bot bot;
  private Map<String, StringBuilder> patterns;

  private int searchDepth = 40;

  public SymbolicPatternMatcher(PatternCodec codec, Bot bot) {
    this.codec = codec;
    this.bot = bot;
    this.patterns = new HashMap<String, StringBuilder>();
  }

  private StringBuilder patternFor(String name) {
    if (!patterns.containsKey(name)) {
      patterns.put(name, new StringBuilder(3000));
    }
    return patterns.get(name);
  }

  private StringBuilder patternFor(Enemy enemy) {
    return patternFor(enemy.name());
  }

  @When(Events.ENEMY_SCANNED)
  public void registerMovement(EnemyScannedEvent event) {
    Enemy enemy = event.enemy();
    patternFor(enemy.name()).append(codec.encode(enemy));
  }

  @Override
  public void execute() {
    if (bot.radar().hasLockedTarget()) {
      Enemy target = bot.radar().target();
      StringBuilder history = patternFor(target);
      int historyIndex = history.length();
      int matchIndex;
      int bulletSpeed = (int) Rules.getBulletSpeed(bot.gun().power());
      int searchEnd = searchDepth + (int) Rules.RADAR_SCAN_RADIUS / bulletSpeed;

      do {
        matchIndex = history.lastIndexOf(
          history.substring(historyIndex - --searchDepth),
          historyIndex - searchEnd);
      }
      while (matchIndex < 0);

      matchIndex += searchDepth;

      int endIndex = matchIndex + ((int) (target.distance() / bulletSpeed));

      Angle angle = codec.calculateTurnAngle(bot, target, history, matchIndex, endIndex);
      bot.radar().turn(angle);
    }
  }

}
