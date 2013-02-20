package atatec.robocode.condition;

import atatec.robocode.Bot;
import atatec.robocode.Condition;
import atatec.robocode.Enemy;
import atatec.robocode.parts.MovingSystem;
import atatec.robocode.parts.Statistics;

/** @author Marcelo Varella Barca Guimarães */
public class Conditions {

  public static final Condition ALWAYS = new Condition() {

    @Override
    public boolean evaluate(Bot bot) {
      return true;
    }
  };

  public static Condition targetLocked() {
    return new Condition() {
      @Override
      public boolean evaluate(Bot bot) {
        return bot.radar().hasLockedTarget();
      }
    };
  }

  public static Condition enemyIsMoving() {
    return new Condition() {
      public boolean evaluate(Bot bot) {
        Enemy enemy = bot.radar().lockedTarget();
        return enemy != null && enemy.velocity() != 0;
      }
    };
  }

  public static Condition enemyIsNotMoving() {
    return new Condition() {
      public boolean evaluate(Bot bot) {
        Enemy enemy = bot.radar().lockedTarget();
        return enemy != null && enemy.velocity() == 0;
      }
    };
  }

  public static Condition enemyIsAtLeastAt(final double distance) {
    return new Condition() {
      public boolean evaluate(Bot bot) {
        Enemy enemy = bot.radar().lockedTarget();
        return enemy != null && enemy.distance() > distance;
      }
    };
  }

  public static Condition enemyIsAtMost(final double distance) {
    return new Condition() {
      public boolean evaluate(Bot bot) {
        Enemy enemy = bot.radar().lockedTarget();
        return enemy != null && enemy.distance() < distance;
      }
    };
  }

  public static Condition enemyIsAtRange(final double minimum, final double maximum) {
    return new Condition() {
      public boolean evaluate(Bot bot) {
        Enemy enemy = bot.radar().lockedTarget();
        double distance = enemy == null ? 0 : enemy.distance();
        return distance >= minimum && distance < maximum;
      }
    };
  }

  public static Condition energyIsAtLest(final double energy) {
    return new Condition() {
      @Override
      public boolean evaluate(Bot bot) {
        return bot.body().energy() >= energy;
      }
    };
  }

  public static Condition hitRateIsAtLeast(final double rate) {
    return new Condition() {
      @Override
      public boolean evaluate(Bot bot) {
        return bot.statistics().accuracy() >= rate;
      }
    };
  }

  public static Condition hitRateIsBellow(final double rate) {
    return new Condition() {
      @Override
      public boolean evaluate(Bot bot) {
        Statistics statistics = bot.statistics();
        bot.log("%d bullets fired", statistics.bulletsFired());
        bot.log("%d bullets hited", statistics.bulletsHited());
        bot.log("%d bullets missed", statistics.bulletsMissed());
        return statistics.bulletsFired() > 0 && statistics.accuracy() < rate;
      }
    };
  }

  public static Condition headToHeadBattle() {
    return new Condition() {
      @Override
      public boolean evaluate(Bot bot) {
        return bot.radar().enemiesCount() == 1;
      }
    };
  }

  public static Condition usingMovingSystem(final Class<? extends MovingSystem> type) {
    return new Condition() {
      @Override
      public boolean evaluate(Bot bot) {
        MovingSystem activated = bot.body().movingSystem().activated();
        return activated != null && activated.getClass().equals(type);
      }
    };
  }

}
