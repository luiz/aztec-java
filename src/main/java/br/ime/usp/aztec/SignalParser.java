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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Parses a signal from a given reader
 *
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class SignalParser implements Iterable<Double> {

	private final Reader reader;
	private final List<Double> readPoints = new ArrayList<Double>();

	/**
	 * @param reader
	 *            Reader from where the signal should be read
	 */
	public SignalParser(Reader reader) {
		this.reader = reader;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		boolean hasPoints = false;
		for (Double d : this) {
			builder.append(d);
			builder.append(", ");
			hasPoints = true;
		}
		if (hasPoints) {
			int lastPosition = builder.length() - 1;
			builder.delete(lastPosition - 1, lastPosition);
		}
		builder.append(']');
		return builder.toString();
	}

	/**
	 * @return An iterator for the parsed input values
	 * @see Iterator
	 */
	@Override
	public Iterator<Double> iterator() {
		return new Iterator<Double>() {

			private final BufferedReader bufferedReader = new BufferedReader(reader);
			private final int numReadPoints = readPoints.size();
			private int usedReadPoints = 0;
			private String nextLine;

			@Override
			public boolean hasNext() {
				return (usedReadPoints < numReadPoints) || nextLine() != null;
			}

			@Override
			public Double next() {
				if (usedReadPoints < numReadPoints) {
					return readPoints.get(usedReadPoints++);
				}
				try {
					String toParse = nextLine();
					this.nextLine = null;
					double read = Double.parseDouble(toParse);
					readPoints.add(read);
					return read;
				} catch (Exception e) {
					throw new NoSuchElementException("Failed to read more items");
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Read-only iterator");
			}

			private String nextLine() {
				if (this.nextLine == null) {
					try {
						this.nextLine = bufferedReader.readLine();
					} catch (IOException e) {
						this.nextLine = null;
					}
				}
				return this.nextLine;
			}
		};
	}

}
