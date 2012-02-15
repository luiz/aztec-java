package br.ime.usp.aztec;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class AlgorithmParameters {

	private static final double DEFAULT_T = 25; // in samples
	private static final double DEFAULT_N = 4;

	private final double t;
	private final double k;
	private final double n;

	public AlgorithmParameters(String[] commandLine) {
		try {
			CommandLineParser parser = new PosixParser();
			CommandLine options = parser.parse(getCommandLineOptions(), commandLine);
			if (options.hasOption('h')) {
				printHelpAndExit();
			}
			this.k = Double.parseDouble(options.getOptionValue('K'));
			this.t = Double.parseDouble(options.getOptionValue('T', String.valueOf(DEFAULT_T)));
			this.n = Double.parseDouble(options.getOptionValue('N', String.valueOf(DEFAULT_N)));
		} catch (ParseException e) {
			printHelpAndExit();
			throw new RuntimeException("To keep the compiler happy");
		}
	}

	public double getT() {
		return this.t;
	}

	public double getK() {
		return this.k;
	}

	public double getN() {
		return this.n;
	}

	private Options getCommandLineOptions() {
		Options options = new Options();

		Option optionK = new Option("K", true, "Maximum variation of voltage to be considered a line");
		optionK.setRequired(true);
		options.addOption(optionK);

		options.addOption("T", true, "Minimum size of line to not be considered part of slope");
		options.addOption("N", true, "Maximum length of a line");
		options.addOption("h", false, "Prints this help and exit");
		return options;
	}

	private void printHelpAndExit() {
		HelpFormatter help = new HelpFormatter();
		help.printHelp(60, "aztec", "AZTEC algorithm encoder", getCommandLineOptions(), "Feel free to send any comments to lreal at ime dot usp dot br", true);
		System.exit(0);
	}
}
