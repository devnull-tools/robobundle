package atatec.robocode.event;

import atatec.robocode.Bot;
import atatec.robocode.annotation.When;
import atatec.robocode.parts.OnOffSystem;
import atatec.robocode.parts.SystemPart;
import org.atatec.trugger.exception.ExceptionHandler;
import org.atatec.trugger.loader.ImplementationLoader;
import org.atatec.trugger.reflection.MethodInvoker;
import org.atatec.trugger.reflection.ReflectionFactory;
import org.atatec.trugger.reflection.Reflector;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** @author Marcelo Varella Barca Guimarães */
public class DefaultEventRegistry implements EventRegistry {

  private final Bot bot;

  private Map<String, Mapping> mappings = new HashMap<String, Mapping>(20);

  private final ReflectionFactory reflectionFactory;

  public DefaultEventRegistry(Bot bot) {
    this.bot = bot;
    this.reflectionFactory = ImplementationLoader.instance().get(ReflectionFactory.class);
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
    bot.log("Registering listener: %s", listener);
    Set<Method> methods = reflect()
      .visible().methods().recursively()
      .annotatedWith(When.class)
      .in(listener);
    bot.log("Found %d listener methods", methods.size());
    String eventName;
    for (Method method : methods) {
      eventName = method.getAnnotation(When.class).value();
      getMapping(eventName).add(listener, method);
    }
  }

  @Override
  public void send(String eventName, Object... args) {
    getMapping(eventName).send(args);
  }

  private Reflector reflect() {
    return reflectionFactory.createReflector();
  }

  private MethodInvoker invoke(Method method) {
    return reflectionFactory.createInvoker(method);
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

  private class ListenerMapping implements ExceptionHandler {

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
        invoke(method).in(listener).handlingExceptionsWith(this).withArgs(args);
      }
    }

    @Override
    public void handle(Throwable throwable) {
      bot.log("Error while invoking %s:%n\t%s - %s",
        method, throwable.getClass(), throwable.getMessage());
    }

  }

}
