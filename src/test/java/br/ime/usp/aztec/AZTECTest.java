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
package br.ime.usp.aztec;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import br.ime.usp.aztec.test.MockEncodingOutput;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class AZTECTest {
	private AZTEC aztec;
	private MockEncodingOutput output;

	@Before
	public void setUp() throws Exception {
		this.output = new MockEncodingOutput();
		AlgorithmParameters params = this.createDefaultParameters();
		this.aztec = new AZTEC(params);
	}

	@Test
	public void encodesALineAsALine() throws Exception {
		Iterable<Double> signal = asList(1.0, 1.0, 1.0, 1.0);
		this.aztec.encode(signal);
		assertThat(this.output, contains(4.0, 1.0));
	}

	@Test
	public void encodesSomethingCloseToALineAsALine() throws Exception {
		Iterable<Double> signal = asList(1.0, 1.1, 1.1, 1.1, 1.0, 1.1);
		this.aztec.encode(signal);
		assertThat(this.output, contains(6.0, 1.05));
	}

	@Test
	public void createsTwoLinesIfInputVariationExceedsMaximumAcceptableVariation()
			throws Exception {
		Iterable<Double> signal = asList(1.0, 1.01, 1.1, 1.0, 1.02, 1.12, 1.12);
		this.aztec.encode(signal);
		assertThat(this.output, contains(5.0, 1.05, 2.0, 1.12));
	}

	@Test
	public void breaksABigLineIntoTwo() throws Exception {
		Iterable<Double> signal = asList(1.0, 1.1, 1.1, 1.1, 1.0, 1.1, 1.1,
				1.1, 1.1, 1.1, 1.1);
		this.aztec.encode(signal);
		assertThat(this.output, contains(10.0, 1.05, 1.0, 1.1));
	}

	@Test
	public void encodesASinglePositiveSlope() throws Exception {
		Iterable<Double> signal = asList(1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.5,
				1.6, 1.8, 1.9, 2.0);
		this.aztec.encode(signal);
		assertThat(this.output, contains(-11.0, 1.0));
	}

	@Test
	public void encodesASingleNegativeSlope() throws Exception {
		Iterable<Double> signal = asList(2.0, 1.9, 1.8, 1.7, 1.6, 1.5, 1.4,
				1.3, 1.2, 1.1, 1.0);
		this.aztec.encode(signal);
		assertThat(this.output, contains(-11.0, -1.0));
	}

	@Test
	public void encodesASmallLineBetweenTwoLines() throws Exception {
		Iterable<Double> signal = asList(1.0, 1.0, 1.1, 1.1, 1.2, 1.3, 1.4,
				1.5, 1.5, 1.5);
		this.aztec.encode(signal);
		assertThat(this.output, contains(4.0, 1.05, 2.0, 1.25, 4.0, 1.45));
	}

	@Test
	public void encodesASmallSlopeBetweenTwoLines() throws Exception {
		Iterable<Double> signal = asList(1.0, 1.05, 1.0, 1.05, 1.15, 1.3, 1.4,
				1.45, 1.5, 1.5, 1.5);
		this.aztec.encode(signal);
		assertThat(this.output, contains(4.0, 1.025, -3.0, 0.25, 4.0, 1.475));
	}

	@Test
	public void encodesABigNegativeSlopeBetweenTwoLines() throws Exception {
		Iterable<Double> signal = asList(10.0, 10.0, 10.0, 10.0, 9.0, 8.0, 7.0,
				6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 1.0, 1.0, 1.0);
		this.aztec.encode(signal);
		assertThat(this.output, contains(4.0, 10.0, -8.0, -7.0, 4.0, 1.0));
	}

	@Test
	public void encodesAPositiveSlopeFollowedByANegativeSlopeFollowedByAPositiveSlope()
			throws Exception {
		Iterable<Double> signal = asList(1.0, 2.0, 3.0, 4.0, 3.0, 2.0, 1.0,
				0.0, 0.5, 1.0, 1.5, 2.0);
		this.aztec.encode(signal);
		assertThat(this.output, contains(-4.0, 3.0, -4.0, -3.0, -4.0, 1.5));
	}

	@Test
	public void encodesASmallSlopeAtTheEndOfTheSignal() throws Exception {
		Iterable<Double> signal = asList(0.0, 0.0, 0.0, 0.0, 1.0, 2.0);
		this.aztec.encode(signal);
		assertThat(this.output, contains(4.0, 0.0, -2.0, 1.0));
	}

	private AlgorithmParameters createDefaultParameters() {
		return new ProgrammaticAlgorithmParameters.Builder()
				.withMaximumAcceptableVariation(0.1)
				.withMaximumLineLength(10.0).withOutput(this.output).build();
	}
}
