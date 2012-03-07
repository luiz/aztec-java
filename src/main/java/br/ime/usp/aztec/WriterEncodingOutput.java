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
import java.io.Writer;

/**
 * Writes the given values in a Writer, one per line, using Java's default
 * double formatting.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class WriterEncodingOutput implements EncodingOutput {
	private final Writer writer;

	public WriterEncodingOutput(Writer writer) {
		this.writer = writer;
	}

	@Override
	public void put(double value) throws IOException {
		writer.append(Double.toString(value));
		writer.append('\n');
	}

	/**
	 * Closes underlying writer
	 * 
	 * @throws IOException
	 *             If the underlying writer throws this exception
	 */
	public void close() throws IOException {
		this.writer.close();
	}

}
