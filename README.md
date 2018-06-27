[![Build Status](https://travis-ci.org/GlobalSport/mycoach-rfc2445.svg?branch=develop)](https://travis-ci.org/GlobalSport/mycoach-rfc2445)

# Mycoach-rfc2445
This library is an embryon implementation of the RFC2445, it supports only what we need today and will be improved with
our needs. There is room for improvement in the actual code and in what is supported from the RFC.

Feel free to contribute with PRs we'll be happy to review/merge them.

## Motivation
We wanted a small, Scala based & *java.time* based library which supports generating weekly and daily events within a time range.
We failed to find something small, maintained and not outdated.

## Features
- Parsing [Recurrence rule](https://tools.ietf.org/html/rfc5545#section-3.3.10) and extracting
    - FREQ (only *DAILY* and *WEEKLY* at this time)
    - COUNT
    - UNTIL
- Generating events based on RRULE and EXDATE

## SBT
```scala
libraryDependencies += "com.mycoachsport" %% "mycoach-rfc2445" % "0.0.1"
```

## Samples
```scala
// Generate an event per day from 10 to 12 UTC between the 20th of June and the 25th of June (included)
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
    )
```


## LICENSE
[MIT](LICENSE)