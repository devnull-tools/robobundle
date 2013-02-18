package atatec.robocode;

import atatec.robocode.calc.Point;
import atatec.robocode.event.EventRegistry;
import atatec.robocode.parts.Body;
import atatec.robocode.parts.Gun;
import atatec.robocode.parts.Radar;
import atatec.robocode.parts.Statistics;

/**
 * Interface that defines a robot.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public interface Bot {

  /**
   * Returns a coordinate representing this bot location in the battlefield.
   *
   * @return a coordinate representing this bot location in the battlefield.
   */
  Point location();

  /**
   * Returns the bot's gun abstraction.
   *
   * @return the bot's gun
   */
  Gun gun();

  /**
   * The bot's body abstraction.
   *
   * @return the bot's body
   */
  Body body();

  /**
   * Returns the bot's radar abstraction
   *
   * @return the bot's radar
   */
  Radar radar();

  /**
   * Logs a message in the bot's output stream
   *
   * @param message the message to log
   * @param params  the parameters to the message (if it is a format)
   */
  void log(Object message, Object... params);

  void plug(Object plugin);

  Statistics statistics();

  boolean isActivated(Object component);

  EventRegistry events();

}
