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
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import br.ime.usp.aztec.io.PleaseHelpMeException;
import br.ime.usp.aztec.io.ReadOnlyOutputException;
import br.ime.usp.aztec.maztec.MAZTECCommandLineParametersParser;

/**
 * Superclass of the command line parser for each algorithm, providing default
 * options and helper methods. Handles options for input and output files and
 * detects if the user wants or needs help.
 * 
 * @param T
 *            Type of the parameters object that will be generated
 * @author Luiz Fernando Oliveira Corte Real
 * @see AZTECCommandLineParametersParser
 * @see MAZTECCommandLineParametersParser
 */
public abstract class CommandLineParametersParser<T> {

	/**
	 * Parses the command line and returns an object to be passed to a specific
	 * algorithm
	 * 
	 * @param commandLine
	 *            Command line arguments received in main method.
	 * @return An object with the parsed options, to be passed to a specific
	 *         algorithm
	 * @throws ParseException
	 *             if the given arguments are invalid
	 * @throws PleaseHelpMeException
	 *             if the user supplied the 'h' option, wanting help
	 * @throws IllegalArgumentException
	 *             if the user didn't supply a mandatory argument
	 */
	public T parse(String[] commandLine) throws ParseException,
			PleaseHelpMeException, IllegalArgumentException {
		CommandLineParser parser = new PosixParser();
		CommandLine options = parser.parse(this.getCommandLineOptions(),
				commandLine);
		if (options.hasOption('h')) {
			throw new PleaseHelpMeException();
		}
		return this.buildParameters(options);
	}

	/**
	 * @param options
	 *            The options parsed from the command line
	 * @return An object with these options in a more suitable format for use in
	 *         an algorithm implementation
	 * @throws IllegalArgumentException
	 *             if a mandatory argument is missing
	 */
	protected abstract T buildParameters(CommandLine options)
			throws IllegalArgumentException;

	/**
	 * Configures the options available in the command line
	 * 
	 * @return An object with the accepted command line options
	 */
	public final Options getCommandLineOptions() {
		Options options = new Options();
		options.addOption("h", false, "Prints this help and exit");
		options.addOption("i", true, "Specify a input file. "
				+ "If none specified, reads signal from standard input");
		options.addOption("o", true, "Specify an output file. "
				+ "If none specified, writes encoding to standard output");
		this.addCustomCommandLineOptions(options);
		return options;
	}

	/**
	 * Subclasses must implement this method to provide their own options,
	 * calling {@link Options#addOption(org.apache.commons.cli.Option)} in the
	 * given argument
	 * 
	 * @param defaultOptions
	 *            An object with the default options, such as input and output
	 *            flags, already filled in
	 */
	protected abstract void addCustomCommandLineOptions(Options defaultOptions);

	/**
	 * Gets the given input file and opens a {@link Reader} for it. If no file
	 * was given, opens a Reader for the standard input instead.
	 * 
	 * @param options
	 *            The parsed command line
	 * @return A reader for a file specified in the command line or for the
	 *         standard input
	 */
	protected Reader openInputGivenIn(CommandLine options) {
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

	/**
	 * Gets the given output file and opens a {@link Writer} for it. If no file
	 * was given, opens a Writer for the standard output instead.
	 * 
	 * @param options
	 *            The parsed command line
	 * @return A writer for a file specified in the command line or for the
	 *         standard output
	 */
	protected Writer openOutputGivenIn(CommandLine options) {
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

	/**
	 * Prints help message with program usage
	 */
	public abstract void printHelp();

}