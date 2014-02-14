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

package atatec.robocode.parts;

/**
 * Represents a storage for values that must be retain
 * across the battles
 *
 * @author Marcelo Guimarães
 */
public interface Storage {

  /**
   * Retrieves the value mapped by the given key
   *
   * @param name the key
   * @return the value mapped by the given key
   */
  <E> E retrieve(String name);

  /**
   * Maps a value to a given key and stores it
   *
   * @param name  the key associated with the value
   * @param value the value to store
   */
  void store(String name, Object value);

  /**
   * Removes the mapped value
   *
   * @param name
   */
  void remove(String name);

  /**
   * Checks if the storage has a value mapped to a
   * given key
   *
   * @param name the key to search
   * @return <code>true</code> if there is a value
   *         mapped for the given key
   */
  boolean hasValueFor(String name);

}
