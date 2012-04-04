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

	/**
	 * Initializes a new instance of the encoder
	 * 
	 * @param thresholdCalculator
	 *            a {@link ThresholdCalculator} is used to calculate the maximum
	 *            distance between the maximum and the minimum of the signal in
	 *            a small piece of it. When the signal falls off this maximum
	 *            distance, a line is generated in the output.
	 */
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
		EncodingOutput out = params.getOutput();
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		double lastMin = min;
		double lastMax = max;
		int length = 0;
		for (Double value : params.getInput()) {
			min = Math.min(min, value);
			max = Math.max(max, value);
			if (max > min + this.thresholdCalculator.getCurrentThreshold()) {
				this.writeLine(out, lastMin, lastMax, length);
				max = value;
				min = value;
				length = 0;
			}
			lastMin = min;
			lastMax = max;
			length++;
			this.thresholdCalculator.newSample(value);
		}
		this.writeLine(out, min, max, length);
	}

	protected void writeLine(EncodingOutput out, double min, double max,
			int length) throws IOException {
		out.put(length);
		out.put((max + min) * 0.5);
	}
}
