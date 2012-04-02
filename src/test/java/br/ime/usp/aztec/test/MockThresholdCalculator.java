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
package br.ime.usp.aztec.test;

import br.ime.usp.aztec.maztec.ThresholdCalculator;

/**
 * Only store a predefined threshold value instead of calculating a new
 * threshold every sample.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class MockThresholdCalculator implements ThresholdCalculator {

	private double[] thresholds;
	private int currentThreshold = 0;

	@Override
	public void newSample(double sample) {
		// Does nothing. I don't care for the signal.
	}

	@Override
	public double getCurrentThreshold() {
		return this.thresholds[this.currentThreshold++];
	}

	/**
	 * Defines a new value to be returned from {@link #getCurrentThreshold()}
	 * 
	 * @param thresholds
	 *            the thresholds to be consecutively returned in the next calls
	 *            of {@link #getCurrentThreshold()}
	 */
	public void defineThresholds(double... thresholds) {
		this.currentThreshold = 0;
		this.thresholds = thresholds;
	}
}
