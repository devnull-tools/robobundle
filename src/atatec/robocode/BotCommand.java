package atatec.robocode;

/** @author Marcelo Varella Barca Guimarães */
public interface BotCommand<E> {

  void execute(Bot bot, E parameter);

}
