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
		Iterator<Double> signalPoints = signal.iterator();
		Line line = new Line(signalPoints.next());
		Slope slope = null;
		while (signalPoints.hasNext()) {
			double current = signalPoints.next();
			if (!line.canContain(current) || line.isTooLong()) {
				if (line.isTooShort()) {
					if (slope == null) {
						slope = new Slope();
					}
					slope.update(line);
				} else {
					line.end();
				}
				line = new Line(current);
			} else {
				line.update(current);
			}
			if (!line.isTooShort() && slope != null) {
				slope.end();
				slope = null;
			}
		}
		if (slope != null) {
			if (line.isTooShort()) {
				slope.update(line);
				slope.end();
			} else {
				slope.end();
				line.end();
			}
		} else {
			line.end();
		}
	}

	private class Line {
		private double min;
		private double max;
		private double length;

		Line(double initialValue) {
			this.min = initialValue;
			this.max = initialValue;
			this.length = 1;
		}

		void update(double current) {
			if (current > max) {
				max = current;
			} else if (current < min) {
				min = current;
			}
			length++;
		}

		boolean canContain(double value) {
			return value <= min + params.getK() && params.getK() + value >= max;
		}

		boolean isTooLong() {
			return this.length >= params.getN();
		}

		boolean isTooShort() {
			return this.length < params.getT();
		}

		void end() throws IOException {
			EncodingOutput out = params.getOutput();
			out.put(length);
			out.put((max + min) / 2);
		}

		@Override
		public String toString() {
			return "Line [min=" + min + ", max=" + max + ", length=" + length
					+ "]";
		}
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

		void update(Line line) {
			if (line.max >= this.max) {
				this.signal = 1.0;
			} else {
				this.signal = -1.0;
			}
			this.min = Math.min(line.min, this.min);
			this.max = Math.max(line.max, this.max);
			this.duration += line.length;
		}

		@Override
		public String toString() {
			return "Slope [min=" + min + ", max=" + max + ", duration="
					+ duration + ", signal=" + signal + "]";
		}
	}

}
