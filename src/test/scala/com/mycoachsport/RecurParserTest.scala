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

import com.mycoachsport.exceptions.UnsupportedOrMissingFreqException
import com.mycoachsport.model.DateTimeHelper._
import com.mycoachsport.model.Freq
import org.scalatest.Matchers._
import org.scalatest.WordSpec

class RecurParserTest extends WordSpec {

  "RecurParser" should {

    "Parse freq" in {
      RecurParser.parse("RRULE:FREQ=WEEKLY;").freq shouldBe Freq.Weekly
      RecurParser.parse("RRULE:FREQ=DAILY;").freq shouldBe Freq.Daily
      RecurParser.parse("RRULE:FREQ=YEARLY;").freq shouldBe Freq.Yearly
    }

    "Throw when freq is missing" in {
      intercept[UnsupportedOrMissingFreqException] {
        RecurParser.parse("RRULE:FREQ=SECONDLY;")
      }
    }

    "Parse count" in {
      RecurParser.parse("RRULE:FREQ=DAILY;COUNT=10").count shouldBe Some(10)
      RecurParser.parse("RRULE:FREQ=DAILY").count shouldBe None
    }

    "Parse until" in {
      RecurParser
        .parse("RRULE:FREQ=DAILY;UNTIL=19901025T043000Z")
        .until shouldBe Some(ZonedDateTime.of(1990, 10, 25, 4, 30, 0, 0, UTC))

      RecurParser.parse("RRULE:FREQ=DAILY").until shouldBe None

      RecurParser.parse("RRULE:FREQ=DAILY;UNTIL=FOO").until shouldBe None

      RecurParser
        .parse("RRULE:FREQ=DAILY;UNTIL=19901025T0430")
        .until shouldBe None
    }
  }
}
