package atatec.robocode.parts.gun;

import atatec.robocode.AbstractBot;
import atatec.robocode.Bot;
import atatec.robocode.BotCommand;
import atatec.robocode.behaviour.Behaviours;
import atatec.robocode.calc.Angle;
import atatec.robocode.calc.Point;
import atatec.robocode.calc.Position;
import atatec.robocode.parts.AimingSystem;
import atatec.robocode.parts.BasePart;
import atatec.robocode.parts.BehaviouralSystem;
import atatec.robocode.parts.FiringSystem;
import atatec.robocode.parts.Gun;

import java.awt.Color;

/** @author Marcelo Varella Barca Guimarães */
public class DefaultGun extends BasePart implements Gun {

  private final AbstractBot bot;

  private final BehaviouralSystem<AimingSystem> aimingSystem;

  private final BehaviouralSystem<FiringSystem> firingSystem;

  public DefaultGun(AbstractBot bot) {
    this.bot = bot;
    this.aimingSystem = new BehaviouralSystem<AimingSystem>(bot, this, new AimingBotCommand());
    this.firingSystem = new BehaviouralSystem<FiringSystem>(bot, this, new FiringBotCommand());
  }

  @Override
  public void setColor(Color color) {
    bot.setGunColor(color);
  }

  @Override
  public void setBulletColor(Color color) {
    bot.setBulletColor(color);
  }

  public void aim() {
    aimingSystem.behave();
  }

  public double power() {
    FiringSystem system = firingSystem.activated();
    return system != null ? system.power() : 0;
  }

  @Override
  public void fire(double power) {
    bot.fire(power);
  }

  public void fire() {
    firingSystem.behave();
  }

  @Override
  public Angle heading() {
    return new Angle(bot.getGunHeadingRadians());
  }

  @Override
  public Angle turnRemaining() {
    return new Angle(bot.getGunTurnRemainingRadians());
  }

  @Override
  public double heat() {
    return bot.getGunHeat();
  }

  @Override
  public double coolingRate() {
    return bot.getGunCoolingRate();
  }

  public void turnLeft(Angle angle) {
    if (angle.radians() < 0) {
      turnRight(angle.inverse());
    } else if (angle.radians() > Math.PI) {
      turnRight(Angle.TWO_PI.minus(angle));
    } else if (angle.radians() >= 1E-5) {
      bot.log("Turning gun %s left from %s", angle, heading());
      bot.setTurnGunLeftRadians(angle.radians());
    }
  }

  public void turnRight(Angle angle) {
    if (angle.radians() < 0) {
      turnLeft(angle.inverse());
    } else if (angle.radians() > Math.PI) {
      turnLeft(Angle.TWO_PI.minus(angle));
    } else if (angle.radians() >= 1E-5) {
      bot.log("Turning gun %s right from %s", angle, heading());
      bot.setTurnGunRightRadians(angle.radians());
    }
  }


  public Behaviours<AimingSystem> aimingBehaviour() {
    return aimingSystem;
  }

  public Behaviours<FiringSystem> firingBehaviour() {
    return firingSystem;
  }

  @Override
  public Gun aimTo(Point point) {
    bot.log("Aiming gun to %s", point);
    Position position = bot.location().bearingTo(point);
    Angle angle = position.angle().minus(bot.gun().heading());
    turn(angle);
    return this;
  }

  private class FiringBotCommand implements BotCommand<FiringSystem> {

    public void execute(Bot bot, FiringSystem firingSystem) {
      firingSystem.fire();
    }

  }

  private class AimingBotCommand implements BotCommand<AimingSystem> {

    public void execute(Bot bot, AimingSystem aimingSystem) {
      aimingSystem.aim();
    }

  }
}
