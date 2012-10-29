package atatec.robocode.event;

import robocode.Bullet;

/** @author Marcelo Varella Barca Guimarães */
public class BulletFiredEvent {

  private final Bullet bullet;

  public BulletFiredEvent(Bullet bullet) {
    this.bullet = bullet;
  }

  public Bullet bullet() {
    return bullet;
  }

}
