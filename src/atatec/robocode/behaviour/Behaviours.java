package atatec.robocode.behaviour;

import atatec.robocode.condition.ConditionSelector;

/** @author Marcelo Varella Barca Guimarães */
public interface Behaviours<E> {

  ConditionSelector<Behaviours<E>> use(E component);

  E activated();

}
