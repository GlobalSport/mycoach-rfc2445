# Mycoach-rfc2445
This library is an embryon implementation of the RFC2445, it supports only what we need today and will be improved with
our needs. There is room for improvement in the actual code and in what is supported from the RFC.

Feel free to contribute with PRs we'll be happy to review/merge them.

## Motivation
We wanted a small & *java.time* based library which supports generating weekly and daily events within a time range.
We failed to find something small, maintained and not outdated.

## Features
- Parsing [Recurrence rule](https://tools.ietf.org/html/rfc5545#section-3.3.10) and extracting
    - FREQ (only *DAILY* and *WEEKLY* at this time
    - COUNT
    - UNTIL
- Generating events based on RRULE and EXDATE

## LICENSE
[MIT](LICENSE)