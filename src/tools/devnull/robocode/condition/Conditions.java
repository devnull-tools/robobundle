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

package tools.devnull.robocode.condition;

/** @author Marcelo Guimarães */
public class Conditions {

  public static final Condition ALWAYS = new Condition() {

    @Override
    public boolean evaluate() {
      return true;
    }
  };

  public static final Condition NEVER = new Condition() {

    @Override
    public boolean evaluate() {
      return false;
    }
  };

  public static Condition all(final Condition... conditions) {
    return new Condition() {
      @Override
      public boolean evaluate() {
        for (Condition condition : conditions) {
          if (!condition.evaluate()) {
            return false;
          }
        }
        return true;
      }
    };
  }

  public static Condition none(final Condition... conditions) {
    return new Condition() {
      @Override
      public boolean evaluate() {
        for (Condition condition : conditions) {
          if (condition.evaluate()) {
            return false;
          }
        }
        return true;
      }
    };
  }

  public static Condition any(final Condition... conditions) {
    return new Condition() {
      @Override
      public boolean evaluate() {
        for (Condition condition : conditions) {
          if (condition.evaluate()) {
            return true;
          }
        }
        return false;
      }
    };
  }

  public static Condition not(final Condition condition) {
    return new Condition() {
      @Override
      public boolean evaluate() {
        return !condition.evaluate();
      }
    };
  }

  public static Condition adapt(final robocode.Condition robocodeCondition) {
    return new Condition() {
      @Override
      public boolean evaluate() {
        return robocodeCondition.test();
      }
    };
  }

  public static robocode.Condition adapt(final Condition condition) {
    return new robocode.Condition() {
      @Override
      public boolean test() {
        return condition.evaluate();
      }
    };
  }

}
