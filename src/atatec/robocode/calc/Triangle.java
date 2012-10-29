package atatec.robocode.calc;

/** @author Marcelo Varella Barca Guimarães */
public class Triangle {

  public enum ID {
    A, B, C
  }

  private final double a;
  private final double b;
  private final double c;

  private final Angle alpha;
  private final Angle beta;
  private final Angle gama;

  public Triangle(double sideA, double sideB, double sideC) {
    this.a = sideA;
    this.b = sideB;
    this.c = sideC;
    //cosine rule
    double cosGama = (Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2 * a * b);
    this.gama = new Angle(Math.acos(cosGama));
    //sine rule
    double sinAlpha = a * gama.sin() / c;
    this.alpha = new Angle(Math.asin(sinAlpha));
    //triangle rule (the sum of the internal angles equals 180 degres
    this.beta = Angle.PI.minus(alpha).minus(gama);
  }

  public double side(ID id) {
    switch (id) {
      case A:
        return a;
      case B:
        return b;
      case C:
        return c;
      default:
        throw new IllegalArgumentException();
    }
  }

  public Angle angle(ID id) {
    switch (id) {
      case A:
        return alpha;
      case B:
        return beta;
      case C:
        return gama;
      default:
        throw new IllegalArgumentException();
    }
  }

}
