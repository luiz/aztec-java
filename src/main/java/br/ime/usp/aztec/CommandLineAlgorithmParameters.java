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

/**
 * Parses algorithm parameters from command line, such as voltage variation
 * threshold, and stores them.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class CommandLineAlgorithmParameters implements
		AlgorithmParameters {

	private final CommandLine options;

	/**
	 * @param commandLine
	 *            Command line arguments received in main method.
	 * @throws ParseException
	 *             if the given arguments are invalid or neither the mandatory
	 *             argument -K nor -h were not given
	 */
	public CommandLineAlgorithmParameters(String[] commandLine)
			throws ParseException {
		CommandLineParser parser = new PosixParser();
		this.options = parser.parse(getCommandLineOptions(), commandLine);
		if (!this.options.hasOption('h') && !this.options.hasOption('K')) {
			throw new ParseException("Mandatory argument not given");
		}
	}

	@Override
	public double getT() {
		return Double.parseDouble(this.options.getOptionValue('T',
				String.valueOf(DEFAULT_T)));
	}

	@Override
	public double getK() {
		return Double.parseDouble(this.options.getOptionValue('K'));
	}

	@Override
	public double getN() {
		return Double.parseDouble(this.options.getOptionValue('N',
				String.valueOf(DEFAULT_N)));
	}

	@Override
	public SignalParser getInput() {
		return new SignalParser(this.openInputGivenIn(this.options));
	}

	@Override
	public WriterEncodingOutput getOutput() {
		return new WriterEncodingOutput(this.openOutputGivenIn(this.options));
	}

	/**
	 * @return True if -h option was given at the command line and the help
	 *         message should be printed
	 */
	public boolean isHelpAsked() {
		return this.options.hasOption('h');
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

	private static Options getCommandLineOptions() {
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
	public static void printHelp() {
		HelpFormatter help = new HelpFormatter();
		help.printHelp(60, "aztec", "AZTEC algorithm encoder",
				getCommandLineOptions(), "Feel free to send any comments to "
						+ "lreal at ime dot usp dot br", true);
	}
}
