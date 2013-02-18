package atatec.robocode.condition;

import atatec.robocode.Condition;

/** @author Marcelo Varella Barca Guimarães */
public interface ConditionSelector<E> {

  E when(Condition condition);

  void inOtherCases();

}
