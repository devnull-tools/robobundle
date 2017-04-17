/************************************************************************************
 * The MIT License                                                                  *
 *                                                                                  *
 * Copyright (c) 2013 Marcelo Guimarães <ataxexe at gmail dot com>                  *
 * -------------------------------------------------------------------------------- *
 * Permission  is hereby granted, free of charge, to any person obtaining a copy of *
 * this  software  and  associated documentation files (the "Software"), to deal in *
 * the  Software  without  restriction,  including without limitation the rights to *
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of *
 * the  Software, and to permit persons to whom the Software is furnished to do so, *
 * subject to the following conditions:                                             *
 *                                                                                  *
 * The  above  copyright notice and this permission notice shall be included in all *
 * copies or substantial portions of the Software.                                  *
 *                            --------------------------                            *
 * THE  SOFTWARE  IS  PROVIDED  "AS  IS",  WITHOUT WARRANTY OF ANY KIND, EXPRESS OR *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS *
 * FOR  A  PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR *
 * COPYRIGHT  HOLDERS  BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER *
 * IN  AN  ACTION  OF  CONTRACT,  TORT  OR  OTHERWISE,  ARISING  FROM, OUT OF OR IN *
 * CONNECTION  WITH  THE  SOFTWARE  OR  THE  USE OR OTHER DEALINGS IN THE SOFTWARE. *
 ************************************************************************************/

package tools.devnull.robobundle.parts;

import tools.devnull.robobundle.Bot;
import tools.devnull.robobundle.Command;
import tools.devnull.robobundle.ConditionalCommand;
import tools.devnull.robobundle.condition.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Marcelo Guimarães
 */
public class DefaultConditionalCommand<E extends Command> implements ConditionalCommand<E> {

  private final Map<Condition, E> components = new LinkedHashMap<Condition, E>();
  private E current;

  private final Bot bot;

  private final BotConditions conditions;

  public DefaultConditionalCommand(Bot bot) {
    this.bot = bot;
    conditions = new BotConditions(bot);
  }

  protected final TargetConditions target() {
    return conditions.target();
  }

  protected final BodyConditions body() {
    return conditions.body();
  }

  protected final GunConditions gun() {
    return conditions.gun();
  }

  protected final RadarConditions radar() {
    return conditions.radar();
  }

  @Override
  public ConditionSelector<ConditionalCommand<E>> use(E command) {
    this.current = command;
    this.bot.plug(command);
    return new ConditionSelector<ConditionalCommand<E>>() {

      public ConditionalCommand<E> when(Condition condition) {
        bot.plug(condition);
        components.put(condition, current);
        current = null;
        return DefaultConditionalCommand.this;
      }

      public void inOtherCases() {
        when(() -> true);
      }

    };
  }

  public E activated() {
    if (components.isEmpty()) {
      return current;
    } else {
      for (Map.Entry<Condition, E> entry : components.entrySet()) {
        if (entry.getKey().evaluate()) {
          return entry.getValue();
        }
      }
    }
    return null;
  }

  @Override
  public void execute() {
    E activated = activated();
    if (activated != null) {
      activated.execute();
    }
  }

}
