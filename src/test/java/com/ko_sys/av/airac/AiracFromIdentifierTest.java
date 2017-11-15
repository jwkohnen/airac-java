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
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class AiracFromIdentifierTest {
	private final String id;
	private final String expectEffective;
	private final int expectYear;
	private final int expectOrdinal;
	private final boolean shallNotFail;

	public AiracFromIdentifierTest(String id, String expectEffective, int expectYear, int expectOrdinal, boolean shallNotFail) {
		this.id = id;
		this.expectEffective = expectEffective;
		this.expectYear = expectYear;
		this.expectOrdinal = expectOrdinal;
		this.shallNotFail = shallNotFail;
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
				{"9913", "1999-12-30", 1999, 13, true},
				{"101", "", 0, 0, false},
				{"160a", "", 0, 0, false},
				{"1a01", "", 0, 0, false},
				{"1016", "", 0, 0, false},
				{"10-1", "", 0, 0, false},
				{"-101", "", 0, 0, false},
				{"", "", 0, 0, false},
				{"nope", "", 0, 0, false}
		});
	}

	@Test
	public void TestFromIdentifier() {
		String msg = String.format("test of {\"%s\", \"%s\", %d, %d, %b}", id, expectEffective, expectYear, expectOrdinal, shallNotFail);
		System.out.println(msg);

		IllegalArgumentException actualException;
		Airac actual;

		Instant expectEffectiveInstant = null;
		if (!expectEffective.isEmpty()) {
			expectEffectiveInstant = AiracFromInstantTest.instantFromIsoDate(expectEffective);
		}

		try {
			actual = Airac.fromIdentifier(id);
			actualException = null;
		} catch (IllegalArgumentException ex) {
			actual = null;
			actualException = ex;
		}

		if (shallNotFail) {
			assertNull(msg, actualException);
			assertNotNull(msg, actual);
			assertEquals(msg, expectYear, actual.getYear());
			assertEquals(msg, expectOrdinal, actual.getOrdinal());
			assertEquals(msg, expectEffectiveInstant, actual.getEffective());
		} else {
			assertNotNull(msg, actualException);
		}
	}
}
