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

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import br.ime.usp.aztec.EncodingOutput;

/**
 * An implementation of {@link EncodingOutput} that is also iterable, so that
 * one can inspect what values were put.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class MockEncodingOutput implements Iterable<Double>,
		EncodingOutput {

	private final List<Double> values = new LinkedList<Double>();

	@Override
	public void put(double value) throws IOException {
		this.values.add(value);
	}

	@Override
	public Iterator<Double> iterator() {
		return this.values.iterator();
	}

	@Override
	public String toString() {
		return this.values.toString();
	}

	@Override
	public void close() throws IOException {
		// does nothing
	}

}
