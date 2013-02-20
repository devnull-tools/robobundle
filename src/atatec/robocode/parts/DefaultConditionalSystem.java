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
public class DefaultConditionalSystem<E extends SystemPart> implements ConditionalSystem<E>, Command {

  private final Map<Condition, E> components = new LinkedHashMap<Condition, E>();

  private E current;

  private final Part part;

  private final Bot bot;

  public DefaultConditionalSystem(Bot bot, Part part) {
    this.part = part;
    this.bot = bot;
  }

  @Override
  public ConditionSelector<ConditionalSystem<E>> use(E systemPart) {
    this.current = systemPart;
    this.bot.plug(systemPart);
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
    if (activated != null) {
      activated.execute();
    }
  }

}
