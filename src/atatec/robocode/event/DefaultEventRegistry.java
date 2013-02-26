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

package atatec.robocode.event;

import atatec.robocode.Bot;
import atatec.robocode.annotation.When;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** @author Marcelo Varella Barca Guimarães */
public class DefaultEventRegistry implements EventRegistry {

  private final Bot bot;

  private Map<String, Mapping> mappings = new HashMap<String, Mapping>(20);

  public DefaultEventRegistry(Bot bot) {
    this.bot = bot;
  }

  private Mapping getMapping(String eventName) {
    if (mappings.containsKey(eventName)) {
      return mappings.get(eventName);
    }
    Mapping mapping = new Mapping();
    mappings.put(eventName, mapping);
    return mapping;
  }

  @Override
  public void register(final Object listener) {
    String[] eventNames;
    for (Method method : listener.getClass().getMethods()) {
      if (method.isAnnotationPresent(When.class)) {
        eventNames = method.getAnnotation(When.class).value();
        for (String eventName : eventNames) {
          getMapping(eventName).add(listener, method);
        }
      }
    }
  }

  @Override
  public void send(String eventName, Object... args) {
    getMapping(eventName).send(args);
  }

  private class Mapping {

    private final List<ListenerMapping> listeners;

    private Mapping() {
      this.listeners = new LinkedList<ListenerMapping>();
    }

    public void add(Object listener, Method method) {
      listeners.add(new ListenerMapping(listener, method));
    }

    public void send(Object... args) {
      for (ListenerMapping mapping : listeners) {
        mapping.send(args);
      }
    }

  }

  private class ListenerMapping {

    private Method method;
    private Object listener;

    private ListenerMapping(Object listener, Method method) {
      this.method = method;
      this.listener = listener;
    }

    public void send(Object... args) {
      Class<?>[] parameterTypes = method.getParameterTypes();
      if (args.length == parameterTypes.length) {
        for (int i = 0; i < parameterTypes.length; i++) {
          if (!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
            return;
          }
        }

      } else if (parameterTypes.length == 0) {
        //if the method does not take any args, invoke it without args
        args = null;
      }
      try {
        method.invoke(listener, args);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        bot.log("Error while invoking %s:%n\t%s - %s",
          method, e.getClass(), e.getMessage());
      } catch (InvocationTargetException e) {
        Throwable cause = e.getCause();
        cause.printStackTrace();
        bot.log("Error while invoking %s:%n\t%s - %s",
          method, cause.getClass(), cause.getMessage());
      }
    }

  }

}
