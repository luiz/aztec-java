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
 * The modified AZTEC algorithm depends upon the average, the standard deviation
 * and the third moment of a signal to calculate its threshold. Thus, this is
 * the interface an implementor of the calculation of such statistics must
 * implement.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public interface SignalStatistics {

	/**
	 * @return the average calculated from the signal values given for
	 *         {@link #update(double)}
	 */
	double getAverage();

	/**
	 * @return the standard deviation calculated from the signal values given
	 *         for {@link #update(double)}
	 */
	double getStandardDeviation();

	/**
	 * @return the third moment of the signal values given for
	 *         {@link #update(double)}
	 */
	double getThirdMoment();

	/**
	 * Updates the statistics based on the given value
	 * 
	 * @param value
	 *            the next value of the signal
	 */
	void update(double value);

	/**
	 * Resets the statistics to their initial values, as if
	 * {@link #update(double)} was never called
	 */
	void reset();
}