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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

/**
 * Parses a signal from a given reader
 *
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class SignalParser {

	/**
	 * @param reader
	 *            Reader from where the signal should be read
	 * @return An array with signal values
	 * @throws IOException If the reader fails to read a line
	 */
	public Iterable<Double> parse(Reader reader) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(reader);
		List<Double> values = new LinkedList<Double>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			values.add(Double.parseDouble(line));
		}
		return values;
	}

}
