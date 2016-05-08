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
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class AiracNextPreviousTest {

	private final String date;
	private final int previousYear;
	private final int previousOrdinal;
	private final int nextYear;
	private final int nextOrdinal;

	public AiracNextPreviousTest(String date, int previousYear, int previousOrdinal, int nextYear, int nextOrdinal) {
		this.date = date;
		this.previousYear = previousYear;
		this.previousOrdinal = previousOrdinal;
		this.nextYear = nextYear;
		this.nextOrdinal = nextOrdinal;
	}

	@NotNull
	@Parameterized.Parameters
	public static Collection<Object[]> date() {
		return Arrays.asList(new Object[][]{
				{"2006-01-20", 2005, 13, 2006, 2},
				{"2021-01-01", 2020, 13, 2021, 1},
		});
	}

	@Test
	public void TestPreviousNext() {
		String msg = String.format("test of {\"%s\", %d, %d, %d, %d}",
				date, previousYear, previousOrdinal, nextYear, nextOrdinal);
		System.out.println(msg);

		Instant instant = AiracFromInstantTest.instantFromIsoDate(date);
		Airac got = Airac.fromInstant(instant);
		Airac gotPrevious = got.previous();
		Airac gotNext = got.next();

		assertEquals(msg, previousYear, gotPrevious.year());
		assertEquals(msg, previousOrdinal, gotPrevious.ordinal());
		assertEquals(msg, nextYear, gotNext.year());
		assertEquals(msg, nextOrdinal, gotNext.ordinal());
	}
}
