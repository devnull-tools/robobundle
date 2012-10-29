package atatec.robocode.parts;

import atatec.robocode.Condition;

/** @author Marcelo Varella Barca Guimarães */
public interface Behaviour<E> {

  Behaviour<E> use(E component);

  Behaviour<E> when(Condition condition);

  Behaviour<E> inOtherCases();

  E activated();

}
