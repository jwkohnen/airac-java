# airac-java: a small Java library for calculating aviation AIRAC cycle dates

[![Apache License v2.0](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)
[![Build Status](https://travis-ci.org/wjkohnen/airac-java.svg?branch=master)](https://travis-ci.org/wjkohnen/airac-java)
[![Code Coverage](https://codecov.io/gh/wjkohnen/airac-java/branch/master/graph/badge.svg)](https://codecov.io/gh/wjkohnen/airac-java)

Package airac-java provides calculations on Aeronautical Information Regulation And
Control (AIRAC) cycles, i.e. cycle identifiers and effective calendar dates.

Regular, planned Aeronautical Information Publications (AIP) as defined by the
International Civil Aviation Organization (ICAO) are published and become
effective at fixed dates. This package implements the AIRAC cycle definition as
published in the ICAO Aeronautical Information Services Manual (DOC 8126;
AN/872; 6th Edition; 2003). Test cases validate documented dates from 1998 until
2020, including the rare cases of a 14th cycle, e. g. in the years 1976, 1998, 2020
and 2043.

## Example

```
Instant shalom = Instant.from(ZonedDateTime.of(2012, 8, 26, 0, 0, 0, 0, ZoneOffset.UTC));
String output = String.format("At %s the current AIRAC cycle was %s.\n",
    DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC).format(shalom),
    Airac.fromInstant(shalom).toLongString());
System.out.println(output);

// Output:
// At 2012-08-26 the current AIRAC cycle was 1209 (effective: 2012-08-23; expires: 2012-09-19).
```

## Maven Central
```
<dependency>
    <groupId>com.ko-sys.av</groupId>
    <artifactId>airac</artifactId>
    <version> <!-- semantic version --> </version>
</dependency>
```

## See also
This is a port of my [go library](https://github.com/wjkohnen/airac/). I did this
port basically in order to learn how to use JSR-310 and parametrized JUnit tests.

## License
Licensed under the Apache License, Version 2.0.

## Wikipedia

Article on AIP / AIRAC cycles: https://en.wikipedia.org/wiki/Aeronautical_Information_Publication

There are wiki macros in that article that do basically the same thing as this
library. Though, this library does not trip over the case of 14 cycles per year
(e. g. 1998 and 2020).
