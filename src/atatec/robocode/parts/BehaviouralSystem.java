package atatec.robocode.parts;

import atatec.robocode.Bot;
import atatec.robocode.BotCommand;
import atatec.robocode.Condition;
import atatec.robocode.behaviour.Behaviour;
import atatec.robocode.behaviour.BehaviourSelector;

import java.util.LinkedHashMap;
import java.util.Map;

/** @author Marcelo Varella Barca Guimarães */
public class BehaviouralSystem<E> implements Behaviour<E>, BehaviourSelector<E> {

  private final Map<Condition, E> components = new LinkedHashMap<Condition, E>();

  private E current;

  private E usedForOtherCases;

  private final Part part;

  private final BotCommand<E> command;

  private final Bot bot;

  public BehaviouralSystem(Bot bot, Part part, BotCommand<E> command) {
    this.part = part;
    this.command = command;
    this.bot = bot;
  }

  @Override
  public BehaviourSelector<E> use(E component) {
    this.current = component;
    this.bot.behaveAs(component);
    return this;
  }

  public Behaviour<E> when(Condition condition) {
    this.components.put(condition, current);
    current = null;
    return this;
  }

  public Behaviour<E> inOtherCases() {
    this.usedForOtherCases = current;
    return this;
  }

  public void behave() {
    if (part.isOn()) {
      execute(activated());
    }
  }

  public E activated() {
    if (components.isEmpty()) {
      return current;
    } else {
      for (Map.Entry<Condition, E> entry : components.entrySet()) {
        if (entry.getKey().evaluate(bot)) {
          return entry.getValue();
        }
      }
      if (usedForOtherCases != null) {
        return usedForOtherCases;
      }
    }
    return null;
  }

  private void execute(E component) {
    if (component != null) {
      bot.log("Executing " + component.getClass().getSimpleName());
      this.command.execute(bot, component);
    }
  }

}
