package atatec.robocode;

import atatec.robocode.calc.Point;
import atatec.robocode.event.EventRegistry;
import atatec.robocode.parts.Body;
import atatec.robocode.parts.Gun;
import atatec.robocode.parts.Radar;
import atatec.robocode.parts.Statistics;
import atatec.robocode.parts.SystemPart;

/**
 * Interface that defines a robot.
 * <p/>
 * In Robobundle, every robot has three independent parts: a {@link Gun}, a {@link Body}
 * and a {@link Radar}. The bot has also a pluggable system and a event driven module
 * designed for better composition of features.
 *
 * @author Marcelo Varella Barca Guimarães
 * @see atatec.robocode.annotation.When
 */
public interface Bot {

  /**
   * Returns a coordinate representing this robot location in the battlefield.
   *
   * @return a coordinate representing this robot location in the battlefield.
   */
  Point location();

  /**
   * Returns the robot's gun part.
   *
   * @return the robot's gun
   */
  Gun gun();

  /**
   * The robot's body part.
   *
   * @return the robot's body
   */
  Body body();

  /**
   * Returns the robot's radar part
   *
   * @return the robot's radar
   */
  Radar radar();

  /**
   * Logs a message in the bot's output stream
   *
   * @param message the message to log
   * @param params  the parameters to the message (if it is a format)
   */
  void log(Object message, Object... params);

  /**
   * Plugs a component that will listen to events through methods annotated with {@link
   * atatec.robocode.annotation.When}
   *
   * @see #events()
   */
  void plug(Object plugin);

  /**
   * Returns the event registry for registering listeners and sending events.
   *
   * @return the event registry
   */
  EventRegistry events();

  /**
   * Returns the robot's statistics.
   *
   * @return the robot's statistics.
   */
  Statistics statistics();

  /** Checks if the given component is activated */
  boolean isActivated(SystemPart systemPart);

  /** Executes the pending movements */
  void execute();

}
