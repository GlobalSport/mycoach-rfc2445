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

import com.mycoachsport.model.DateTimeHelper._
import com.mycoachsport.model.{Event, Freq, RecurringEvent}

import java.time.temporal.{ChronoUnit, TemporalAdjusters}
import java.time.{LocalDateTime, ZonedDateTime}

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

    val eventDuration: Long = recurringEvent.firstOccurenceStartDate
      .until(recurringEvent.firstOccurenceEndDate, ChronoUnit.NANOS)

    val events = Stream
      .from(0)
      .flatMap { i =>
        val eventStart = startDate.plus(i, recurringEvent.rrule.freq)
        val event =
          Event(
            ZonedDateTime.of(
              LocalDateTime.of(
                eventStart,
                recurringEvent.firstOccurenceStartDate.toLocalTime
              ),
              recurringEvent.firstOccurenceStartDate.getZone
            ),
            ZonedDateTime.of(
              LocalDateTime.of(
                eventStart,
                recurringEvent.firstOccurenceStartDate
                  .plus(eventDuration, ChronoUnit.NANOS)
                  .toLocalTime
              ),
              recurringEvent.firstOccurenceEndDate.getZone
            )
          )

        val events = recurringEvent.rrule.byday match {
          case None =>
            List(event)
          case Some(x) if x.isEmpty =>
            List(event)
          case Some(x) =>
            x.map { day =>
              val realStart = if (day == eventStart.getDayOfWeek) {
                eventStart
              } else {
                eventStart.`with`(TemporalAdjusters.next(day))
              }

              Event(
                ZonedDateTime.of(
                  LocalDateTime.of(
                    realStart,
                    recurringEvent.firstOccurenceStartDate.toLocalTime
                  ),
                  recurringEvent.firstOccurenceStartDate.getZone
                ),
                ZonedDateTime.of(
                  LocalDateTime.of(
                    realStart,
                    recurringEvent.firstOccurenceStartDate
                      .plus(eventDuration, ChronoUnit.NANOS)
                      .toLocalTime
                  ),
                  recurringEvent.firstOccurenceEndDate.getZone
                )
              )
            }
        }

        events.filterNot(e =>
          recurringEvent.exdates
            .exists(_.isEqual(e.startDate))
        )
      }

    if (recurringEvent.rrule.count.isDefined) {
      events.take(recurringEvent.rrule.count.get.toInt).toSet
    } else {
      val limit = recurringEvent.rrule.until.get
      events
        .takeWhile(x =>
          x.startDate.isBefore(limit) || x.startDate.isEqual(limit)
        )
        .toSet
    }
  }

  case class EventAndSize(events: List[Event], overallSize: Int)

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
