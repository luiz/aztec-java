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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class MAZTECParametersTest {
	@Test
	public void isCreatedWithDefaultValues() throws Exception {
		MAZTECParameters params = new MAZTECParameters.Builder().build();
		assertThat(params.getC1(), is(MAZTECParameters.DEFAULT_C1));
		assertThat(params.getC2(), is(MAZTECParameters.DEFAULT_C2));
		assertThat(params.getInitialT(), is(MAZTECParameters.DEFAULT_INITIAL_T));
		assertThat(params.getTMin(), is(MAZTECParameters.DEFAULT_T_MIN));
		assertThat(params.getTMax(), is(MAZTECParameters.DEFAULT_T_MAX));
		// Don't know how to test if the input and output are ok...
		assertThat(params.getInput(), notNullValue());
		assertThat(params.getOutput(), notNullValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void doesntAllowPassingMaximumThresholdValueSmallerThanInitialValue()
			throws Exception {
		new MAZTECParameters.Builder().withInitialThreshold(1.0)
				.withMaximumThreshold(0.9);
	}

	@Test(expected = IllegalArgumentException.class)
	public void doesntAllowPassingInitialThresholdBiggerThanMaximumThreshold()
			throws Exception {
		new MAZTECParameters.Builder().withMaximumThreshold(0.9)
				.withInitialThreshold(1.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void doesntAllowPassingMinimumThresholdValueBiggerThanInitialValue()
			throws Exception {
		new MAZTECParameters.Builder().withInitialThreshold(1.0)
				.withMinimumThreshold(1.1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void doesntAllowPassingInitialThresholdSmallerThanMinimumThreshold()
			throws Exception {
		new MAZTECParameters.Builder().withMinimumThreshold(1.1)
				.withInitialThreshold(1.0);
	}

	@Test
	public void allowsDefiningBigMinimumThresholdValueAndAdjustsInitialValueAccordingly()
			throws Exception {
		MAZTECParameters params = new MAZTECParameters.Builder()
				.withMinimumThreshold(1.1).build();
		assertThat(params.getInitialT(), is(1.1));
	}

	@Test
	public void allowsDefiningSmallMaximumThresholdValueAndAdjustsInitialValueAccordingly()
			throws Exception {
		MAZTECParameters params = new MAZTECParameters.Builder()
				.withMaximumThreshold(0.01).build();
		assertThat(params.getInitialT(), is(0.01));
	}

	@Test
	public void knowsIfIsDecoding() throws Exception {
		MAZTECParameters encoding = new MAZTECParameters.Builder()
				.withMaximumThreshold(0.01).build();
		assertFalse(encoding.isDecoding());

		MAZTECParameters decoding = new MAZTECParameters.Builder()
				.withMaximumThreshold(0.01).decoding().build();
		assertTrue(decoding.isDecoding());
	}

	// Is it worth testing the other methods of the builder?
}
