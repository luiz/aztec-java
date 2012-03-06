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

import java.io.Reader;
import java.io.Writer;

/**
 * AZTEC algorithm parameters provider interface.
 *
 * @author Luiz Fernando Oliveira Corte Real
 */
public interface AlgorithmParameters {

	/**
	 * Default value for the parameter T of the algorithm, in samples
	 * @see {@link AlgorithmParameters#getT()}
	 */
	public static final double DEFAULT_T = 4;

	/**
	 * Default value for the parameter N of the algorithm, in samples
	 * @see {@link AlgorithmParameters#getN()}
	 */
	public static final double DEFAULT_N = 25;

	/**
	 * @return Minimum size of line to not be considered part of slope
	 */
	double getT();

	/**
	 * @return Maximum variation of voltage to be considered a line
	 */
	double getK();

	/**
	 * @return Maximum length of a line
	 */
	double getN();

	/**
	 * @return Reader for input signal
	 */
	Reader getInput();

	/**
	 * @return Writer for algorithm output
	 */
	Writer getOutput();
}