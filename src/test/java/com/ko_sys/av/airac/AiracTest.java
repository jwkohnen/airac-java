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


import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AiracTest {
	@Test
	public void testOverflow() {

		Airac first = Airac.fromInstant(Instant.from(ZonedDateTime.of(1800, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)));
		Airac epoch = Airac.fromInstant(Airac.epoch);
		Airac last = Airac.fromInstant(Instant.from(ZonedDateTime.of(2200, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)));

		boolean visitedEpochCycle = false;
		int count = 0;
		for (Airac airac = first;
			 airac.effective().isBefore(last.effective());
			 airac = airac.next()) {
			count++;
			Airac previous = airac.previous();
			Duration between = Duration.between(previous.effective(), airac.effective());

			assertEquals(String.format("Duration between two cycles' effective dates must be 28 days " +
					"( %s <- %s )", previous, airac), Airac.durationCycle, between);

			if (airac.effective().equals(Airac.epoch)) {
				assertEquals(epoch, airac);
				visitedEpochCycle = true;
			}

			Airac airacFromInstant = Airac.fromInstant(airac.effective());
			assertEquals(airac, airacFromInstant);
		}
		assertTrue("we have to pass by the epoch's cycle", visitedEpochCycle);
		System.out.println(count + "; " + first.toLongString() + "; " + last.toLongString());
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

		assertEquals(left.next(), right.next());
		assertEquals(left.next().hashCode(), right.next().hashCode());
	}
}
