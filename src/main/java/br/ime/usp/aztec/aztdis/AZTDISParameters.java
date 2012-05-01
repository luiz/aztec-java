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

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import br.ime.usp.aztec.AZTECParameters;
import br.ime.usp.aztec.io.EncodingOutput;
import br.ime.usp.aztec.io.SignalParser;
import br.ime.usp.aztec.io.WriterEncodingOutput;

/**
 * Container for AZTDIS algorithm parameters. Must be created with the Builder
 * class inside it.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class AZTDISParameters {
	/**
	 * Default value for the minimum distance between events
	 */
	public static final double DEFAULT_K = 3;

	private double threshold;
	private double epsilon;
	private double k;
	private Iterable<Double> input;
	private EncodingOutput output;

	private AZTDISParameters() {
	}

	/**
	 * @return Minimum distance between event-generating points
	 */
	public double getMinimumDistanceBetweenEvents() {
		return this.k;
	}

	/**
	 * @return Maximum displacement between a point and the original signal
	 */
	public double getEpsilon() {
		return this.epsilon;
	}

	/**
	 * @return Maximum variation of the signal between events
	 */
	public double getThreshold() {
		return this.threshold;
	}

	/**
	 * @return Iterable with input signal values
	 */
	public Iterable<Double> getInput() {
		return this.input;
	}

	/**
	 * @return Algorithm output
	 */
	public EncodingOutput getOutput() {
		return this.output;
	}

	/**
	 * Builder for {@link AZTDISParameters}
	 * 
	 * @author Luiz Fernando Oliveira Corte Real
	 */
	public static final class Builder {
		private final AZTDISParameters params = new AZTDISParameters();

		/**
		 * @param threshold
		 *            The maximum variation in input signal accepted between the
		 *            events generated by the algorithm.
		 * @return A builder for the the next mandatory parameter of the
		 *         algorithm.
		 * @see AZTDISParameters#getThreshold()
		 * @throws IllegalArgumentException
		 *             if the given threshold isn't positive
		 */
		public SecondMandatoryParameterBuilder withThreshold(double threshold) {
			if (threshold <= 0) {
				throw new IllegalArgumentException("Threshold must be positive");
			}
			this.params.threshold = threshold;
			this.params.k = DEFAULT_K;
			this.params.input = new SignalParser(new InputStreamReader(
					System.in));
			this.params.output = new WriterEncodingOutput(
					new OutputStreamWriter(System.out));
			return new SecondMandatoryParameterBuilder(this.params);
		}
	}

	public static final class SecondMandatoryParameterBuilder {
		private final AZTDISParameters params;

		private SecondMandatoryParameterBuilder(AZTDISParameters params) {
			this.params = params;
		}

		/**
		 * @param displacement
		 *            The maximum displacement accepted between a point of the
		 *            resulting encoding and the original signal.
		 * @return A builder for the optional parameters of the algorithm.
		 * @see AZTDISParameters#getEpsilon()
		 * @throws IllegalArgumentException
		 *             if the given displacement isn't positive
		 */
		public OptionalParametersBuilder withMaximumDisplacement(
				double displacement) {
			if (displacement <= 0) {
				throw new IllegalArgumentException(
						"Maximum accepted displacement must be positive");
			}
			this.params.epsilon = displacement;
			return new OptionalParametersBuilder(this.params);
		}
	}

	public static final class OptionalParametersBuilder {
		private final AZTDISParameters params;

		private OptionalParametersBuilder(AZTDISParameters params) {
			this.params = params;
		}

		/**
		 * @param distance
		 *            The minimum distance between two consecutive significative
		 *            samples. Samples exceeding the threshold in less than this
		 *            distance are not considered significative unconditionally.
		 * @return The builder
		 * @see AZTDISParameters#getMinimumDistanceBetweenEvents()
		 * @throws IllegalArgumentException
		 *             if the given distance isn't positive
		 */
		public OptionalParametersBuilder withMinimumDistanceBetweenEvents(
				double distance) {
			if (distance <= 0) {
				throw new IllegalArgumentException(
						"Minimum distance between events must be positive");
			}
			this.params.k = distance;
			return this;
		}

		/**
		 * @param input
		 *            A signal
		 * @return The builder
		 * @see AZTECParameters#getInput()
		 */
		public OptionalParametersBuilder withInput(Iterable<Double> input) {
			this.params.input = input;
			return this;
		}

		/**
		 * @param output
		 *            A processor of the algorithm output
		 * @return The builder
		 * @see AZTECParameters#getOutput()
		 */
		public OptionalParametersBuilder withOutput(EncodingOutput output) {
			this.params.output = output;
			return this;
		}

		/**
		 * Call this method when you have finished the configuration of the
		 * parameters
		 * 
		 * @return A configured {@link AZTDISParameters}
		 */
		public AZTDISParameters build() {
			return this.params;
		}
	}
}