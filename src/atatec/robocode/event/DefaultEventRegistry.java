package atatec.robocode.event;

import atatec.robocode.Bot;
import atatec.robocode.annotation.When;
import atatec.robocode.parts.OnOffSystem;
import atatec.robocode.parts.SystemPart;

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
    String eventName;
    for (Method method : listener.getClass().getMethods()) {
      if (method.isAnnotationPresent(When.class)) {
        eventName = method.getAnnotation(When.class).value();
        getMapping(eventName).add(listener, method);
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
        Object listener = mapping.listener;
        if (listener instanceof OnOffSystem) {
          if (((OnOffSystem) listener).isOn()) {
            mapping.send(args);
          }
        } else if (listener instanceof SystemPart) {
          if (bot.isActivated(listener)) {
            mapping.send(args);
          }
        } else {
          mapping.send(args);
        }
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
        bot.log("Error while invoking %s:%n\t%s - %s",
          method, e.getClass(), e.getMessage());
      } catch (InvocationTargetException e) {
        Throwable cause = e.getCause();
        bot.log("Error while invoking %s:%n\t%s - %s",
          method, cause.getClass(), cause.getMessage());
      }
    }

  }

}
