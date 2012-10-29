package atatec.robocode.event;

import atatec.robocode.Bot;
import atatec.robocode.annotation.When;
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
    Method[] methods = listener.getClass().getMethods();
    String eventName;
    for (Method method : methods) {
      if (method.isAnnotationPresent(When.class)) {
        eventName = method.getAnnotation(When.class).value();
        getMapping(eventName).add(listener, method);
      }
    }
  }

  @Override
  public void send(String eventName) {
    getMapping(eventName).send();
  }

  @Override
  public void send(String eventName, Object eventObject) {
    getMapping(eventName).send(eventObject);
  }

  private class Mapping {

    private final List<ListenerMapping> listeners;

    private Mapping() {
      this.listeners = new LinkedList<ListenerMapping>();
    }

    public void add(Object listener, Method method) {
      listeners.add(new ListenerMapping(listener, method));
    }

    public void send(Object event) {
      for (ListenerMapping mapping : listeners) {
        Object listener = mapping.listener;
        if (listener instanceof SystemPart) {
          if (bot.isActivated(listener)) {
            mapping.send(event);
          }
        } else {
          mapping.send(event);
        }
      }
    }

    public void send() {
      for (ListenerMapping mapping : listeners) {
        Object listener = mapping.listener;
        if (listener instanceof SystemPart) {
          if (bot.isActivated(listener)) {
            mapping.send();
          }
        } else {
          mapping.send();
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

    public void send() {
      try {
        method.invoke(listener);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    public void send(Object event) {
      try {
        method.invoke(listener, event);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }

  }

}
