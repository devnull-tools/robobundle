package atatec.robocode.ioc;

import atatec.robocode.Bot;
import robocode.exception.RobotException;

/** @author Marcelo Varella Barca Guimarães */
public class Instantiator {

  private final Bot bot;

  public Instantiator(Bot bot) {
    this.bot = bot;
  }

  public <T> T instantiate(Class<T> type) {
    try {
      try {
        return type.getConstructor(Bot.class).newInstance(bot);
      } catch (NoSuchMethodException e) {
        return type.getConstructor().newInstance();
      }
    } catch (Exception e) {
      e.printStackTrace();
      bot.log("Error while instantiating %s: %s", type.getCanonicalName(), e.getMessage());
      throw new RobotException(e.getMessage());
    }
  }

}
