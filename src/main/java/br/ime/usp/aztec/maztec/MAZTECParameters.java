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

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import br.ime.usp.aztec.AZTECCommandLineParametersParser;
import br.ime.usp.aztec.io.EncodingOutput;
import br.ime.usp.aztec.io.SignalParser;
import br.ime.usp.aztec.io.WriterEncodingOutput;

/**
 * Container for improved AZTEC algorithm parameters. Must be created with the
 * Builder class inside it.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 * @see AZTECCommandLineParametersParser
 */
public final class MAZTECParameters {

	/**
	 * Default value for the weight of the criterion function
	 */
	public static final double DEFAULT_C1 = 1;

	/**
	 * Default value for the weight of the last threshold
	 */
	public static final double DEFAULT_C2 = 0.08;

	/**
	 * Default value for the minimum value of the threshold
	 */
	public static final double DEFAULT_T_MIN = 0;

	/**
	 * Default value for the maximum value of the threshold
	 */
	public static final double DEFAULT_T_MAX = Double.POSITIVE_INFINITY;

	/**
	 * Default value for initial threshold value
	 */
	public static final double DEFAULT_INITIAL_T = 0.1;

	private double c1;
	private double c2;
	private double tMin;
	private double tMax;
	private double initialT;
	private Iterable<Double> input;
	private EncodingOutput output;

	private MAZTECParameters() {
	}

	/**
	 * @return Weight of the criterion function
	 */
	public double getC1() {
		return this.c1;
	}

	/**
	 * @return Weight of the last threshold
	 */
	public double getC2() {
		return this.c2;
	}

	/**
	 * @return Minimum value for the threshold
	 */
	public double getTMin() {
		return this.tMin;
	}

	/**
	 * @return Maximum value for the threshold
	 */
	public double getTMax() {
		return this.tMax;
	}

	/**
	 * @return Initial value for the threshold
	 */
	public double getInitialT() {
		return this.initialT;
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
	 * Builder for {@link MAZTECParameters}
	 * 
	 * @author Luiz Fernando Oliveira Corte Real
	 */
	public static final class Builder {
		private final MAZTECParameters params = new MAZTECParameters();

		public Builder() {
			this.params.c1 = DEFAULT_C1;
			this.params.c2 = DEFAULT_C2;
			this.params.initialT = DEFAULT_INITIAL_T;
			this.params.tMin = DEFAULT_T_MIN;
			this.params.tMax = DEFAULT_T_MAX;
			this.params.input = new SignalParser(new InputStreamReader(
					System.in));
			this.params.output = new WriterEncodingOutput(
					new OutputStreamWriter(System.out));
		}

		/**
		 * @param c1
		 *            The weight of the criterion function (the c1 constant)
		 * @return The builder
		 * @see MAZTECParameters#getC1()
		 */
		public Builder withCriterionFunctionWeight(double c1) {
			this.params.c1 = c1;
			return this;
		}

		/**
		 * @param c2
		 *            The last threshold weight (the c2 constant)
		 * @return The builder
		 * @see MAZTECParameters#getC2()
		 */
		public Builder withLastThresholdWeight(double c2) {
			this.params.c2 = c2;
			return this;
		}

		/**
		 * @param tMin
		 *            The minimum value the threshold can assume
		 * @return The builder
		 * @see MAZTECParameters#getTMin()
		 */
		public Builder withMinimumThreshold(double tMin) {
			this.params.tMin = tMin;
			return this;
		}

		/**
		 * @param tMax
		 *            The maximum value the threshold can assume
		 * @return The builder
		 * @see MAZTECParameters#getTMax()
		 */
		public Builder withMaximumThreshold(double tMax) {
			this.params.tMax = tMax;
			return this;
		}

		/**
		 * @param initialT
		 *            The initial value for the threshold
		 * @return The builder
		 * @see MAZTECParameters#getInitialT()
		 */
		public Builder withInitialThreshold(double initialT) {
			this.params.initialT = initialT;
			return this;
		}

		/**
		 * @param input
		 *            A signal
		 * @return The builder
		 * @see MAZTECParameters#getInput()
		 */
		public Builder withInput(Iterable<Double> input) {
			this.params.input = input;
			return this;
		}

		/**
		 * @param output
		 *            A processor of the algorithm output
		 * @return The builder
		 * @see MAZTECParameters#getOutput()
		 */
		public Builder withOutput(EncodingOutput output) {
			this.params.output = output;
			return this;
		}

		/**
		 * Call this method when you have finished the configuration of the
		 * parameters
		 * 
		 * @return A configured {@link MAZTECParameters}
		 */
		public MAZTECParameters build() {
			return this.params;
		}
	}
}
