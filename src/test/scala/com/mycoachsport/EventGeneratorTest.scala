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

    "Generate all daily events within a time range bound by UNTIL" in {
      EventGenerator
        .generate(
          DateRange(
            ZonedDateTime.of(2018, 6, 18, 0, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 7, 30, 0, 0, 0, 0, UTC)
          ), RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Daily, None, Some(ZonedDateTime.of(2018, 6, 25, 0, 0, 0, 0, UTC))),
            Set()
          )
        ) shouldBe (0 to 5).map {
        n =>
          Event(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusDays(n),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusDays(n)
          )
      }.toSet
    }

    "Generate all daily events within a time range bound by generator end date" in {
      EventGenerator
        .generate(
          DateRange(
            ZonedDateTime.of(2018, 6, 20, 0, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 27, 0, 0, 0, 0, UTC)
          ),
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Daily, Some(10), None),
            Set())
        ) shouldBe (0 to 7).map {
        n =>
          Event(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusDays(n),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusDays(n)
          )
      }.toSet

      EventGenerator
        .generate(
          DateRange(
            ZonedDateTime.of(2018, 6, 20, 0, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 27, 0, 0, 0, 0, UTC)
          ),
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Daily, None, Some(ZonedDateTime.of(2018, 12, 1, 0, 0, 0, 0, UTC))),
            Set()
          )
        ) shouldBe (0 to 7).map {
        n =>
          Event(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusDays(n),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusDays(n)
          )
      }.toSet
    }

    "Generate all daily events" in {
      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Daily, Some(10), None),
            Set())
        ) shouldBe (0 until 10).map {
        n =>
          Event(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusDays(n),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusDays(n)
          )
      }.toSet

      EventGenerator
        .generate(
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Daily, None, Some(ZonedDateTime.of(2018, 6, 30, 0, 0, 0, 0, UTC))),
            Set()
          )
        ) shouldBe (0 until 11).map {
        n =>
          Event(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusDays(n),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusDays(n)
          )
      }.toSet
    }

    "Throw if infitine recursion when generating all events" in {
      intercept[IllegalArgumentException] {
        EventGenerator.generate(RecurringEvent(
          ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
          ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
          Recur(Freq.Daily, None, None),
          Set()))
      }
    }

    "Generate all daily events within a time range bound by COUNT" in {
      EventGenerator
        .generate(
          DateRange(
            ZonedDateTime.of(2018, 6, 20, 0, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 9, 30, 0, 0, 0, 0, UTC)
          ),
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 18, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 18, 12, 0, 0, 0, UTC),
            Recur(Freq.Daily, Some(10), None),
            Set())
        ) shouldBe (0 until 8).map {
        n =>
          Event(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusDays(n),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusDays(n)
          )
      }.toSet
    }

    "Generate all weekly events within a time range bound by COUNT" in {
      EventGenerator
        .generate(
          DateRange(
            ZonedDateTime.of(2018, 6, 20, 0, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 8, 28, 0, 0, 0, 0, UTC)
          ),
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Weekly, Some(4), None),
            Set())
        ) shouldBe (0 until 4).map {
        n =>
          Event(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusWeeks(n),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusWeeks(n)
          )
      }.toSet
    }

    "Generate all weekly events within a time range bound by end date" in {
      EventGenerator
        .generate(
          DateRange(
            ZonedDateTime.of(2018, 6, 20, 0, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 7, 11, 0, 0, 0, 0, UTC)
          ),
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Weekly, Some(10), None),
            Set())
        ) shouldBe (0 until 4).map {
        n =>
          Event(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusWeeks(n),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusWeeks(n)
          )
      }.toSet

      EventGenerator
        .generate(
          DateRange(
            ZonedDateTime.of(2018, 6, 20, 0, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 7, 11, 0, 0, 0, 0, UTC)),
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Weekly, None, Some(ZonedDateTime.of(2018, 12, 1, 0, 0, 0, 0, UTC))),
            Set())
        ) shouldBe (0 until 4).map {
        n =>
          Event(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusWeeks(n),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusWeeks(n)
          )
      }.toSet
    }

    "Generate all weekly events within a time range bound by UNTIL" in {
      EventGenerator
        .generate(
          DateRange(
            ZonedDateTime.of(2018, 6, 20, 0, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 7, 25, 0, 0, 0, 0, UTC)),
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Weekly, None, Some(ZonedDateTime.of(2018, 7, 4, 0, 0, 0, 0, UTC))),
            Set())
        ) shouldBe (0 until 3).map {
        n =>
          Event(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC).plusWeeks(n),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC).plusWeeks(n)
          )
      }.toSet
    }

    "Generate all weekly events within a time range and exludes a set of dates" in {
      EventGenerator
        .generate(
          DateRange(
            ZonedDateTime.of(2018, 6, 20, 0, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 7, 25, 0, 0, 0, 0, UTC)),
          RecurringEvent(
            ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
            ZonedDateTime.of(2018, 6, 20, 12, 0, 0, 0, UTC),
            Recur(Freq.Weekly, None, Some(ZonedDateTime.of(2018, 7, 18, 0, 0, 0, 0, UTC))),
            Set(
              ZonedDateTime.of(2018, 6, 20, 10, 0, 0, 0, UTC),
              ZonedDateTime.of(2018, 7, 4, 10, 0, 0, 0, UTC)
            )
          )
        ) shouldBe Set(
        Event(
          ZonedDateTime.of(2018, 6, 27, 10, 0, 0, 0, UTC),
          ZonedDateTime.of(2018, 6, 27, 12, 0, 0, 0, UTC)
        ),
        Event(
          ZonedDateTime.of(2018, 7, 11, 10, 0, 0, 0, UTC),
          ZonedDateTime.of(2018, 7, 11, 12, 0, 0, 0, UTC)
        ),
        Event(
          ZonedDateTime.of(2018, 7, 18, 10, 0, 0, 0, UTC),
          ZonedDateTime.of(2018, 7, 18, 12, 0, 0, 0, UTC)
        )
      )
    }

    "Generate only one event when the count is 1" in {
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

  }

}
