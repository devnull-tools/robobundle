package atatec.robocode.parts;

import atatec.robocode.Conditional;
import atatec.robocode.Bot;
import atatec.robocode.BotCommand;
import atatec.robocode.Condition;
import atatec.robocode.condition.ConditionSelector;
import atatec.robocode.condition.Conditions;

import java.util.LinkedHashMap;
import java.util.Map;

/** @author Marcelo Varella Barca Guimarães */
public class ConditionalSystem<E> implements Conditional<E> {

  private final Map<Condition, E> components = new LinkedHashMap<Condition, E>();

  private E current;

  private final Part part;

  private final BotCommand<E> command;

  private final Bot bot;

  public ConditionalSystem(Bot bot, Part part, BotCommand<E> command) {
    this.part = part;
    this.command = command;
    this.bot = bot;
  }

  @Override
  public ConditionSelector<Conditional<E>> use(E component) {
    this.current = component;
    this.bot.plug(component);
    return new ConditionSelector<Conditional<E>>() {

      public Conditional<E> when(Condition condition) {
        components.put(condition, current);
        current = null;
        return ConditionalSystem.this;
      }

      public void inOtherCases() {
        when(Conditions.ALWAYS);
      }

    };
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
