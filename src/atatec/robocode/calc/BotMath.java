package atatec.robocode.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** @author Marcelo Varella Barca Guimarães */
public final class BotMath {

  public static BigDecimal convert(double a) {
    return BigDecimal.valueOf(a).setScale(6, RoundingMode.HALF_UP);
  }

  public static boolean areEquals(double a, double b) {
    return convert(a).equals(convert(b));
  }

  public static int compare(double a, double b) {
    return convert(a).compareTo(convert(b));
  }

}
