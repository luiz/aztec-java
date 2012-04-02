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
 * Computes the adaptive threshold of the modified AZTEC algorithm
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public interface ThresholdCalculator {

	/**
	 * Use this to notify this object that a new sample has been received and
	 * that the threshold must be updated
	 * 
	 * @param sample
	 *            The sample value
	 */
	void newSample(double sample);

	/**
	 * @return the current threshold for the algorithm
	 */
	double getCurrentThreshold();

}