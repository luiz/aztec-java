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
		AlgorithmParameters params = createDefaultParameters();
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

	private AlgorithmParameters createDefaultParameters() {
		return new ProgrammaticAlgorithmParameters.Builder()
				.withMaximumAcceptableVariation(0.1)
				.withOutput(this.output).build();
	}
}
