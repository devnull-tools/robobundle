package atatec.robocode.event;

/** @author Marcelo Varella Barca Guimarães */
public interface EventRegistry {

  void register(Object listener);

  void send(String eventName, Object... args);

}
