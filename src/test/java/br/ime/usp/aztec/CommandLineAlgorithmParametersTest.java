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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public class CommandLineAlgorithmParametersTest {
	private static final String TEST_FILE_TEXT = "1.0\n2.0\n";
	private CommandLineAlgorithmParameters filledParameters;
	private CommandLineAlgorithmParameters defaultParameters;

	@Before
	public void setUp() throws Exception {
		File tempFile = createTempFile();
		this.filledParameters = new CommandLineAlgorithmParameters(
				new String[] { "-K", "15", "-T", "3", "-N", "30", "-i",
						tempFile.getAbsolutePath() });
		this.defaultParameters = new CommandLineAlgorithmParameters(
				new String[] { "-K", "20" });
	}

	private File createTempFile() throws IOException {
		File tempFile = File.createTempFile("tmp", "txt");
		FileWriter fileWriter = new FileWriter(tempFile);
		fileWriter.append(TEST_FILE_TEXT);
		fileWriter.close();
		return tempFile;
	}

	@Test
	public void extractsMaximumVoltageVariationFromCommandLine()
			throws Exception {
		assertThat(this.filledParameters.getK(), is(15.0));
	}

	@Test
	public void extractsMinimumLineLengthFromCommandLine() throws Exception {
		assertThat(this.filledParameters.getT(), is(3.0));
	}

	@Test
	public void extractsMaximumLineLengthFromCommandLine() throws Exception {
		assertThat(this.filledParameters.getN(), is(30.0));
	}

	@Test
	public void detectsIfHelpWasAsked() throws Exception {
		assertThat(this.filledParameters.isHelpAsked(), is(false));
		CommandLineAlgorithmParameters askedHelp = new CommandLineAlgorithmParameters(
				new String[] { "-h" });
		assertThat(askedHelp.isHelpAsked(), is(true));
	}

	@Test
	public void createsReaderFromGivenInputFile() throws Exception {
		SignalParser input = this.filledParameters.getInput();
		assertThat(input, contains(1.0, 2.0));
	}

	@Test
	public void createsWriterForGivenOutputFile() throws Exception {
		File tempFile = createTempFile();
		Double number = 3.14159;

		CommandLineAlgorithmParameters params = new CommandLineAlgorithmParameters(
				new String[] { "-K", "20", "-o", tempFile.getAbsolutePath() });
		WriterEncodingOutput output = params.getOutput();
		output.put(number);
		output.close();

		String outputLine = new Scanner(tempFile).nextLine();
		assertThat(outputLine, containsString(number.toString()));
	}

	@Test(expected = ParseException.class)
	public void requiresMaximumVoltageVariation() throws Exception {
		new CommandLineAlgorithmParameters(new String[] {});
	}

	@Test
	public void givesDefaultMinimumLineLengthIfNoneGiven() throws Exception {
		assertThat(this.defaultParameters.getT(), is(4.0));
	}

	@Test
	public void givesDefaultMaximumLineLengthIfNoneGiven() throws Exception {
		assertThat(this.defaultParameters.getN(), is(25.0));
	}
}