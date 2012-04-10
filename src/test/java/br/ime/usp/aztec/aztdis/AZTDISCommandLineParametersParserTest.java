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
package br.ime.usp.aztec.aztdis;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import br.ime.usp.aztec.io.EncodingOutput;
import br.ime.usp.aztec.io.PleaseHelpMeException;
import br.ime.usp.aztec.io.ReadOnlyOutputException;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class AZTDISCommandLineParametersParserTest {
	private static final String TEST_FILE_TEXT = "1.0\n2.0\n";
	private String[] filledParameters;
	private String[] defaultParameters;
	private AZTDISCommandLineParametersParser parser;

	@Before
	public void setUp() throws Exception {
		File tempFile = this.createTempFile();
		this.filledParameters = new String[] { "-t", "0.1", "-e", "3", "-k",
				"4", "-i", tempFile.getAbsolutePath() };
		this.defaultParameters = new String[] { "-t", "0.1", "-e", "3" };
		this.parser = new AZTDISCommandLineParametersParser();
	}

	private File createTempFile() throws IOException {
		File tempFile = File.createTempFile("tmp", "txt");
		FileWriter fileWriter = new FileWriter(tempFile);
		fileWriter.append(TEST_FILE_TEXT);
		fileWriter.close();
		return tempFile;
	}

	@Test
	public void extractsThresholdValueFromCommandLine() throws Exception {
		assertThat(this.parser.parse(this.filledParameters).getThreshold(),
				is(0.1));
	}

	@Test
	public void extractsMaximumDisplacementValueFromCommandLine()
			throws Exception {
		assertThat(this.parser.parse(this.filledParameters).getEpsilon(),
				is(3.0));
	}

	@Test
	public void extractsMinimumDistanceBetweenEventsFromCommandLine()
			throws Exception {
		assertThat(this.parser.parse(this.filledParameters)
				.getMinimumDistanceBetweenEvents(), is(4.0));
	}

	@Test(expected = PleaseHelpMeException.class)
	public void throwsHelpWantedExceptionIfHelpFlagIsPresent() throws Exception {
		this.parser.parse(new String[] { "-h" });
	}

	@Test
	public void createsReaderFromGivenInputFile() throws Exception {
		Iterable<Double> input = this.parser.parse(this.filledParameters)
				.getInput();
		assertThat(input, contains(1.0, 2.0));
	}

	@Test
	public void createsWriterForGivenOutputFile() throws Exception {
		File tempFile = this.createTempFile();
		Double number = 3.14159;

		AZTDISParameters params = this.parser.parse(new String[] { "-t", "0.1",
				"-e", "3", "-o", tempFile.getAbsolutePath() });
		EncodingOutput output = params.getOutput();
		output.put(number);
		output.close();

		String outputLine = new Scanner(tempFile).nextLine();
		assertThat(outputLine, containsString(number.toString()));
	}

	@Test
	public void givesDefaultMaximumDistanceBetweenEventsIfNoneGiven()
			throws Exception {
		assertThat(this.parser.parse(this.defaultParameters)
				.getMinimumDistanceBetweenEvents(),
				is(AZTDISParameters.DEFAULT_K));
	}

	@Test(expected = IllegalArgumentException.class)
	public void requiresThresholdOrHelpFlag() throws Exception {
		this.parser.parse(new String[] { "-e", "3.0" });
	}

	@Test(expected = IllegalArgumentException.class)
	public void requiresMaximumDisplacementOrHelpFlag() throws Exception {
		this.parser.parse(new String[] { "-t", "0.1" });
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsExceptionIfInputFileGivenDoesNotExist() throws Exception {
		AZTDISParameters params = this.parser.parse(new String[] { "-t", "0.1",
				"-e", "3", "-i", "idontexist" });
		params.getInput();
	}

	@Test(expected = ReadOnlyOutputException.class)
	public void throwsExceptionIfOutputCannotBeOpened() throws Exception {
		File tempFile = this.createTempFile();
		tempFile.setReadOnly();
		AZTDISParameters params = this.parser.parse(new String[] { "-t", "0.1",
				"-e", "3", "-o", tempFile.getAbsolutePath() });
		params.getOutput();
	}
}