package atatec.robocode;

import atatec.robocode.condition.ConditionSelector;

/** @author Marcelo Varella Barca Guimarães */
public interface Conditional<E> {

  ConditionSelector<Conditional<E>> use(E component);

  E activated();

}
