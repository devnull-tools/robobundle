package atatec.robocode.behaviour;

/** @author Marcelo Varella Barca Guimarães */
public interface Behaviour<E> {

  BehaviourSelector<E> use(E component);

  E activated();

}
