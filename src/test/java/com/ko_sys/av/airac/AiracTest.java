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


import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AiracTest {
	@Test
	public void testUnderflow() {
		Airac first = Airac.fromInstant(Airac.epoch).getNext();
		Airac doc8126 = Airac.fromInstant(Instant.from(ZonedDateTime.of(1998, 1, 29, 0, 0, 0, 0, ZoneOffset.UTC)));
		Airac last = Airac.fromInstant(Instant.from(ZonedDateTime.of(2200, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)));

		boolean visitedDoc8126Cycle = false;

		for (Airac airac = first; airac.compareTo(last) <= 0; airac = airac.getNext()) {
			Airac previous = airac.getPrevious();

			Duration between = Duration.between(previous.getEffective(), airac.getEffective());
			assertEquals(Airac.durationCycle, between);

			if (airac.getEffective().equals(doc8126.getEffective())) {
				assertEquals(doc8126, airac);
				visitedDoc8126Cycle = true;
			}

			Airac airacFromInstant = Airac.fromInstant(airac.getEffective());
			assertEquals(airac, airacFromInstant);

			Airac underflow = Airac.fromInstant(airac.getEffective().minusNanos(1));
			assertEquals(previous, underflow);
			if (airac.getOrdinal() == 1) {
				assertEquals(airac.getYear() - 1, underflow.getYear());
				assertTrue(underflow.getOrdinal() == 13 || underflow.getOrdinal() == 14);
			}
		}
		assertTrue("we have to pass by the DOC 8126's cycle", visitedDoc8126Cycle);
	}

	@Test
	public void testExampleFromInstant() {
		Instant shalom = Instant.from(ZonedDateTime.of(2012, 8, 26, 0, 0, 0, 0, ZoneOffset.UTC));
		String output = String.format("At %s the current AIRAC cycle was %s.\n",
				DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC).format(shalom),
				Airac.fromInstant(shalom).toLongString());
		System.out.println(output);

		assertEquals("At 2012-08-26 the current AIRAC cycle was 1209 (effective: 2012-08-23; expires: 2012-09-19).\n",
				output);
	}

	@Test
	public void testToString() {
		Airac got = Airac.fromInstant(Instant.from(ZonedDateTime.of(2016, 5, 4, 8, 20, 0, 0, ZoneOffset.UTC)));
		String want = "1605";

		assertEquals(want, got.toString());
	}

	@Test
	public void testEqualHashCode() {
		Airac left = Airac.fromIdentifier("1605");
		Airac right = Airac.fromInstant(Instant.from(ZonedDateTime.of(2016, 5, 4, 8, 20, 0, 0, ZoneOffset.UTC)));

		assertEquals(left, right);
		assertEquals(left.hashCode(), right.hashCode());

		assertEquals(left.getNext(), right.getNext());
		assertEquals(left.getNext().hashCode(), right.getNext().hashCode());

		assertTrue(left.equals(left));
		assertTrue(left.equals(right));
		assertTrue(right.equals(right));
		assertTrue(right.equals(left));
		assertFalse(left.equals(null));
		assertFalse(right.equals(new Object()));
	}

	@Test
	public void lastAiracOfYearTest() {
		for (int year = 1901; year < 2200; year++) {
			Airac airac = Airac.fromInstant(Instant.from(ZonedDateTime.of(year + 1, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC))
					.minusNanos(1));
			assertEquals("should be the same year", year, airac.getYear());
		}
	}

	@Test
	public void testIdentifiers() {
		List<Integer> fourteens = Arrays.asList(1976, 1998, 2020, 2043);
		for (int year = 1964; year < 2064; year++) {
			int cycles = 14;
			if (!fourteens.contains(year)) {
				cycles = 13;
			}
			for (int ordinal = 1; ordinal <= cycles; ordinal++) {
				Airac airac = Airac.fromIdentifier(String.format("%02d%02d", year % 100, ordinal));
				assertEquals(year, airac.getYear());
				assertEquals(ordinal, airac.getOrdinal());
			}
		}
	}
}