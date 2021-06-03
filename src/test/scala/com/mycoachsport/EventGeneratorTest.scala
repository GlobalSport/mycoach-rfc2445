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

import java.time.{DayOfWeek, ZonedDateTime}
import com.mycoachsport.model.DateTimeHelper._
import com.mycoachsport.model._
import org.scalatest.Matchers._
import org.scalatest.WordSpec

class EventGeneratorTest extends WordSpec {

  "Event Generator" should {

    "Generate daily events bound by count" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Daily, Some(10), None),
            Set()
          )
        ) shouldBe (0 until 10).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusDays(n),
          ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusDays(n)
        )
      }.toSet
    }
    "Generate weekly events bound by count" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Weekly, Some(10), None),
            Set()
          )
        ) shouldBe (0 until 10).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusWeeks(n),
          ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusWeeks(n)
        )
      }.toSet
    }

    "Generate yearly events bound by count" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Yearly, Some(10), None),
            Set()
          )
        ) shouldBe (0 until 10).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusYears(n),
          ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusYears(n)
        )
      }.toSet
    }

    "Generate all daily events bound by until" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(
              Freq.Daily,
              None,
              Some(ZonedDateTime.of(2018, 6, 30, 9, 0, 0, 0, UTC))
            ),
            Set()
          )
        ) shouldBe (0 until 10).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusDays(n),
          ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusDays(n)
        )
      }.toSet
    }

    "Generate all daily events bound by until including limit" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(
              Freq.Daily,
              None,
              Some(ZonedDateTime.of(2018, 6, 30, 10, 0, 0, 0, UTC))
            ),
            Set()
          )
        ) shouldBe (0 until 11).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusDays(n),
          ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusDays(n)
        )
      }.toSet
    }

    "Generate all weekly events bound by until" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 14, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 14, 12, 0, 0, 0, UTC),
            Recur(
              Freq.Weekly,
              None,
              Some(ZonedDateTime.of(2018, 6, 29, 0, 0, 0, 0, UTC))
            ),
            Set()
          )
        ) shouldBe (0 until 3).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 14, 10, 0, 0, 0, UTC).plusWeeks(n),
          ZonedDateTime.of(2018, 6, 14, 12, 0, 0, 0, UTC).plusWeeks(n)
        )
      }.toSet
    }

    "Generate all weekly events bound by until including limit" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 14, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 14, 12, 0, 0, 0, UTC),
            Recur(
              Freq.Weekly,
              None,
              Some(ZonedDateTime.of(2018, 6, 29, 10, 0, 0, 0, UTC))
            ),
            Set()
          )
        ) shouldBe (0 until 3).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 14, 10, 0, 0, 0, UTC).plusWeeks(n),
          ZonedDateTime.of(2018, 6, 14, 12, 0, 0, 0, UTC).plusWeeks(n)
        )
      }.toSet
    }

    "Generate all yearly events bound by until" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 14, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 14, 12, 0, 0, 0, UTC),
            Recur(
              Freq.Yearly,
              None,
              Some(ZonedDateTime.of(2020, 6, 14, 0, 0, 0, 0, UTC))
            ),
            Set()
          )
        ) shouldBe (0 until 2).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 14, 10, 0, 0, 0, UTC).plusYears(n),
          ZonedDateTime.of(2018, 6, 14, 12, 0, 0, 0, UTC).plusYears(n)
        )
      }.toSet
    }

    "Generate all yearly events bound by until including limit" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 14, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 14, 12, 0, 0, 0, UTC),
            Recur(
              Freq.Yearly,
              None,
              Some(ZonedDateTime.of(2020, 6, 14, 10, 0, 0, 0, UTC))
            ),
            Set()
          )
        ) shouldBe (0 until 3).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 14, 10, 0, 0, 0, UTC).plusYears(n),
          ZonedDateTime.of(2018, 6, 14, 12, 0, 0, 0, UTC).plusYears(n)
        )
      }.toSet
    }

    "Throw if infitine recursion when generating all events" in {
      intercept[IllegalArgumentException] {
        EventGenerator.generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Daily, None, None),
            Set()
          )
        )
      }
    }

    "Generate only one daily event when the count is 1" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Daily, Some(1), None),
            Set()
          )
        ) shouldBe
        Set(
          Event(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC)
          )
        )
    }

    "Generate only one weekly event when the count is 1" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Weekly, Some(1), None),
            Set()
          )
        ) shouldBe
        Set(
          Event(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC)
          )
        )
    }

    "Generate only one yearly event when the count is 1" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Yearly, Some(1), None),
            Set()
          )
        ) shouldBe
        Set(
          Event(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC)
          )
        )
    }

    "Generate all yearly bound by UNTIL" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 18, 0, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC),
            Recur(
              Freq.Yearly,
              None,
              Some(ZonedDateTime.of(2020, 9, 30, 0, 0, 0, 0, UTC))
            ),
            Set()
          )
        ) shouldBe (0 until 3).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 18, 0, 0, 0, 0, UTC).plusYears(n),
          ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC).plusYears(n)
        )
      }.toSet
    }

    "Generate all yearly events and exclude a set of dates" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 18, 0, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC),
            Recur(
              Freq.Yearly,
              None,
              Some(ZonedDateTime.of(2020, 9, 30, 0, 0, 0, 0, UTC))
            ),
            Set(
              ZonedDateTime.of(2020, 6, 18, 0, 0, 0, 0, UTC),
              ZonedDateTime.of(2019, 6, 18, 0, 0, 0, 0, UTC)
            )
          )
        ) shouldBe (0 until 1).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 18, 0, 0, 0, 0, UTC).plusYears(n),
          ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC).plusYears(n)
        )
      }.toSet

      val startEvent = Event(
        ZonedDateTime.of(2018, 6, 18, 0, 0, 0, 0, UTC),
        ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC)
      )
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 18, 0, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC),
            Recur(Freq.Yearly, Some(3), None),
            Set(
              ZonedDateTime.of(2020, 6, 18, 0, 0, 0, 0, UTC),
              ZonedDateTime.of(2019, 6, 18, 0, 0, 0, 0, UTC)
            )
          )
        ) shouldBe Set(
        startEvent,
        startEvent.copy(
          startDate = startEvent.startDate.plusYears(3),
          endDate = startEvent.endDate.plusYears(3)
        ),
        startEvent.copy(
          startDate = startEvent.startDate.plusYears(4),
          endDate = startEvent.endDate.plusYears(4)
        )
      )
    }

    "Generate all daily events and exclude a set of dates" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 18, 0, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC),
            Recur(
              Freq.Daily,
              None,
              Some(ZonedDateTime.of(2018, 6, 21, 0, 0, 0, 0, UTC))
            ),
            Set(
              ZonedDateTime.of(2018, 6, 20, 0, 0, 0, 0, UTC),
              ZonedDateTime.of(2018, 6, 19, 0, 0, 0, 0, UTC)
            )
          )
        ) shouldBe (0 to 3)
        .map { n =>
          Event(
            ZonedDateTime.of(2018, 6, 18, 0, 0, 0, 0, UTC).plusDays(n),
            ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC).plusDays(n)
          )
        }
        .filter(r =>
          r.startDate.getDayOfMonth != 20 && r.startDate.getDayOfMonth != 19
        )
        .toSet

      val startEvent = Event(
        ZonedDateTime.of(2018, 6, 18, 0, 0, 0, 0, UTC),
        ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC)
      )

      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 18, 0, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC),
            Recur(Freq.Daily, Some(4), None),
            Set(
              ZonedDateTime.of(2018, 6, 20, 0, 0, 0, 0, UTC),
              ZonedDateTime.of(2018, 6, 19, 0, 0, 0, 0, UTC)
            )
          )
        ) shouldBe Set(
        startEvent,
        startEvent.copy(
          startDate = startEvent.startDate.plusDays(3),
          endDate = startEvent.endDate.plusDays(3)
        ),
        startEvent.copy(
          startDate = startEvent.startDate.plusDays(4),
          endDate = startEvent.endDate.plusDays(4)
        ),
        startEvent.copy(
          startDate = startEvent.startDate.plusDays(5),
          endDate = startEvent.endDate.plusDays(5)
        )
      )
    }

    "Generate all weekly events and exlucde a set of dates" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 14, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 14, 12, 0, 0, 0, UTC),
            Recur(
              Freq.Weekly,
              None,
              Some(ZonedDateTime.of(2018, 6, 29, 0, 0, 0, 0, UTC))
            ),
            Set(
              ZonedDateTime.of(2018, 6, 14, 10, 0, 0, 0, UTC),
              ZonedDateTime.of(2018, 6, 21, 10, 0, 0, 0, UTC)
            )
          )
        ) shouldBe (2 until 3).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 14, 10, 0, 0, 0, UTC).plusWeeks(n),
          ZonedDateTime.of(2018, 6, 14, 12, 0, 0, 0, UTC).plusWeeks(n)
        )
      }.toSet

      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 14, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 14, 12, 0, 0, 0, UTC),
            Recur(Freq.Weekly, Some(3), None),
            Set(
              ZonedDateTime.of(2018, 6, 14, 10, 0, 0, 0, UTC),
              ZonedDateTime.of(2018, 6, 21, 10, 0, 0, 0, UTC)
            )
          )
        ) shouldBe (0 to 2).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 28, 10, 0, 0, 0, UTC).plusWeeks(n),
          ZonedDateTime.of(2018, 6, 28, 12, 0, 0, 0, UTC).plusWeeks(n)
        )
      }.toSet
    }

    "Generate weekly events bound by count with byday" in {
      val expected = (0 until 5).flatMap { n =>
        val e = Event(
          ZonedDateTime.of(2018, 6, 18, 10, 0, 0, 0, UTC).plusWeeks(n),
          ZonedDateTime.of(2018, 6, 18, 12, 0, 0, 0, UTC).plusWeeks(n)
        )
        Set(
          e,
          e.copy(
            startDate = e.startDate.plusDays(1),
            endDate = e.endDate.plusDays(1)
          )
        )
      }.toSet

      val actual = EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 18, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 18, 12, 0, 0, 0, UTC),
            Recur(
              Freq.Weekly,
              Some(10),
              None,
              Some(Set(DayOfWeek.MONDAY, DayOfWeek.TUESDAY))
            ),
            Set()
          )
        )
      actual shouldBe expected
    }

    "Generate weekly events bound by count with byday with a full week" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 18, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 18, 12, 0, 0, 0, UTC),
            Recur(
              Freq.Weekly,
              Some(17),
              None,
              Some(
                Set(
                  DayOfWeek.MONDAY,
                  DayOfWeek.TUESDAY,
                  DayOfWeek.WEDNESDAY,
                  DayOfWeek.THURSDAY,
                  DayOfWeek.FRIDAY,
                  DayOfWeek.SATURDAY,
                  DayOfWeek.SUNDAY
                )
              )
            ),
            Set()
          )
        ) shouldBe (0 until 17).map { x =>
        Event(
          ZonedDateTime.of(2018, 6, 18, 10, 0, 0, 0, UTC).plusDays(x),
          ZonedDateTime.of(2018, 6, 18, 12, 0, 0, 0, UTC).plusDays(x)
        )
      }.toSet
    }

    "Generate weekly events bound by count with byday when first occurence is not in the list of days" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 18, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 18, 12, 0, 0, 0, UTC),
            Recur(
              Freq.Weekly,
              Some(10),
              None,
              Some(
                Set(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
              )
            ),
            Set()
          )
        ) shouldBe (0 until 4)
        .flatMap { n =>
          val e = Event(
            ZonedDateTime.of(2018, 6, 19, 10, 0, 0, 0, UTC).plusWeeks(n),
            ZonedDateTime.of(2018, 6, 19, 12, 0, 0, 0, UTC).plusWeeks(n)
          )
          Set(
            e,
            e.copy(
              startDate = e.startDate.plusDays(1),
              endDate = e.endDate.plusDays(1)
            ),
            e.copy(
              startDate = e.startDate.plusDays(3),
              endDate = e.endDate.plusDays(3)
            )
          )
        }
        .slice(0, 10)
        .toSet
    }

    "Generate all weekly events bound by until with byday" in {
      val startEvent = Event(
        ZonedDateTime.of(2018, 6, 18, 10, 0, 0, 0, UTC),
        ZonedDateTime.of(2018, 6, 18, 12, 0, 0, 0, UTC)
      )

      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 18, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 18, 12, 0, 0, 0, UTC),
            Recur(
              Freq.Weekly,
              None,
              Some(ZonedDateTime.of(2018, 7, 5, 0, 0, 0, 0, UTC)),
              Some(Set(DayOfWeek.MONDAY, DayOfWeek.TUESDAY))
            ),
            Set()
          )
        ) shouldBe (0 until 3).flatMap { n =>
        Set(
          startEvent.copy(
            startDate = startEvent.startDate.plusWeeks(n),
            endDate = startEvent.endDate.plusWeeks(n)
          ),
          startEvent.copy(
            startDate = startEvent.startDate.plusDays(1).plusWeeks(n),
            endDate = startEvent.endDate.plusDays(1).plusWeeks(n)
          )
        )
      }.toSet
    }

    "Generate all weekly events bound by until with byday with a full week" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 18, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 18, 12, 0, 0, 0, UTC),
            Recur(
              Freq.Weekly,
              None,
              Some(ZonedDateTime.of(2018, 7, 5, 0, 0, 0, 0, UTC)),
              Some(
                Set(
                  DayOfWeek.MONDAY,
                  DayOfWeek.TUESDAY,
                  DayOfWeek.WEDNESDAY,
                  DayOfWeek.THURSDAY,
                  DayOfWeek.FRIDAY,
                  DayOfWeek.SATURDAY,
                  DayOfWeek.SUNDAY
                )
              )
            ),
            Set()
          )
        ) shouldBe (0 until 17).map { x =>
        Event(
          ZonedDateTime.of(2018, 6, 18, 10, 0, 0, 0, UTC).plusDays(x),
          ZonedDateTime.of(2018, 6, 18, 12, 0, 0, 0, UTC).plusDays(x)
        )
      }.toSet
    }

    "Generate all weekly events bound by until with byday when first occurrence is not in the list of days" in {
      val startEvent = Event(
        ZonedDateTime.of(2018, 6, 19, 10, 0, 0, 0, UTC),
        ZonedDateTime.of(2018, 6, 19, 12, 0, 0, 0, UTC)
      )

      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 18, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 18, 12, 0, 0, 0, UTC),
            Recur(
              Freq.Weekly,
              None,
              Some(ZonedDateTime.of(2018, 7, 5, 0, 0, 0, 0, UTC)),
              Some(Set(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY))
            ),
            Set()
          )
        ) shouldBe (0 until 3).flatMap { n =>
        Set(
          startEvent.copy(
            startDate = startEvent.startDate.plusWeeks(n),
            endDate = startEvent.endDate.plusWeeks(n)
          ),
          startEvent.copy(
            startDate = startEvent.startDate.plusDays(1).plusWeeks(n),
            endDate = startEvent.endDate.plusDays(1).plusWeeks(n)
          )
        )
      }.toSet
    }
  }

}
