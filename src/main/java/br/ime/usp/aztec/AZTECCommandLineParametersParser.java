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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import br.ime.usp.aztec.AZTECParameters.OptionalParametersBuilder;
import br.ime.usp.aztec.io.SignalParser;
import br.ime.usp.aztec.io.WriterEncodingOutput;

/**
 * Handles command-line options for the AZTEC algorithm, such as minimum and
 * maximum line length
 * 
 * @author Luiz Fernando Oliveira Corte Real
 * @see AZTEC
 */
public final class AZTECCommandLineParametersParser extends
		CommandLineParametersParser<AZTECParameters> {

	private static final String IGNORED_VALUE_JUST_FOR_DECODING = "0";

	@Override
	protected AZTECParameters buildParameters(CommandLine options)
			throws IllegalArgumentException {
		if (!options.hasOption('K') && !options.hasOption('d')) {
			throw new IllegalArgumentException("Mandatory argument not given");
		}
		OptionalParametersBuilder builder = new AZTECParameters.Builder()
				.withMaximumAcceptableVariation(
						Double.parseDouble(options.getOptionValue('K',
								IGNORED_VALUE_JUST_FOR_DECODING)))
				.withInput(new SignalParser(this.openInputGivenIn(options)))
				.withOutput(
						new WriterEncodingOutput(this
								.openOutputGivenIn(options)))
				.withMaximumSlopeLineSize(
						Double.parseDouble(options.getOptionValue('T',
								String.valueOf(AZTECParameters.DEFAULT_T))))
				.withMaximumLineLength(
						Double.parseDouble(options.getOptionValue('N',
								String.valueOf(AZTECParameters.DEFAULT_N))));
		if (options.hasOption('d')) {
			builder.decoding();
		}
		return builder.build();
	}

	@Override
	protected void addCustomCommandLineOptions(Options defaultOptions) {
		defaultOptions.addOption("K", true,
				"Maximum variation of voltage to be considered a line");
		defaultOptions.addOption("T", true,
				"Minimum size of line to not be considered part of slope. "
						+ "Defaults to 4 samples");
		defaultOptions.addOption("N", true,
				"Maximum length of a line. Defaults to 25 samples");
		defaultOptions.addOption("d", false, "Decode instead of encode");
	}

	@Override
	public void printHelp() {
		HelpFormatter help = new HelpFormatter();
		help.printHelp(60, "aztec", "AZTEC algorithm encoder",
				this.getCommandLineOptions(),
				"Feel free to send any comments to "
						+ "lreal at ime dot usp dot br", true);
	}
}
