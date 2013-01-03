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

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
public final class MAZTECCommandLineParametersParserTest {
	private static final String TEST_FILE_TEXT = "1.0\n2.0\n";
	private String[] filledParameters;
	private String[] defaultParameters;
	private MAZTECCommandLineParametersParser parser;

	@Before
	public void setUp() throws Exception {
		File tempFile = this.createTempFile();
		this.filledParameters = new String[] { "-t", "0.1", "-T", "1", "-0",
				"0.5", "-1", "2", "-2", "0.2", "-I", "-i",
				tempFile.getAbsolutePath() };
		this.defaultParameters = new String[0];
		this.parser = new MAZTECCommandLineParametersParser();
	}

	private File createTempFile() throws IOException {
		File tempFile = File.createTempFile("tmp", "txt");
		FileWriter fileWriter = new FileWriter(tempFile);
		fileWriter.append(TEST_FILE_TEXT);
		fileWriter.close();
		return tempFile;
	}

	@Test
	public void extractsMinimumThresholdValueFromCommandLine() throws Exception {
		assertThat(this.parser.parse(this.filledParameters).getTMin(), is(0.1));
	}

	@Test
	public void extractsMaximumThresholdValueFromCommandLine() throws Exception {
		assertThat(this.parser.parse(this.filledParameters).getTMax(), is(1.0));
	}

	@Test
	public void extractsInitialThresholdValueFromCommandLine() throws Exception {
		assertThat(this.parser.parse(this.filledParameters).getInitialT(),
				is(0.5));
	}

	@Test
	public void extractsCriterionFunctionWeightValueFromCommandLine()
			throws Exception {
		assertThat(this.parser.parse(this.filledParameters).getC1(), is(2.0));
	}

	@Test
	public void extractsLastThresholdWeightValueFromCommandLine()
			throws Exception {
		assertThat(this.parser.parse(this.filledParameters).getC2(), is(0.2));
	}

	@Test
	public void detectsIfTheImprovedVersionOfTheAlgorithmMustBeUsed()
			throws Exception {
		assertThat(this.parser.parse(this.filledParameters).isImproved(),
				is(true));
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

		MAZTECParameters params = this.parser.parse(new String[] { "-o",
				tempFile.getAbsolutePath() });
		EncodingOutput output = params.getOutput();
		output.put(number);
		output.close();

		String outputLine = new Scanner(tempFile).nextLine();
		assertThat(outputLine, containsString(number.toString()));
	}

	@Test
	public void givesDefaultMinimumThresholdValueIfNoneGiven() throws Exception {
		assertThat(this.parser.parse(this.defaultParameters).getTMin(),
				is(MAZTECParameters.DEFAULT_T_MIN));
	}

	@Test
	public void givesDefaultMaximumThresholdValueIfNoneGiven() throws Exception {
		assertThat(this.parser.parse(this.defaultParameters).getTMax(),
				is(MAZTECParameters.DEFAULT_T_MAX));
	}

	@Test
	public void givesDefaultInitialThresholdValueIfNoneGiven() throws Exception {
		assertThat(this.parser.parse(this.defaultParameters).getInitialT(),
				is(MAZTECParameters.DEFAULT_INITIAL_T));
	}

	@Test
	public void givesDefaultCriterionFunctionWeightIfNoneGiven()
			throws Exception {
		assertThat(this.parser.parse(this.defaultParameters).getC1(),
				is(MAZTECParameters.DEFAULT_C1));
	}

	@Test
	public void givesDefaultLastThresholdWeightIfNoneGiven() throws Exception {
		assertThat(this.parser.parse(this.defaultParameters).getC2(),
				is(MAZTECParameters.DEFAULT_C2));
	}

	@Test
	public void doesNotUseTheImprovedVersionOfTheAlgorithmByDefault()
			throws Exception {
		assertThat(this.parser.parse(this.defaultParameters).isImproved(),
				is(false));
	}

	@Test
	public void isEncodingIfDecodeFlagNotSpecified() throws Exception {
		assertFalse("Should not indicate that is decoding",
				this.parser.parse(this.defaultParameters).isDecoding());
	}

	@Test
	public void extractsDecodingFlagFromCommandLine() throws Exception {
		MAZTECParameters params = this.parser.parse(new String[] { "-d" });
		assertTrue("Should indicate that is decoding", params.isDecoding());
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsExceptionIfInputFileGivenDoesNotExist() throws Exception {
		MAZTECParameters params = this.parser.parse(new String[] { "-i",
				"idontexist" });
		params.getInput();
	}

	@Test(expected = ReadOnlyOutputException.class)
	public void throwsExceptionIfOutputCannotBeOpened() throws Exception {
		File tempFile = this.createTempFile();
		tempFile.setReadOnly();
		MAZTECParameters params = this.parser.parse(new String[] { "-o",
				tempFile.getAbsolutePath() });
		params.getOutput();
	}
}