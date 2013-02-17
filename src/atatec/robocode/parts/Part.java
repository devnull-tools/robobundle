package atatec.robocode.parts;

import atatec.robocode.calc.Angle;

import java.awt.Color;

/** @author Marcelo Varella Barca Guimarães */
public interface Part extends OnOffSystem {

  void turn(Angle angle);

  Angle turnRemaining();

  Angle heading();

  void setColor(Color color);

}
