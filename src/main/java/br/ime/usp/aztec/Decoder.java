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

import java.io.IOException;
import java.util.Iterator;

import br.ime.usp.aztec.io.EncodingOutput;
import br.ime.usp.aztec.io.MalformedInputException;

/**
 * Decodes a signal encoded by the AZTEC, mAZTEC or imAZTEC algorithm.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class Decoder {
	/**
	 * @param encoded
	 *            Encoded signal to be decoded
	 * @param output
	 *            Destination of the decoded signal
	 * @throws IOException
	 *             if the output throws it
	 * @throws MalformedInputException
	 *             if the input is not as expected
	 */
	public void decode(Iterable<Double> encoded, EncodingOutput output)
			throws IOException, MalformedInputException {
		Iterator<Double> iterator = encoded.iterator();
		Double lastValue = null;
		while (iterator.hasNext()) {
			int repetitions = iterator.next().intValue();
			if (repetitions > 0) {
				lastValue = iterator.next();
				this.decodeLine(repetitions, output, lastValue);
			} else {
				lastValue = this.decodeSlope(-repetitions, output,
						iterator.next(), lastValue);
			}
		}
	}

	private void decodeLine(int repetitions, EncodingOutput output, double value)
			throws IOException {
		for (int i = 0; i < repetitions; i++) {
			output.put(value);
		}
	}

	private double decodeSlope(int length, EncodingOutput output,
			double height, double base) throws IOException {
		double step = height / length;
		double value = base;
		for (int i = 0; i < length; i++) {
			value += step;
			output.put(value);
		}
		return value;
	}
}
