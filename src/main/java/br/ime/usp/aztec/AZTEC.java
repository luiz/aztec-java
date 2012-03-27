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

import br.ime.usp.aztec.io.EncodingOutput;

/**
 * The algorithm implementation
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class AZTEC {

	private State state;

	/**
	 * Encodes the given signal, writing the output to the
	 * {@link EncodingOutput} specified in the {@link AZTECParameters} passed as
	 * argument
	 * 
	 * @param parameters
	 *            Parameters for the execution of the algorithm, such as input,
	 *            output, maximum line length etc.
	 * @throws IOException
	 *             If the output throws it
	 * @see AZTECParameters
	 * @see EncodingOutput
	 */
	public void encode(AZTECParameters parameters) throws IOException {
		this.state = new ShortLine(parameters);
		for (Double sample : parameters.getInput()) {
			this.state.process(sample);
		}
		this.state.finish();
	}

	private interface State {
		void process(double sample) throws IOException;

		void finish() throws IOException;
	}

	private class ShortLine implements State {
		private final Line line;
		private final AZTECParameters params;

		public ShortLine(AZTECParameters parameters) {
			this.params = parameters;
			this.line = new Line(parameters);
		}

		@Override
		public void process(double sample) throws IOException {
			if (this.line.canContain(sample)) {
				this.line.update(sample);
				if (!this.line.isTooShort()) {
					AZTEC.this.state = new NormalLine(this.params, this.line);
				}
			} else {
				AZTEC.this.state = new PossibleSlope(this.params, this.line);
				AZTEC.this.state.process(sample);
			}
		}

		@Override
		public void finish() throws IOException {
			this.line.end();
		}
	}

	private class NormalLine implements State {
		private final Line line;
		private final AZTECParameters params;

		NormalLine(AZTECParameters params, Line line) {
			this.params = params;
			this.line = line;
		}

		@Override
		public void process(double sample) throws IOException {
			if (this.line.canContain(sample)) {
				this.line.update(sample);
				if (this.line.isTooLong()) {
					this.finish();
					AZTEC.this.state = new ShortLine(this.params);
				}
			} else {
				this.finish();
				AZTEC.this.state = new ShortLine(this.params);
				AZTEC.this.state.process(sample);
			}
		}

		@Override
		public void finish() throws IOException {
			this.line.end();
		}
	}

	private class PossibleSlope implements State {
		private final Line previousLine;
		private final Line currentLine;
		private final AZTECParameters params;

		PossibleSlope(AZTECParameters params, Line previousLine) {
			this.params = params;
			this.currentLine = new Line(params);
			this.previousLine = previousLine;
		}

		@Override
		public void process(double sample) throws IOException {
			if (this.currentLine.canContain(sample)) {
				this.currentLine.update(sample);
				if (!this.currentLine.isTooShort()) {
					this.previousLine.end();
					AZTEC.this.state = new NormalLine(this.params,
							this.currentLine);
				}
			} else {
				this.detectSlopeType();
				AZTEC.this.state.process(sample);
			}
		}

		private void detectSlopeType() {
			int duration = this.previousLine.length + this.currentLine.length;
			if (this.currentLine.average() > this.previousLine.average()) {
				Slope slope = new Slope(this.params, this.previousLine.min,
						this.currentLine.max, 1.0, duration);
				AZTEC.this.state = new AscendingSlope(this.params, slope);
			} else {
				Slope slope = new Slope(this.params, this.currentLine.min,
						this.previousLine.max, -1.0, duration);
				AZTEC.this.state = new DescendingSlope(this.params, slope);
			}
		}

		@Override
		public void finish() throws IOException {
			this.detectSlopeType();
			AZTEC.this.state.finish();
		}
	}

	private abstract class SlopeState implements State {
		protected final Slope slope;
		protected final AZTECParameters params;
		protected Line line;

		SlopeState(AZTECParameters params, Slope slope) {
			this.params = params;
			this.slope = slope;
			this.line = new Line(params);
		}

		@Override
		public void process(double sample) throws IOException {
			if (this.line.canContain(sample)) {
				this.line.update(sample);
				if (!this.line.isTooShort()) {
					this.slope.end();
					AZTEC.this.state = new NormalLine(this.params, this.line);
				}
			} else {
				if (this.changedSignal()) {
					this.slope.end();
					AZTEC.this.state = new PossibleSlope(this.params, this.line);
					AZTEC.this.state.process(sample);
				} else {
					this.slope.update(this.line);
					this.line = new Line(this.params);
					this.line.update(sample);
				}
			}
		}

		@Override
		public void finish() throws IOException {
			this.slope.update(this.line);
			this.slope.end();
		}

		protected abstract boolean changedSignal();
	}

	private class AscendingSlope extends SlopeState {

		AscendingSlope(AZTECParameters params, Slope slope) {
			super(params, slope);
		}

		@Override
		protected boolean changedSignal() {
			return this.line.average() < this.slope.max;
		}
	}

	private class DescendingSlope extends SlopeState {
		DescendingSlope(AZTECParameters params, Slope slope) {
			super(params, slope);
		}

		@Override
		protected boolean changedSignal() {
			return this.line.average() > this.slope.min;
		}
	}

	private class Line {
		private double min = Double.POSITIVE_INFINITY;
		private double max = Double.NEGATIVE_INFINITY;
		private int length = 0;
		private final AZTECParameters params;

		Line(AZTECParameters parameters) {
			this.params = parameters;
		}

		void update(double current) {
			this.min = Math.min(current, this.min);
			this.max = Math.max(current, this.max);
			this.length++;
		}

		boolean canContain(double value) {
			return value <= this.min + this.params.getK()
					&& this.params.getK() + value >= this.max;
		}

		boolean isTooLong() {
			return this.length >= this.params.getN();
		}

		boolean isTooShort() {
			return this.length < this.params.getT();
		}

		void end() throws IOException {
			EncodingOutput out = this.params.getOutput();
			out.put(this.length);
			out.put(this.average());
		}

		double average() {
			return (this.max + this.min) * 0.5;
		}

		@Override
		public String toString() {
			return "Line [min=" + this.min + ", max=" + this.max + ", length="
					+ this.length + "]";
		}
	}

	private class Slope {
		private double min;
		private double max;
		private double duration;
		private final double signal;
		private final AZTECParameters params;

		Slope(AZTECParameters params, double min, double max, double signal,
				double duration) {
			this.params = params;
			this.min = min;
			this.max = max;
			this.signal = signal;
			this.duration = duration;
		}

		void end() throws IOException {
			EncodingOutput out = this.params.getOutput();
			out.put(-this.duration);
			out.put(this.signal * (this.max - this.min));
		}

		void update(Line line) {
			this.min = Math.min(line.min, this.min);
			this.max = Math.max(line.max, this.max);
			this.duration += line.length;
		}

		@Override
		public String toString() {
			return "Slope [min=" + this.min + ", max=" + this.max
					+ ", duration=" + this.duration + ", signal=" + this.signal
					+ "]";
		}
	}

}
