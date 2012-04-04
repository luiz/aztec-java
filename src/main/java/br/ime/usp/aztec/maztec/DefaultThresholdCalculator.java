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

/**
 * Default implementation of the adaptive threshold of modified AZTEC algorithm
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class DefaultThresholdCalculator implements ThresholdCalculator {
	private final SignalStatistics statsCalculator;
	private final MAZTECParameters params;
	private double threshold;
	private double cf;

	/**
	 * Initialized a new threshold calculator for the modified AZTEC algorithm
	 * 
	 * @param params
	 *            The parameters for the algorithm execution with an initial
	 *            threshold value
	 * @param statsCalculator
	 *            An object that knows how to compute statistics from the signal
	 *            in an online fashion. Used for updating the initial threshold
	 *            given in the algorithm parameters
	 */
	public DefaultThresholdCalculator(MAZTECParameters params,
			SignalStatistics statsCalculator) {
		this.params = params;
		this.statsCalculator = statsCalculator;
		this.threshold = params.getInitialT();
		this.cf = 0.0;
	}

	@Override
	public void newSample(double sample) {
		this.statsCalculator.update(sample);
		double lastCf = this.cf;
		this.cf = this.params.getC1()
				* (this.statsCalculator.getStandardDeviation() + this.statsCalculator
						.getThirdMoment());
		this.threshold = this.threshold - this.params.getC2()
				* (this.cf - lastCf) * this.threshold;
		if (this.threshold < this.params.getTMin()) {
			this.threshold = this.params.getTMin();
		}
		if (this.threshold > this.params.getTMax()) {
			this.threshold = this.params.getTMax();
		}
	}

	@Override
	public double getCurrentThreshold() {
		return this.threshold;
	}

	@Override
	public void reset() {
		this.statsCalculator.reset();
	}

}
