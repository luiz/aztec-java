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
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Parses algorithm parameters from command line, such as voltage variation
 * threshold, and stores them.
 *
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class AlgorithmParameters {

	private static final double DEFAULT_T = 25; // in samples
	private static final double DEFAULT_N = 4;

	private final double t;
	private final double k;
	private final double n;
	private final Reader input;

	/**
	 * @param commandLine
	 *            Command line arguments received in main method.
	 */
	public AlgorithmParameters(String[] commandLine) {
		try {
			CommandLineParser parser = new PosixParser();
			CommandLine options = parser.parse(getCommandLineOptions(),
					commandLine);
			if (options.hasOption('h')) {
				printHelpAndExitWithCode(0);
			}
			this.k = Double.parseDouble(options.getOptionValue('K'));
			this.t = Double.parseDouble(options.getOptionValue('T',
					String.valueOf(DEFAULT_T)));
			this.n = Double.parseDouble(options.getOptionValue('N',
					String.valueOf(DEFAULT_N)));
			this.input = openInputGivenIn(options);
		} catch (ParseException e) {
			printHelpAndExitWithCode(1);
			throw new RuntimeException("To keep the compiler happy");
		}
	}

	/**
	 * @return Minimum size of line to not be considered part of slope
	 */
	public double getT() {
		return this.t;
	}

	/**
	 * @return Maximum variation of voltage to be considered a line
	 */
	public double getK() {
		return this.k;
	}

	/**
	 * @return Maximum length of a line
	 */
	public double getN() {
		return this.n;
	}

	/**
	 * @return Reader for input signal
	 */
	public Reader getInput() {
		return this.input;
	}

	private Options getCommandLineOptions() {
		Options options = new Options();

		Option optionK = new Option("K", true,
				"Maximum variation of voltage to be considered a line");
		optionK.setRequired(true);
		options.addOption(optionK);

		options.addOption(
				"T",
				true,
				"Minimum size of line to not be considered part of slope. " +
				"Defaults to 4 samples");
		options.addOption("N", true,
				"Maximum length of a line. Defaults to 25 samples");
		options.addOption("h", false, "Prints this help and exit");
		options.addOption("i", true,
				"Specify a input file. " +
				"If none specified, reads signal from standard input");
		return options;
	}

	private Reader openInputGivenIn(CommandLine options) {
		if (options.hasOption('i')) {
			try {
				return new FileReader(options.getOptionValue('i'));
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
				System.exit(2);
			}
		}
		return new InputStreamReader(System.in);
	}

	private void printHelpAndExitWithCode(int code) {
		HelpFormatter help = new HelpFormatter();
		help.printHelp(
				60,
				"aztec",
				"AZTEC algorithm encoder",
				getCommandLineOptions(),
				"Feel free to send any comments to " +
				"lreal at ime dot usp dot br",
				true);
		System.exit(code);
	}
}
