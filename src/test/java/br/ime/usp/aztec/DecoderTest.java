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

import br.ime.usp.aztec.io.IterableEncodingOutput;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class DecoderTest {

	private Decoder decoder;
	private IterableEncodingOutput output;

	@Before
	public void setUp() throws Exception {
		this.decoder = new Decoder();
		this.output = new IterableEncodingOutput();
	}

	@Test
	public void decodesALineAsALine() throws Exception {
		Iterable<Double> encoded = asList(4.0, 1.0);
		this.decoder.decode(encoded, this.output);
		assertThat(this.output, contains(1.0, 1.0, 1.0, 1.0));
	}

	@Test
	public void decodesAnotherLine() throws Exception {
		Iterable<Double> encoded = asList(6.0, 1.05);
		this.decoder.decode(encoded, this.output);
		assertThat(this.output, contains(1.05, 1.05, 1.05, 1.05, 1.05, 1.05));
	}

	@Test
	public void decodesTwoLines() throws Exception {
		Iterable<Double> encoded = asList(5.0, 1.05, 2.0, 1.12);
		this.decoder.decode(encoded, this.output);
		assertThat(this.output,
				contains(1.05, 1.05, 1.05, 1.05, 1.05, 1.12, 1.12));
	}

	@Test
	public void decodesALineAndAPositiveSlope() throws Exception {
		Iterable<Double> encoded = asList(3.0, 1.0, -3.0, 3.0);
		this.decoder.decode(encoded, this.output);
		assertThat(this.output, contains(1.0, 1.0, 1.0, 2.0, 3.0, 4.0));
	}

	@Test
	public void decodesALineAndANegativeSlope() throws Exception {
		Iterable<Double> encoded = asList(3.0, 1.0, -3.0, -3.0);
		this.decoder.decode(encoded, this.output);
		assertThat(this.output, contains(1.0, 1.0, 1.0, 0.0, -1.0, -2.0));
	}

	@Test
	public void decodesTwoConsecutiveSlopes() throws Exception {
		Iterable<Double> encoded = asList(3.0, 1.0, -3.0, 3.0, -4.0, -4.0);
		this.decoder.decode(encoded, this.output);
		assertThat(this.output,
				contains(1.0, 1.0, 1.0, 2.0, 3.0, 4.0, 3.0, 2.0, 1.0, 0.0));
	}
}
