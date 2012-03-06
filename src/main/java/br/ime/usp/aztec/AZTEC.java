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

/**
 * The algorithm implementation
 *
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class AZTEC {

	private final AlgorithmParameters params;

	public AZTEC(AlgorithmParameters params) {
		this.params = params;
	}

	/**
	 * Encodes the given signal, writing the output to the
	 * {@link EncodingOutput} specified in the {@link AlgorithmParameters}
	 * passed on the construction of this object
	 *
	 * @param signal
	 *            Signal to be encoded
	 * @throws IOException
	 *             If the output throws it
	 * @see AlgorithmParameters
	 * @see EncodingOutput
	 */
	public void encode(Iterable<Double> signal) throws IOException {
		EncodingOutput out = params.getOutput();
		Iterator<Double> signalPoints = signal.iterator();
		double first = signalPoints.next();
		double max = first;
		double min = first;
		int length = 1;
		Slope slope = null;
		while (signalPoints.hasNext()) {
			double current = signalPoints.next();
			if (isCurrentTooFarFrom(current, min, max) || length >= params.getN()) {
				if (length >= params.getT()) {
					out.put(length);
					out.put((max + min) / 2);
				} else {
					if (slope == null) {
						slope = new Slope();
					}
					slope.update(min, max, length);
				}
				max = current;
				min = current;
				length = 0;
			}
			if (current > max) {
				max = current;
			} else if (current < min) {
				min = current;
			}
			length++;
			if (length >= params.getT() && slope != null) {
				slope.end();
				slope = null;
			}
		}
		if (slope != null) {
			if (length < params.getT()) {
				slope.update(min, max, length);
			}
			slope.end();
		} else {
			out.put(length);
			out.put((max + min) / 2);
		}
	}

	private boolean isCurrentTooFarFrom(double current, double min, double max) {
		return current > min + params.getK() || max > params.getK() + current;
	}

	private class Slope {
		private double min;
		private double max;
		private double duration;
		private double signal;

		Slope() {
			this.min = Double.POSITIVE_INFINITY;
			this.max = Double.NEGATIVE_INFINITY;
			this.duration = 0;
			this.signal = 0;
		}

		void end() throws IOException {
			EncodingOutput out = params.getOutput();
			out.put(-duration);
			out.put(signal * (max - min));
		}

		void update(double min, double max, double length) {
			if (max >= this.max) {
				this.signal = 1.0;
			} else {
				this.signal = -1.0;
			}
			this.min = Math.min(min, this.min);
			this.max = Math.max(max, this.max);
			this.duration += length;
		}
	}

}
