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

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.ime.usp.aztec.test.MockEncodingOutput;
import br.ime.usp.aztec.test.MockThresholdCalculator;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class MAZTECTest {
	private MockEncodingOutput output;
	private MockThresholdCalculator thresholdCalculator;

	@Before
	public void setUp() throws Exception {
		this.output = new MockEncodingOutput();
		this.thresholdCalculator = new MockThresholdCalculator();
	}

	@Test
	public void generatesASingleLineIfTheInputSignalVariationStaysWithinTheAdaptiveThreshold()
			throws Exception {
		List<Double> signal = asList(1.0, 1.1, 1.0, 0.9, 1.0);
		MAZTECParameters params = this
				.createDefaultParametersUsingInput(signal);
		this.thresholdCalculator.defineThresholds(0.1, 0.15, 0.25, 0.21, 0.2);

		new MAZTEC(this.thresholdCalculator).encode(params);

		assertThat(this.output, contains(5.0, 1.0));
	}

	@Test
	public void generatesMoreLinesIfTheInputSignalVariationFallsOffTheAdaptiveThreshold()
			throws Exception {
		List<Double> signal = asList(1.0, 1.1, 1.0, 0.9, 1.0);
		MAZTECParameters params = this
				.createDefaultParametersUsingInput(signal);
		this.thresholdCalculator.defineThresholds(0.1, 0.15, 0.25, 0.19, 0.2);

		new MAZTEC(this.thresholdCalculator).encode(params);

		assertThat(this.output, contains(3.0, 1.05, 2.0, 0.95));
	}

	private MAZTECParameters createDefaultParametersUsingInput(
			Iterable<Double> signal) {
		return new MAZTECParameters.Builder().withInput(signal)
				.withOutput(this.output).build();
	}
}
