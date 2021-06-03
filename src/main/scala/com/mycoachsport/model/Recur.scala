/*
 * Copyright 2018 Globalsport SAS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mycoachsport.model

import java.time.{DayOfWeek, ZonedDateTime}
import com.mycoachsport.model.DateTimeHelper._

/**
  * A Recurrence rule.
  *
  * The `count` and `until` parameters are mutually exclusives.
  *
  * @param freq  the frequence of this recurrence rule
  * @param count the number of occurence of the reccurence
  * @param until the date until this recurrence rule should apply
  * @throws IllegalArgumentException when both `until` and `count` parameters are set to `Some`
  */
case class Recur(
    freq: Freq.Value,
    count: Option[Long],
    until: Option[ZonedDateTime],
    byday: Option[Set[DayOfWeek]] = None
) {
  require(
    !(count.isDefined && until.isDefined),
    "'count' and 'until' are mutually exclusive parameters"
  )

  /**
    * Returns the corresponding RECUR string representation
    */
  override def toString: String = {
    Seq(
      Some(s"FREQ=${freq.toString}"),
      count.map(c => s"COUNT=$c"),
      until.map(u => s"UNTIL=${u.format(ICalUtcDateTimeFormat)}"),
      byday.map {
        case x if x.nonEmpty =>
          s"BYDAY=${x.map(dayOfWeekToString).mkString(",")}"
        case _ =>
          ""
      }
    ).flatten.mkString(";")
  }

  private def dayOfWeekToString: DayOfWeek => String = {
    case DayOfWeek.MONDAY =>
      "MO"
    case DayOfWeek.TUESDAY =>
      "TU"
    case DayOfWeek.WEDNESDAY =>
      "WE"
    case DayOfWeek.THURSDAY =>
      "TH"
    case DayOfWeek.FRIDAY =>
      "FR"
    case DayOfWeek.SATURDAY =>
      "SA"
    case DayOfWeek.SUNDAY =>
      "SU"
  }
}
