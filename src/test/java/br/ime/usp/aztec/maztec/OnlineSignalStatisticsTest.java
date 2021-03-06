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

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class OnlineSignalStatisticsTest {
	@Test
	public void hasAverageZeroWhenCreated() throws Exception {
		assertThat(new OnlineSignalStatistics().getAverage(), is(0.0));
	}

	@Test
	public void hasStandardDeviationZeroWhenCreated() throws Exception {
		assertThat(new OnlineSignalStatistics().getStandardDeviation(), is(0.0));
	}

	@Test
	public void hasThirdMomentZeroWhenCreated() throws Exception {
		assertThat(new OnlineSignalStatistics().getThirdMoment(), is(0.0));
	}

	@Test
	public void updatesTheAverage() throws Exception {
		SignalStatistics stats = new OnlineSignalStatistics();
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
		SignalStatistics stats = new OnlineSignalStatistics();
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
		SignalStatistics stats = new OnlineSignalStatistics();
		stats.update(1.0);
		assertThat(stats.getThirdMoment(), is(0.0));
		stats.update(2.0);
		assertThat(stats.getThirdMoment(), is(0.0));
		stats.update(3.0);
		assertThat(stats.getThirdMoment(), is(0.0));
		stats.update(6.0);
		assertThat(stats.getThirdMoment(), is(4.5));
	}

	@Test
	public void updatesTheThirdMomentWhenTheSignalHasNegativeSkewness()
			throws Exception {
		SignalStatistics stats = new OnlineSignalStatistics();
		stats.update(1.0);
		assertThat(stats.getThirdMoment(), is(0.0));
		stats.update(4.0);
		assertThat(stats.getThirdMoment(), is(0.0));
		stats.update(5.0);
		assertThat(stats.getThirdMoment(), closeTo(-70.0 / 27.0, 1.0e-10));
		stats.update(6.0);
		assertThat(stats.getThirdMoment(), is(-4.5));
	}

	@Test
	public void behavesAsANewObjectAfterResetIsCalled() throws Exception {
		SignalStatistics oldStats = new OnlineSignalStatistics();
		oldStats.update(1.0);
		oldStats.update(4.0);
		oldStats.update(5.0);
		oldStats.update(6.0);

		oldStats.reset();

		OnlineSignalStatistics newStats = new OnlineSignalStatistics();

		assertThat(oldStats.getAverage(), is(newStats.getAverage()));
		assertThat(oldStats.getStandardDeviation(),
				is(newStats.getStandardDeviation()));
		assertThat(oldStats.getThirdMoment(), is(newStats.getThirdMoment()));

		oldStats.update(1.0);
		newStats.update(1.0);

		assertThat(oldStats.getAverage(), is(newStats.getAverage()));
		assertThat(oldStats.getStandardDeviation(),
				is(newStats.getStandardDeviation()));
		assertThat(oldStats.getThirdMoment(), is(newStats.getThirdMoment()));

		oldStats.update(2.0);
		newStats.update(2.0);

		assertThat(oldStats.getAverage(), is(newStats.getAverage()));
		assertThat(oldStats.getStandardDeviation(),
				is(newStats.getStandardDeviation()));
		assertThat(oldStats.getThirdMoment(), is(newStats.getThirdMoment()));
	}
}
