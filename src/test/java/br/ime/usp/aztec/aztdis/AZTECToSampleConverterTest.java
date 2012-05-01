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
package br.ime.usp.aztec.aztdis;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class AZTECToSampleConverterTest {

	private AZTECToSampleConverter converter;

	@Before
	public void setUp() throws Exception {
		this.converter = new AZTECToSampleConverter();
	}

	@Test
	public void outputsNothingWithAnEmptyEncoding() throws Exception {
		Iterable<Double> aztecEncoding = emptyList();

		Iterable<Double> result = this.converter.convert(aztecEncoding);

		assertThat(result, Matchers.<Double> emptyIterable());
	}

	@Test
	public void doesntChangeASingleHorizontalLineButRepeatsTheFirstSampleInTheEnd()
			throws Exception {
		Iterable<Double> aztecEncoding = asList(5.0, 1.02);

		Iterable<Double> result = this.converter.convert(aztecEncoding);

		assertThat(result, contains(0.0, 1.02, 4.0, 1.02));
	}

	@Test
	public void transformsTheDurationOfAnIntermediateHorizontalLineIntoItsTemporalPosition()
			throws Exception {
		Iterable<Double> aztecEncoding = asList(5.0, 1.02, 2.0, 1.2, 3.0, 1.0);

		Iterable<Double> result = this.converter.convert(aztecEncoding);

		assertThat(result, contains(0.0, 1.02, 5.0, 1.2, 7.0, 1.0, 9.0, 1.0));
	}

	@Test
	public void computesTheNextSignificantSampleAfterASlope() throws Exception {
		Iterable<Double> aztecEncoding = asList(4.0, 1.025, -3.0, 0.25, 4.0,
				1.5);

		Iterable<Double> result = this.converter.convert(aztecEncoding);

		assertThat(result,
				contains(0.0, 1.025, 4.0, 1.025, 7.0, 1.5, 10.0, 1.5));
	}

	@Test
	public void convertsASlopeAtTheEndOfTheEncodingToASignificantSample()
			throws Exception {
		Iterable<Double> aztecEncoding = asList(4.0, 0.0, -2.0, 1.0);

		Iterable<Double> result = this.converter.convert(aztecEncoding);

		assertThat(result, contains(0.0, 0.0, 4.0, 0.0, 5.0, 1.0));
	}

	@Test
	public void convertsASequenceOfSlopesToSignificantSamples()
			throws Exception {
		Iterable<Double> aztecEncoding = asList(1.0, 1.0, -4.0, 3.0, -4.0,
				-3.0, -4.0, 1.5);

		Iterable<Double> result = this.converter.convert(aztecEncoding);

		assertThat(result,
				contains(0.0, 1.0, 1.0, 1.0, 5.0, 4.0, 9.0, 1.0, 12.0, 2.5));
	}
}
