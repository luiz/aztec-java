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

import br.ime.usp.aztec.AZTECParameters.Builder;

/**
 * Handles command-line options, such as minimum and maximum line length, and
 * prints a help message for them.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class CommandLineParametersParser {

	/**
	 * @param commandLine
	 *            Command line arguments received in main method.
	 * @throws ParseException
	 *             if the given arguments are invalid or neither the mandatory
	 *             argument -K nor -h were not given
	 * @throws PleaseHelpMeException
	 *             if the user supplied the 'h' option, wanting help
	 */
	public AZTECParameters parse(String[] commandLine) throws ParseException,
			PleaseHelpMeException {
		CommandLineParser parser = new PosixParser();
		CommandLine options = parser.parse(this.getCommandLineOptions(),
				commandLine);
		if (!options.hasOption('h') && !options.hasOption('K')) {
			throw new ParseException("Mandatory argument not given");
		}
		if (options.hasOption('h')) {
			throw new PleaseHelpMeException();
		}
		Builder builder = new AZTECParameters.Builder();
		return builder
				.withMaximumAcceptableVariation(
						Double.parseDouble(options.getOptionValue('K')))
				.withInput(new SignalParser(this.openInputGivenIn(options)))
				.withOutput(
						new WriterEncodingOutput(this
								.openOutputGivenIn(options)))
				.withMaximumSlopeLineSize(
						Double.parseDouble(options.getOptionValue('T',
								String.valueOf(AZTECParameters.DEFAULT_T))))
				.withMaximumLineLength(
						Double.parseDouble(options.getOptionValue('N',
								String.valueOf(AZTECParameters.DEFAULT_N))))
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

		options.addOption("K", true,
				"Maximum variation of voltage to be considered a line");
		options.addOption("T", true,
				"Minimum size of line to not be considered part of slope. "
						+ "Defaults to 4 samples");
		options.addOption("N", true,
				"Maximum length of a line. Defaults to 25 samples");
		options.addOption("h", false, "Prints this help and exit");
		options.addOption("i", true, "Specify a input file. "
				+ "If none specified, reads signal from standard input");
		options.addOption("o", true, "Specify an output file. "
				+ "If none specified, writes encoding to standard output");
		return options;
	}

	/**
	 * Prints help message with program usage
	 */
	public void printHelp() {
		HelpFormatter help = new HelpFormatter();
		help.printHelp(60, "aztec", "AZTEC algorithm encoder",
				this.getCommandLineOptions(),
				"Feel free to send any comments to "
						+ "lreal at ime dot usp dot br", true);
	}
}
