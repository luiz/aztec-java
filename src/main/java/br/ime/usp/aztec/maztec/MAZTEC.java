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

import java.io.IOException;

import br.ime.usp.aztec.io.EncodingOutput;

/**
 * Modified AZTEC encoder implementation
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class MAZTEC {

	private final ThresholdCalculator thresholdCalculator;

	public MAZTEC(ThresholdCalculator thresholdCalculator) {
		this.thresholdCalculator = thresholdCalculator;
	}

	/**
	 * Encodes the given signal, writing the output to the
	 * {@link EncodingOutput} specified in the {@link MAZTECParameters} passed
	 * as argument
	 * 
	 * @param params
	 *            Parameters for the execution of the algorithm, such as input,
	 *            output and weights for updating the threshold of the algorithm
	 * @throws IOException
	 *             if an error occurs when outputting values
	 * 
	 * @see MAZTECParameters
	 */
	public void encode(MAZTECParameters params) throws IOException {
		params.getOutput().put(5.0);
		params.getOutput().put(1.0);
	}
}
