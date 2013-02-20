package atatec.robocode;

import atatec.robocode.condition.ConditionSelector;
import atatec.robocode.parts.SystemPart;

/** @author Marcelo Varella Barca Guimarães */
public interface ConditionalSystem<E extends SystemPart> {

  ConditionSelector<ConditionalSystem<E>> use(E systemPart);

  E activated();

}
