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
 * Computes statistics of a signal as it is updated.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class OnlineSignalStatistics implements SignalStatistics {

	private int count = 0;
	private double average = 0.0;
	private double stdDev = 0.0;
	private double thirdMoment = 0.0;
	private double m2 = 0.0;
	private double m3 = 0.0;

	@Override
	public double getAverage() {
		return this.average;
	}

	@Override
	public double getStandardDeviation() {
		return this.stdDev;
	}

	@Override
	public double getThirdMoment() {
		return this.thirdMoment;
	}

	@Override
	public void update(double value) {
		double delta = (value - this.average);
		int lastCount = this.count++;
		double deltaByN = delta / this.count;
		double updateSquareSumTerm = delta * deltaByN * lastCount;

		this.average += deltaByN;
		this.m3 += updateSquareSumTerm * deltaByN * (this.count - 2) - 3
				* deltaByN * this.m2;
		this.m2 += updateSquareSumTerm;
		this.stdDev = Math.sqrt(this.m2 / this.count);
		this.thirdMoment = this.m3 / this.count;
	}

}
