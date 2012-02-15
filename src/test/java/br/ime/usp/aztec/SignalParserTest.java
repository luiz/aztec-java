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

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

public final class SignalParserTest {
	@Test
	public void readsNumbersFromReaderOnePerLineAndReturnsThemInAnArray()
			throws Exception {
		String signal = "1.0\n2.0\n3.0\n5.0\n8.0\n";
		Reader reader = new StringReader(signal);
		Iterable<Double> parsedSignal = new SignalParser().parse(reader);
		assertThat(parsedSignal, contains(1.0, 2.0, 3.0, 5.0, 8.0));
	}

	@Test
	public void readsNumbersFromReaderWithNoNewLineInTheEndAndReturnsThemInAnArray()
			throws Exception {
		String signal = "1.0\n2.0\n3.0\n4.0\n5.0";
		Reader reader = new StringReader(signal);
		Iterable<Double> parsedSignal = new SignalParser().parse(reader);
		assertThat(parsedSignal, contains(1.0, 2.0, 3.0, 4.0, 5.0));
	}
}
