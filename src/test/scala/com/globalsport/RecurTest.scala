/*
 * Copyright 2018 Globalsport
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.globalsport

import java.time.ZonedDateTime

import com.globalsport.model.DateTimeHelper._
import com.globalsport.model.{Freq, Recur}
import org.scalatest.Matchers._
import org.scalatest.WordSpec

class RecurTest extends WordSpec {

  "Recur" should {
    "Produce a valid RECUR rule" in {
      Recur(Freq.Weekly, None, None).toString shouldBe "FREQ=WEEKLY"
      Recur(Freq.Daily, None, None).toString shouldBe "FREQ=DAILY"
      Recur(Freq.Daily, Some(10), None).toString shouldBe "FREQ=DAILY;COUNT=10"
      Recur(Freq.Daily, None, Some(ZonedDateTime.of(2018, 6, 26, 14, 0, 0, 0, UTC))).toString shouldBe "FREQ=DAILY;UNTIL=20180626T140000Z"
    }

    "Throw when used with both count and until" in {
      the[IllegalArgumentException] thrownBy
        (Recur(Freq.Weekly, Some(10), Some(ZonedDateTime.now(UTC)))) should have message
        "requirement failed: 'count' and 'until' are mutually exclusive parameters"
    }
  }
}
