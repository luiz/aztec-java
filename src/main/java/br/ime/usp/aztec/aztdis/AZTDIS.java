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

import java.io.IOException;

import br.ime.usp.aztec.AZTEC;
import br.ime.usp.aztec.AZTECParameters;
import br.ime.usp.aztec.io.EncodingOutput;
import br.ime.usp.aztec.io.IterableEncodingOutput;

/**
 * The AZTDIS algorithm implementation
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public class AZTDIS {
	/**
	 * Encodes a signal specified as input in the given parameters using the
	 * AZTDIS algorithm.
	 * 
	 * @param parameters
	 *            Parameters for the execution of the algorithm, such as input,
	 *            output, error tolerance etc.
	 * @throws IOException
	 *             if the output throws it
	 * @see AZTDISParameters
	 * @see EncodingOutput
	 */
	public void encode(AZTDISParameters parameters) throws IOException {
		AZTEC aztec = new AZTEC();
		IterableEncodingOutput aztecOutput = new IterableEncodingOutput();
		AZTECParameters aztecParameters = new AZTECParameters.Builder()
				.withMaximumAcceptableVariation(parameters.getThreshold())
				.withMaximumLineLength(Double.POSITIVE_INFINITY)
				.withInput(parameters.getInput()).withOutput(aztecOutput)
				.build();
		aztec.encode(aztecParameters);
		Iterable<Double> significantSamples = new AZTECToSampleConverter()
				.convert(aztecOutput);
		EncodingRefinement refiner = new EncodingRefinement();
		for (int i = 0; i < 5; i++) {
			significantSamples = refiner.refine(significantSamples,
					parameters.getInput(), parameters.getEpsilon());
		}

		// TODO refine encoding using SLOPE

		for (Double sample : significantSamples) {
			parameters.getOutput().put(sample);
		}
	}
}
