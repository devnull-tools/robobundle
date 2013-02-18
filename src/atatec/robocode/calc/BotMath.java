package atatec.robocode.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** @author Marcelo Varella Barca Guimarães */
public final class BotMath {

  public static BigDecimal toBigecimal(double a) {
    return BigDecimal.valueOf(a).setScale(6, RoundingMode.HALF_UP);
  }

  public static boolean areEquals(double a, double b) {
    return toBigecimal(a).equals(toBigecimal(b));
  }

  public static int compare(double a, double b) {
    return toBigecimal(a).compareTo(toBigecimal(b));
  }

}
