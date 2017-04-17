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

package tools.devnull.robobundle.event;

/** @author Marcelo Guimarães */
public interface Events {

  public static final String ENEMY_SCANNED = "ENEMY_SCANNED";
  public static final String HIT_BY_BULLET = "HIT_BY_BULLET";
  public static final String BULLET_HIT = "BULLET_HIT";
  public static final String BULLET_HIT_BULLET = "BULLET_HIT_BULLET";
  public static final String BULLET_MISSED = "BULLET_MISSED";
  public static final String ROBOT_DEATH = "ROBOT_DEATH";
  public static final String HIT_ROBOT = "HIT_ROBOT";
  public static final String HIT_WALL = "HIT_WALL";
  public static final String PAINT = "PAINT";
  public static final String DRAW = "DRAW";
  public static final String NEXT_TURN = "NEXT_TURN";
  public static final String BULLET_FIRED = "BULLET_FIRED";
  public static final String WIN = "WIN";
  public static final String ENEMY_FIRE = "ENEMY_FIRE";
  public static final String ROUND_STARTED = "ROUND_STARTED";
  public static final String ROUND_ENDED = "ROUND_ENDED";
  public static final String BATTLE_ENDED = "BATTLE_ENDED";
  public static final String DEATH = "DEATH";
  public static final String NEAR_TO_WALL = "NEAR_TO_WALL";
  public static final String NEAR_TO_ENEMY = "NEAR_TO_ENEMY";
  public static final String BULLET_NOT_FIRED = "BULLET_NOT_FIRED";
  public static final String TARGET_UNSET = "TARGET_UNSET";
  public static final String TARGET_SET = "TARGET_SET";
  public static final String GUN_AIMED = "GUN_AIMED"; //gun aimed successfully

}
