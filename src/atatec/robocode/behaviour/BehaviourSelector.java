package atatec.robocode.behaviour;

import atatec.robocode.Condition;

/** @author Marcelo Varella Barca Guimarães */
public interface BehaviourSelector<E> {

  Behaviour<E> when(Condition condition);

  Behaviour<E> inOtherCases();


}
