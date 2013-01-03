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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class AZTECParametersTest {
	@Test
	public void isCreatedWithDefaultValues() throws Exception {
		AZTECParameters params = new AZTECParameters.Builder()
				.withMaximumAcceptableVariation(0.123).build();
		assertThat(params.getK(), is(0.123));
		assertThat(params.getN(), is(AZTECParameters.DEFAULT_N));
		assertThat(params.getT(), is(AZTECParameters.DEFAULT_T));
		// Don't know how to test if the input and output are ok...
		assertThat(params.getInput(), notNullValue());
		assertThat(params.getOutput(), notNullValue());
	}

	@Test
	public void knowsIfIsDecoding() throws Exception {
		AZTECParameters encoding = new AZTECParameters.Builder()
				.withMaximumAcceptableVariation(0.123).build();
		assertFalse(encoding.isDecoding());

		AZTECParameters decoding = new AZTECParameters.Builder()
				.withMaximumAcceptableVariation(0.123).decoding().build();
		assertTrue(decoding.isDecoding());
	}

	// Is it worth testing the methods of the builder?
}
