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

import br.ime.usp.aztec.maztec.DefaultThresholdCalculator;
import br.ime.usp.aztec.maztec.SignalStatistics;

/**
 * Only stores fake statistical measures to test if the
 * {@link DefaultThresholdCalculator} uses them properly.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class MockSignalStatistics implements SignalStatistics {

	private double average;
	private double standardDeviation;
	private double thirdMoment;
	private boolean calledReset = false;

	@Override
	public double getAverage() {
		return this.average;
	}

	@Override
	public double getStandardDeviation() {
		return this.standardDeviation;
	}

	@Override
	public double getThirdMoment() {
		return this.thirdMoment;
	}

	@Override
	public void update(double value) {
		// Does nothing. I don't care about the signal
	}

	@Override
	public void reset() {
		this.calledReset = true;
	}

	/**
	 * Configures the value that {@link #getAverage()} should return
	 * 
	 * @param value
	 *            The value for {@link #getAverage()} to return
	 */
	public void returnsAverage(double value) {
		this.average = value;
	}

	/**
	 * Configures the value that {@link #getStandardDeviation()} should return
	 * 
	 * @param value
	 *            The value for {@link #getStandardDeviation()} to return
	 */
	public void returnsStandardDeviation(double value) {
		this.standardDeviation = value;
	}

	/**
	 * Configures the value that {@link #getThirdMoment()} should return
	 * 
	 * @param value
	 *            The value for {@link #getThirdMoment()} to return
	 */
	public void returnsThirdMoment(double value) {
		this.thirdMoment = value;
	}

	/**
	 * @return true if the {@link #reset()} method was called at least once
	 *         after the initialization of this object
	 */
	public boolean resetWasCalled() {
		return this.calledReset;
	}
}
