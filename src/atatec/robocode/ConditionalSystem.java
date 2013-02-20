package atatec.robocode;

import atatec.robocode.condition.ConditionSelector;

/** @author Marcelo Varella Barca Guimarães */
public interface ConditionalSystem<E> {

  ConditionSelector<ConditionalSystem<E>> use(E component);

  E activated();

}
