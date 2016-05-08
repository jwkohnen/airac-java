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

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class AiracFromIdentifierTest {
	private final String id;
	private final String effective;
	private final int year;
	private final int ordinal;
	private final boolean ok;

	public AiracFromIdentifierTest(String id, String effective, int year, int ordinal, boolean ok) {
		this.id = id;
		this.effective = effective;
		this.year = year;
		this.ordinal = ordinal;
		this.ok = ok;
	}

	@NotNull
	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"2014", "2020-12-31", 2020, 14, true},
				{"1511", "2015-10-15", 2015, 11, true},
				{"1514", "", 0, 0, false},
				{"1501", "2015-01-08", 2015, 1, true},
				{"9999", "", 0, 0, false},
				{"6401", "1964-01-16", 1964, 1, true},
				{"6301", "2063-01-04", 2063, 1, true},
				{"6313", "2063-12-06", 2063, 13, true},
				{"9913", "1999-12-30", 1999, 13, true}
		});
	}

	@Test
	public void TestFromIdentifier() {
		String msg = String.format("test of {\"%s\", \"%s\", %d, %d, %b}", id, effective, year, ordinal, ok);
		System.out.println(msg);

		IllegalArgumentException gotException;
		Airac got;

		Instant wantEffective = null;
		if (!effective.isEmpty()) {
			wantEffective = AiracFromInstantTest.instantFromIsoDate(effective);
		}

		try {
			got = Airac.fromIdentifier(id);
			gotException = null;
		} catch (IllegalArgumentException ex) {
			got = null;
			gotException = ex;
		}

		if (ok) {
			assertNull(msg, gotException);
			assertNotNull(msg, got);
			assertEquals(msg, wantEffective, got.effective());
			assertEquals(msg, year, got.year());
			assertEquals(msg, ordinal, got.ordinal());
		} else {
			assertNotNull(msg, gotException);
		}
	}
}
