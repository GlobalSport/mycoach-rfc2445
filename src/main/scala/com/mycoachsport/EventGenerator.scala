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

import java.time.temporal.ChronoUnit
import java.time.{LocalDate, LocalDateTime, ZonedDateTime}

import com.mycoachsport.model.DateTimeHelper._
import com.mycoachsport.model.{DateRange, Event, Freq, RecurringEvent}

/**
  * Event generator methods.
  *
  * Given a date range and a recurring event, generate all the events within the date range accounting for Recurence
  * rule and exclusion dates.
  */
object EventGenerator {

  /**
    * Generates all the events for this `RecurringEvent` definition
    *
    * @param recurringEvent the generator's recurring event
    * @return returns all the recuring events generated
    */
  def generate(recurringEvent: RecurringEvent): Set[Event] = {
    require(
      recurringEvent.rrule.until.isDefined || recurringEvent.rrule.count.isDefined,
      "Generating an infinite number of events is not supported"
    )
    val startDate = recurringEvent.firstOccurenceStartDate
    val endDate = getGeneratorEndDate(recurringEvent)

    val eventDuration: Long = recurringEvent.firstOccurenceStartDate
      .until(recurringEvent.firstOccurenceEndDate, ChronoUnit.NANOS)

    val range = startDate.until(endDate, recurringEvent.rrule.freq)

    (0L to range).flatMap { i =>
      val event = Event(
        ZonedDateTime.of(
          LocalDateTime.of(
            startDate.plus(i, recurringEvent.rrule.freq),
            recurringEvent.firstOccurenceStartDate.toLocalTime
          ),
          recurringEvent.firstOccurenceStartDate.getZone
        ),
        ZonedDateTime.of(
          LocalDateTime.of(
            startDate.plus(i, recurringEvent.rrule.freq),
            recurringEvent.firstOccurenceStartDate
              .plus(eventDuration, ChronoUnit.NANOS)
              .toLocalTime
          ),
          recurringEvent.firstOccurenceEndDate.getZone
        )
      )

      recurringEvent.exdates
        .exists(_.isEqual(event.startDate)) match {
        case true =>
          None
        case false =>
          Some(event)
      }
    }.toSet
  }

  private def getGeneratorEndDate(recurringEvent: RecurringEvent) = {
    val recurringEventFinalDate = (
      recurringEvent.rrule.freq,
      recurringEvent.rrule.count,
      recurringEvent.rrule.until
    ) match {
      case (Freq.Daily, Some(count), None) =>
        recurringEvent.firstOccurenceEndDate.plusDays(count - 1)
      case (Freq.Weekly, Some(count), None) =>
        recurringEvent.firstOccurenceEndDate.plusWeeks(count - 1)
      case (Freq.Yearly, Some(count), None) =>
        recurringEvent.firstOccurenceEndDate.plusYears(count - 1)
      case (_, None, Some(finishDate)) =>
        finishDate
      case _ =>
        throw new IllegalArgumentException(
          "Will not happen until Recur has the require"
        )
    }

    recurringEventFinalDate
  }

  private implicit def FreqToTemporalUnit(freq: Freq.Value): ChronoUnit =
    freq match {
      case Freq.Daily =>
        ChronoUnit.DAYS
      case Freq.Weekly =>
        ChronoUnit.WEEKS
      case Freq.Yearly =>
        ChronoUnit.YEARS
    }
}
