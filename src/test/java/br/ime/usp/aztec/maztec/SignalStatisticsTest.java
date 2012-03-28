/*
Copyright 2012 Luiz Fernando Oliveira Corte Real

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package br.ime.usp.aztec.maztec;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public class SignalStatisticsTest {
	@Test
	public void hasAverageZeroWhenCreated() throws Exception {
		assertThat(new SignalStatistics().getAverage(), is(0.0));
	}

	@Test
	public void hasStandardDeviationZeroWhenCreated() throws Exception {
		assertThat(new SignalStatistics().getStandardDeviation(), is(0.0));
	}

	@Test
	public void hasThirdMomentZeroWhenCreated() throws Exception {
		assertThat(new SignalStatistics().getThirdMoment(), is(0.0));
	}

	@Test
	public void updatesTheAverage() throws Exception {
		SignalStatistics stats = new SignalStatistics();
		stats.update(1.0);
		assertThat(stats.getAverage(), is(1.0));
		stats.update(2.0);
		assertThat(stats.getAverage(), is(1.5));
		stats.update(3.0);
		assertThat(stats.getAverage(), is(2.0));
		stats.update(6.0);
		assertThat(stats.getAverage(), is(3.0));
	}

	@Test
	public void updatesTheStandardDeviation() throws Exception {
		SignalStatistics stats = new SignalStatistics();
		stats.update(1.0);
		assertThat(stats.getStandardDeviation(), is(0.0));
		stats.update(2.0);
		assertThat(stats.getStandardDeviation(), is(Math.sqrt(0.25)));
		stats.update(3.0);
		assertThat(stats.getStandardDeviation(), is(Math.sqrt(2.0 / 3.0)));
		stats.update(6.0);
		assertThat(stats.getStandardDeviation(), is(Math.sqrt(3.5)));
	}

	@Test
	public void updatesTheThirdMoment() throws Exception {
		SignalStatistics stats = new SignalStatistics();
		stats.update(1.0);
		assertThat(stats.getThirdMoment(), is(0.0));
		stats.update(2.0);
		assertThat(stats.getThirdMoment(), is(0.0));
		stats.update(3.0);
		assertThat(stats.getThirdMoment(), is(0.0));
		stats.update(6.0);
		assertThat(stats.getThirdMoment(), is(4.5));
	}
}
