/*
 * Copyright (c) 2017 Johannes Kohnen <wjkohnen@users.noreply.github.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ko_sys.av.airac;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class AiracFromInstantTest {

	private final String date;
	private final int expectYear;
	private final int expectOrdinal;

	public AiracFromInstantTest(String date, int expectYear, int expectOrdinal) {
		this.date = date;
		this.expectYear = expectYear;
		this.expectOrdinal = expectOrdinal;
	}

	@NotNull
	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				/*
						ICAO DOC 8126, 6th edition (2003); Paragraph 2.6.2 b):
                                the AIRAC effective dates must be in accordance
                                with the predetermined, internationally agreed
                                schedule of effective dates based on an interval of
                                28 days, including 29 January 1998
                */
				{"1998-01-29", 1998, 2},

                /*
						ICAO DOC 8126, 6th edition (2003); table 2-1 "Schedule of AIRAC effective date 2003-2012"
                */
				// first airac of the year and its predecessor
				{"2003-01-23", 2003, 1},

				{"2004-01-21", 2003, 13},
				{"2004-01-22", 2004, 1},

				{"2005-01-19", 2004, 13},
				{"2005-01-20", 2005, 1},

				{"2006-01-18", 2005, 13},
				{"2006-01-19", 2006, 1},

				{"2007-01-17", 2006, 13},
				{"2007-01-18", 2007, 1},

				{"2008-01-16", 2007, 13},
				{"2008-01-17", 2008, 1},

				{"2009-01-14", 2008, 13},
				{"2009-01-15", 2009, 1},

				{"2010-01-13", 2009, 13},
				{"2010-01-14", 2010, 1},

				{"2011-01-12", 2010, 13},
				{"2011-01-13", 2011, 1},

				{"2012-01-11", 2011, 13},
				{"2012-01-12", 2012, 1},

                /*
						http://www.eurocontrol.int/articles/airac-adherence-monitoring-phase-1-p-03
                */
				// first airac of the expectYear and its predecessor (2013 2020)
				{"2013-01-09", 2012, 13},
				{"2013-01-10", 2013, 1},

				{"2014-01-08", 2013, 13},
				{"2014-01-09", 2014, 1},

				{"2015-01-07", 2014, 13},
				{"2015-01-08", 2015, 1},

				{"2016-01-06", 2015, 13},
				{"2016-01-07", 2016, 1},

				{"2017-01-04", 2016, 13},
				{"2017-01-05", 2017, 1},

				{"2018-01-03", 2017, 13},
				{"2018-01-04", 2018, 1},

				{"2019-01-02", 2018, 13},
				{"2019-01-03", 2019, 1},

				{"2020-01-01", 2019, 13},
				{"2020-01-02", 2020, 1},

				// the 'special' one:
				{"2020-12-30", 2020, 13},
				{"2020-12-31", 2020, 14},

				// calculated manually
				{"2021-01-27", 2020, 14},
				{"2021-01-28", 2021, 1},
				{"2003-01-22", 2002, 13},
				{"1964-01-16", 1964, 1},
				{"1901-01-10", 1901, 1},
				{"1998-12-31", 1998, 14},

				{"1963-12-31", 1963, 13}
		});
	}

	static Instant instantFromIsoDate(@NotNull String date) {
		// Now this is a whole lot more complicated than I thought it would be.
		TemporalAccessor parse = DateTimeFormatter.ISO_DATE.parse(date);
		LocalDate localDate = LocalDate.from(parse);
		ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneOffset.UTC);
		return Instant.from(zonedDateTime);
	}

	@Test
	public void fromInstantTest() {
		String msg = String.format("test of {\"%s\", %d, %d}", date, expectYear, expectOrdinal);
		System.out.println(msg);

		Instant instant = instantFromIsoDate(date);
		Airac actual = Airac.fromInstant(instant);

		assertEquals(msg, expectYear, actual.getYear());
		assertEquals(msg, expectOrdinal, actual.getOrdinal());
	}


}
