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

import java.time.ZonedDateTime

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
          ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC).plusYears(n),
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
          ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC).plusYears(n),
        )
      }.toSet

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
        ) shouldBe (0 until 1).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 18, 0, 0, 0, 0, UTC).plusYears(n),
          ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC).plusYears(n),
        )
      }.toSet
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
            ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC).plusDays(n),
          )
        }
        .filter(
          r =>
            r.startDate.getDayOfMonth != 20 && r.startDate.getDayOfMonth != 19
        )
        .toSet

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
        ) shouldBe (0 to 3)
        .map { n =>
          Event(
            ZonedDateTime.of(2018, 6, 18, 0, 0, 0, 0, UTC).plusDays(n),
            ZonedDateTime.of(2018, 6, 18, 23, 59, 59, 0, UTC).plusDays(n),
          )
        }
        .filter(
          r =>
            r.startDate.getDayOfMonth != 20 && r.startDate.getDayOfMonth != 19
        )
        .toSet
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
        ) shouldBe (2 until 3).map { n =>
        Event(
          ZonedDateTime.of(2018, 6, 14, 10, 0, 0, 0, UTC).plusWeeks(n),
          ZonedDateTime.of(2018, 6, 14, 12, 0, 0, 0, UTC).plusWeeks(n)
        )
      }.toSet
    }
  }

}
