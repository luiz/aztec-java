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

import org.junit.Before;
import org.junit.Test;

import br.ime.usp.aztec.test.MockSignalStatistics;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class ThresholdCalculatorTest {
	private MAZTECParameters parameters;
	private MockSignalStatistics stats;
	private ThresholdCalculator thresholdCalculator;

	@Before
	public void setUp() throws Exception {
		this.parameters = new MAZTECParameters.Builder().withInitialThreshold(
				0.07).build();
		this.stats = new MockSignalStatistics();
		this.thresholdCalculator = new ThresholdCalculator(this.parameters,
				this.stats);
	}

	@Test
	public void initialValueIsTheSameFromGivenParameters() throws Exception {
		assertThat(this.thresholdCalculator.getCurrentThreshold(),
				is(this.parameters.getInitialT()));
	}

	@Test
	public void updatesThresholdBasedOnStatistics() throws Exception {
		this.stats.returnsAverage(42.0);
		this.stats.returnsStandardDeviation(0.0);
		this.stats.returnsThirdMoment(0.0);
		this.thresholdCalculator.newSample(42);

		assertThat(this.thresholdCalculator.getCurrentThreshold(),
				is(this.parameters.getInitialT()));

		this.stats.returnsAverage(42.5);
		this.stats.returnsStandardDeviation(0.5);
		this.stats.returnsThirdMoment(0.0);
		this.thresholdCalculator.newSample(43);

		assertThat(this.thresholdCalculator.getCurrentThreshold(),
				closeTo(0.0672, 1.0e-10));

		// from now on, statistics are fake
		this.stats.returnsAverage(43.0);
		this.stats.returnsStandardDeviation(0.6);
		this.stats.returnsThirdMoment(0.1);
		this.thresholdCalculator.newSample(45);

		assertThat(this.thresholdCalculator.getCurrentThreshold(),
				closeTo(0.0661248, 1.0e-10));
	}

	@Test
	public void updatesThresholdBasedOnWeights() throws Exception {
		MAZTECParameters parameters = new MAZTECParameters.Builder()
				.withInitialThreshold(1.0).withCriterionFunctionWeight(2.0)
				.withLastThresholdWeight(0.5).build();
		ThresholdCalculator thresholdCalculator = new ThresholdCalculator(
				parameters, this.stats);

		this.stats.returnsAverage(42.0);
		this.stats.returnsStandardDeviation(0.0);
		this.stats.returnsThirdMoment(0.0);
		thresholdCalculator.newSample(42);

		assertThat(thresholdCalculator.getCurrentThreshold(),
				is(parameters.getInitialT()));

		this.stats.returnsAverage(42.5);
		this.stats.returnsStandardDeviation(0.5);
		this.stats.returnsThirdMoment(0.0);
		thresholdCalculator.newSample(43);

		assertThat(thresholdCalculator.getCurrentThreshold(),
				closeTo(0.5, 1.0e-10));

		// from now on, statistics are fake
		this.stats.returnsAverage(43.0);
		this.stats.returnsStandardDeviation(0.6);
		this.stats.returnsThirdMoment(0.1);
		thresholdCalculator.newSample(45);

		assertThat(thresholdCalculator.getCurrentThreshold(),
				closeTo(0.4, 1.0e-10));
	}
}
