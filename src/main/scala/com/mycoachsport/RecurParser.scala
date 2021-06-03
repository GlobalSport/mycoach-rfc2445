/*
 * Copyright 2018 Globalsport SAS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mycoachsport

import java.time.{DayOfWeek, LocalDateTime, ZonedDateTime}
import com.mycoachsport.exceptions.UnsupportedOrMissingFreqException
import com.mycoachsport.model.DateTimeHelper._
import com.mycoachsport.model.{Freq, Recur}

/**
  * Recurrence rule parser.
  *
  * Parse a string like "FREQ=DAILY;COUNT=10" to a `Recur` instance.
  *
  * Supports:
  * <ul>
  * <li>FREQ(WEEKLY|DAILY)</li>
  * <li>COUNT</li>
  * <li>UNTIL</li>
  * <li>BYDAY</li>
  * </ul>
  *
  * The parser uses regex to extract the `Recur` fields.
  */
object RecurParser {

  private val FreqPattern = ".*FREQ=(DAILY|WEEKLY|YEARLY).*".r
  private val CountPattern = ".*COUNT=(\\d+).*".r
  private val UntilPattern = ".*UNTIL=([0-9]{8}T[0-9]{6}Z).*".r
  private val ByDayPattern = ".*BYDAY=(((SU|MO|TU|WE|TH|FR|SA),*)+).*".r

  /**
    * Returns a `Recur` instance corresponding to the string representation
    *
    * @param str the recurrence's rule string representation
    * @return returns a Recur matching the string rule
    */
  def parse(str: String): Recur = {
    Recur(parseFreq(str), parseCount(str), parseUntil(str), parseByDay(str))
  }

  private def parseFreq(str: String): Freq.Value = {
    str match {
      case FreqPattern(frequency) =>
        Freq.withName(frequency)
      case _ =>
        throw new UnsupportedOrMissingFreqException
    }
  }

  private def parseCount(str: String): Option[Long] = {
    str match {
      case CountPattern(number) =>
        Some(number.toLong)
      case _ =>
        None
    }
  }

  private def parseUntil(str: String): Option[ZonedDateTime] = {
    str match {
      case UntilPattern(date) =>
        Some(LocalDateTime.parse(date, ICalUtcDateTimeFormat).atZone(UTC))
      case _ =>
        None
    }
  }

  private def parseByDay(str: String): Option[Set[DayOfWeek]] = {
    val matches =
      ByDayPattern.findAllMatchIn(str).map(_.group(1)).toList.headOption

    matches match {
      case Some(days) =>
        Some(
          days
            .split(",")
            .map(toDayOfWeek)
            .toSet
        )
      case _ =>
        None
    }
  }

  private def toDayOfWeek: String => DayOfWeek = {
    case "SU" =>
      DayOfWeek.SUNDAY
    case "MO" =>
      DayOfWeek.MONDAY
    case "TU" =>
      DayOfWeek.TUESDAY
    case "WE" =>
      DayOfWeek.WEDNESDAY
    case "TH" =>
      DayOfWeek.THURSDAY
    case "FR" =>
      DayOfWeek.FRIDAY
    case "SA" =>
      DayOfWeek.SATURDAY
  }
}
