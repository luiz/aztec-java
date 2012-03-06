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

/**
 * An object to transform the algorithm output, composed of real values, to any
 * desired format
 *
 * @author Luiz Fernando Oliveira Corte Real
 */
public interface EncodingOutput {
	/**
	 * Adds a new value to the algorithm output
	 *
	 * @param value
	 *            Any value generated by the algorithm
	 * @throws IOException
	 *             If the underlying output can not be modified correctly
	 */
	void put(double value) throws IOException;
}
