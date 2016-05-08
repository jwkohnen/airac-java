/*
 *  Copyright (C) 2016 Wolfgang Johannes Kohnen <wjkohnen@users.noreply.github.com>
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
	private final int year;
	private final int ordinal;

	public AiracFromInstantTest(String date, int year, int ordinal) {
		this.date = date;
		this.year = year;
		this.ordinal = ordinal;
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
				// first airac of the year and its predecessor (2013 2020)
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
				{"1998-12-31", 1998, 14}
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
		String msg = String.format("test of {\"%s\", %d, %d}", date, year, ordinal);
		System.out.println(msg);

		Instant instant = instantFromIsoDate(date);
		Airac got = Airac.fromInstant(instant);

		assertEquals(msg, got.year(), year);
		assertEquals(msg, got.ordinal(), ordinal);
	}

}
