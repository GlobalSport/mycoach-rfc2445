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

import com.mycoachsport.model.{DateRange, Event, Freq, RecurringEvent}
import com.mycoachsport.model.DateTimeHelper._

/**
  * Event generator methods.
  *
  * Given a date range and a recurring event, generate all the events within the date range accounting for Recurence
  * rule and exclusion dates.
  */
object EventGenerator {

  /**
    * Generates all the events during the given `dateRange` based on the `recurringEvent`
    *
    * @param dateRange      the generator's date range
    * @param recurringEvent the generator's recurring event
    * @return returns the events for this `dateRange`
    */
  def generate(dateRange: DateRange, recurringEvent: RecurringEvent): Set[Event] =
    generate(Some(dateRange), recurringEvent)

  /**
    * Generates all the events for this `RecurringEvent` definition
    *
    * @param recurringEvent the generator's recurring event
    * @return returns all the recuring events generated
    */
  def generate(recurringEvent: RecurringEvent): Set[Event] = {
    require(recurringEvent.rrule.until.isDefined || recurringEvent.rrule.count.isDefined, "Generating an infinite number of events is not supported")
    generate(None, recurringEvent)
  }

  private def generate(dateRange: Option[DateRange], recurringEvent: RecurringEvent): Set[Event] = {
    val startDate = getGeneratorStartDate(dateRange, recurringEvent)
    val endDate = getGeneratorEndDate(dateRange, recurringEvent)

    val eventDuration: Long = recurringEvent.firstOccurenceStartDate.until(recurringEvent.firstOccurenceEndDate, ChronoUnit.NANOS)

    val range = startDate.until(endDate, recurringEvent.rrule.freq)

    (0L to range).flatMap {
      i =>
        val event = Event(
          ZonedDateTime.of(
            LocalDateTime.of(startDate.plus(i, recurringEvent.rrule.freq), recurringEvent.firstOccurenceStartDate.toLocalTime),
            recurringEvent.firstOccurenceStartDate.getZone
          ),
          ZonedDateTime.of(
            LocalDateTime.of(startDate.plus(i, recurringEvent.rrule.freq), recurringEvent.firstOccurenceStartDate.plus(eventDuration, ChronoUnit.NANOS).toLocalTime),
            recurringEvent.firstOccurenceEndDate.getZone
          )
        )

        recurringEvent
          .exdates
          .contains(event.startDate) match {
          case true =>
            None
          case false =>
            Some(event)
        }
    }.toSet
  }

  private def getGeneratorStartDate(dateRange: Option[DateRange], recurringEvent: RecurringEvent): LocalDate = {
    dateRange match {
      case Some(dr) =>
        dr.startDate isAfter recurringEvent.firstOccurenceStartDate match {
          case true =>
            dr.startDate
          case false =>
            recurringEvent.firstOccurenceStartDate
        }
      case None =>
        recurringEvent.firstOccurenceStartDate
    }
  }

  private def getGeneratorEndDate(dateRange: Option[DateRange], recurringEvent: RecurringEvent): LocalDate = {
    val recurringEventFinalDate = (recurringEvent.rrule.freq, recurringEvent.rrule.count, recurringEvent.rrule.until) match {
      case (Freq.Daily, Some(count), None) =>
        recurringEvent.firstOccurenceEndDate.plusDays(count)
      case (Freq.Weekly, Some(count), None) =>
        recurringEvent.firstOccurenceEndDate.plusWeeks(count)
      case (_, None, Some(finishDate)) =>
        finishDate
      case _ =>
        throw new IllegalArgumentException("Will not happen until Recur has the require")
    }


    dateRange match {
      case Some(dr) =>
        dr.endDate isAfter recurringEventFinalDate match {
          case true =>
            recurringEventFinalDate
          case false =>
            dr.endDate
        }
      case None =>
        recurringEventFinalDate
    }

  }

  private implicit def FreqToTemporalUnit(freq: Freq.Value): ChronoUnit = freq match {
    case Freq.Daily =>
      ChronoUnit.DAYS
    case Freq.Weekly =>
      ChronoUnit.WEEKS
  }
}
