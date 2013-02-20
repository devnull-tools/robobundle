package atatec.robocode.parts;

import atatec.robocode.Bot;
import atatec.robocode.Command;
import atatec.robocode.Condition;
import atatec.robocode.ConditionalSystem;
import atatec.robocode.condition.ConditionSelector;
import atatec.robocode.condition.Conditions;

import java.util.LinkedHashMap;
import java.util.Map;

/** @author Marcelo Varella Barca Guimarães */
public class DefaultConditionalSystem<E extends Command> implements ConditionalSystem<E>, Command {

  private final Map<Condition, E> components = new LinkedHashMap<Condition, E>();

  private E current;

  private final Part part;

  private final Bot bot;

  public DefaultConditionalSystem(Bot bot, Part part) {
    this.part = part;
    this.bot = bot;
  }

  @Override
  public ConditionSelector<ConditionalSystem<E>> use(E component) {
    this.current = component;
    this.bot.plug(component);
    return new ConditionSelector<ConditionalSystem<E>>() {

      public ConditionalSystem<E> when(Condition condition) {
        components.put(condition, current);
        current = null;
        return DefaultConditionalSystem.this;
      }

      public void inOtherCases() {
        when(Conditions.ALWAYS);
      }

    };
  }

  public E activated() {
    if (part.isOn()) {
      if (components.isEmpty()) {
        return current;
      } else {
        for (Map.Entry<Condition, E> entry : components.entrySet()) {
          if (entry.getKey().evaluate(bot)) {
            return entry.getValue();
          }
        }
      }
    }
    return null;
  }

  @Override
  public void execute() {
    E activated = activated();
    if(activated != null) {
      activated.execute();
    }
  }

}
