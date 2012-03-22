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
package br.ime.usp.aztec.iaztec;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import br.ime.usp.aztec.iaztec.IAZTECParameters.Builder;
import br.ime.usp.aztec.io.PleaseHelpMeException;
import br.ime.usp.aztec.io.ReadOnlyOutputException;
import br.ime.usp.aztec.io.SignalParser;
import br.ime.usp.aztec.io.WriterEncodingOutput;

/**
 * Handles command-line options for the improved AZTEC algorithm and prints a
 * help message for them.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class IAZTECCommandLineParametersParser {

	/**
	 * @param commandLine
	 *            Command line arguments received in main method.
	 * @throws ParseException
	 *             if the given arguments are invalid
	 * @throws PleaseHelpMeException
	 *             if the user supplied the 'h' option, wanting help
	 */
	public IAZTECParameters parse(String[] commandLine) throws ParseException,
			PleaseHelpMeException {
		CommandLineParser parser = new PosixParser();
		CommandLine options = parser.parse(this.getCommandLineOptions(),
				commandLine);
		if (options.hasOption('h')) {
			throw new PleaseHelpMeException();
		}
		Builder builder = new IAZTECParameters.Builder();
		return builder
				.withInput(new SignalParser(this.openInputGivenIn(options)))
				.withOutput(
						new WriterEncodingOutput(this
								.openOutputGivenIn(options)))
				.withMinimumThreshold(
						Double.parseDouble(options.getOptionValue('t',
								String.valueOf(IAZTECParameters.DEFAULT_T_MIN))))
				.withMaximumThreshold(
						Double.parseDouble(options.getOptionValue('T',
								String.valueOf(IAZTECParameters.DEFAULT_T_MAX))))
				.withInitialThreshold(
						Double.parseDouble(options.getOptionValue('0', String
								.valueOf(IAZTECParameters.DEFAULT_INITIAL_T))))
				.withCriterionFunctionWeight(
						Double.parseDouble(options.getOptionValue('1',
								String.valueOf(IAZTECParameters.DEFAULT_C1))))
				.withLastThresholdWeight(
						Double.parseDouble(options.getOptionValue('2',
								String.valueOf(IAZTECParameters.DEFAULT_C2))))
				.build();
	}

	private Reader openInputGivenIn(CommandLine options) {
		if (options.hasOption('i')) {
			String fileName = options.getOptionValue('i');
			try {
				return new FileReader(fileName);
			} catch (FileNotFoundException e) {
				throw new IllegalArgumentException("Input file '" + fileName
						+ "' does not exist");
			}
		}
		return new InputStreamReader(System.in);
	}

	private Writer openOutputGivenIn(CommandLine options) {
		if (options.hasOption('o')) {
			String fileName = options.getOptionValue('o');
			try {
				return new FileWriter(fileName);
			} catch (IOException e) {
				throw new ReadOnlyOutputException(fileName);
			}
		}
		return new OutputStreamWriter(System.out);
	}

	private Options getCommandLineOptions() {
		Options options = new Options();

		options.addOption("h", false, "Prints this help and exit");
		options.addOption("i", true, "Specify a input file. "
				+ "If none specified, reads signal from standard input");
		options.addOption("o", true, "Specify an output file. "
				+ "If none specified, writes encoding to standard output");
		options.addOption("t", true,
				"Defines the minimum value the threshold can assume. Defaults to 0.0");
		options.addOption("T", true,
				"Defines the maximum value the threshold can assume."
						+ " Defaults to infinity (no limit)");
		options.addOption(
				"0",
				true,
				"Defines the initial value for the threshold."
						+ " Must be between the minimum and the maximum values for the threshold");
		options.addOption("1", true,
				"Defines the weight of the criterion function (the constant c1)."
						+ " Defaults to 1");
		options.addOption("2", true,
				"Defines the weight of the last threshold in"
						+ " the calculation of the new one (the constant c2)."
						+ " Defaults to 0.08");
		return options;
	}

	/**
	 * Prints help message with program usage
	 */
	public void printHelp() {
		HelpFormatter help = new HelpFormatter();
		help.printHelp(60, "iaztec", "Improved AZTEC algorithm encoder",
				this.getCommandLineOptions(),
				"Feel free to send any comments to "
						+ "lreal at ime dot usp dot br", true);
	}
}
