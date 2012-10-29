package atatec.robocode.parts;

import atatec.robocode.calc.Angle;

/** @author Marcelo Varella Barca Guimarães */
public interface Part extends OnOffSystem {

  void turn(Angle angle);

  Angle turnRemaining();

  Angle heading();

}
