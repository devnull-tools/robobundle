/************************************************************************************
 * The MIT License                                                                  *
 *                                                                                  *
 * Copyright (c) 2013 Marcelo Guimarães <ataxexe at gmail dot com>                  *
 * -------------------------------------------------------------------------------- *
 * Permission  is hereby granted, free of charge, to any person obtaining a copy of *
 * this  software  and  associated documentation files (the "Software"), to deal in *
 * the  Software  without  restriction,  including without limitation the rights to *
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of *
 * the  Software, and to permit persons to whom the Software is furnished to do so, *
 * subject to the following conditions:                                             *
 *                                                                                  *
 * The  above  copyright notice and this permission notice shall be included in all *
 * copies or substantial portions of the Software.                                  *
 *                            --------------------------                            *
 * THE  SOFTWARE  IS  PROVIDED  "AS  IS",  WITHOUT WARRANTY OF ANY KIND, EXPRESS OR *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS *
 * FOR  A  PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR *
 * COPYRIGHT  HOLDERS  BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER *
 * IN  AN  ACTION  OF  CONTRACT,  TORT  OR  OTHERWISE,  ARISING  FROM, OUT OF OR IN *
 * CONNECTION  WITH  THE  SOFTWARE  OR  THE  USE OR OTHER DEALINGS IN THE SOFTWARE. *
 ************************************************************************************/

package atatec.robocode.util;

import atatec.robocode.ConditionalCommand;
import atatec.robocode.parts.AimingSystem;
import atatec.robocode.parts.FiringSystem;
import atatec.robocode.parts.MovingSystem;

/** @author Marcelo Guimarães */
public class Systems {

  private Systems() {
  }

  public static AimingSystem asAimingSystem(final ConditionalCommand<AimingSystem> system) {
    return new AimingSystem() {
      @Override
      public void execute() {
        system.execute();
      }
    };
  }

  public static FiringSystem asFiringSystem(final ConditionalCommand<FiringSystem> system) {
    return new FiringSystem() {
      @Override
      public void execute() {
        system.execute();
      }

      @Override
      public double firePower() {
        FiringSystem activated = system.activated();
        return activated != null ? activated.firePower() : 0;
      }
    };
  }

  public static MovingSystem asMovingSystem(final ConditionalCommand<MovingSystem> system) {
    return new MovingSystem() {
      @Override
      public void execute() {
        system.execute();
      }
    };
  }

}
