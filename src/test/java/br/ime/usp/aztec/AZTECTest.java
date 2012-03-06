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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class AZTECTest {
	private AZTEC aztec;
	private StringWriter mockWriter;

	@Before
	public void setUp() throws Exception {
		this.mockWriter = new StringWriter();
		AlgorithmParameters params = createDefaultParameters();
		this.aztec = new AZTEC(params);
	}

	@Test
	public void encodesALineAsALine() throws Exception {
		Iterable<Double> signal = asList(1.0, 1.0, 1.0, 1.0);
		this.aztec.encode(signal);
		Iterable<Double> result = getAlgorithmOutput();
		assertThat(result, contains(4.0, 1.0));
	}

	private AlgorithmParameters createDefaultParameters() {
		return new ProgrammaticAlgorithmParameters.Builder()
				.withMaximumAcceptableVariation(0.1)
				.withOutput(this.mockWriter).build();
	}

	private Iterable<Double> getAlgorithmOutput() throws IOException {
		return new SignalParser().parse(new StringReader(this.mockWriter
				.getBuffer().toString()));
	}
}
