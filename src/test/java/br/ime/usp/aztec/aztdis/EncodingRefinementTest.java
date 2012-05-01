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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class EncodingRefinementTest {
	private EncodingRefinement encodingRefinement;

	@Before
	public void setUp() throws Exception {
		this.encodingRefinement = new EncodingRefinement();
	}

	@Test
	public void outputsTheEncodingWithoutChangesIfItsAlreadyCloseToTheSignal()
			throws Exception {
		Iterable<Double> signal = asList(1.0, 1.01, 1.1, 1.1, 1.0, 1.01, 1.02);
		Iterable<Double> encoding = asList(0.0, 1.05, 6.0, 1.05);

		Iterable<Double> refinedEncoding = this.encodingRefinement.refine(
				encoding, signal, 0.8);

		assertThat(refinedEncoding, equalTo(encoding));
	}

	@Test
	public void addsAPointOfTheSignalTooFarFromTheEncoding() throws Exception {
		Iterable<Double> signal = asList(1.0, 1.01, 1.1, 1.1, 1.0, 1.12, 1.12);
		Iterable<Double> encoding = asList(0.0, 1.05, 5.0, 1.12, 6.0, 1.12);

		Iterable<Double> refinedEncoding = this.encodingRefinement.refine(
				encoding, signal, 0.08);

		assertThat(refinedEncoding,
				contains(0.0, 1.05, 4.0, 1.0, 5.0, 1.12, 6.0, 1.12));
	}

	@Test
	public void addsOnlyOnePointOfTheSignalTooFarFromTheEncodingPerIteration()
			throws Exception {
		Iterable<Double> signal = asList(1.0, 1.01, 1.1, 1.1, 1.0, 1.0, 1.0,
				1.12, 1.12);
		Iterable<Double> encoding = asList(0.0, 1.05, 7.0, 1.12, 8.0, 1.12);

		Iterable<Double> refinedEncoding = this.encodingRefinement.refine(
				encoding, signal, 0.08);

		assertThat(refinedEncoding,
				contains(0.0, 1.05, 4.0, 1.0, 7.0, 1.12, 8.0, 1.12));
	}
}
