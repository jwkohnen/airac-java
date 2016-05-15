/*
 * Copyright (c) 2016 Wolfgang Johannes Kohnen <wjkohnen@users.noreply.github.com>
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
		Airac gotPrevious = got.getPrevious();
		Airac gotNext = got.getNext();

		assertEquals(msg, previousYear, gotPrevious.getYear());
		assertEquals(msg, previousOrdinal, gotPrevious.getOrdinal());
		assertEquals(msg, nextYear, gotNext.getYear());
		assertEquals(msg, nextOrdinal, gotNext.getOrdinal());
	}
}
