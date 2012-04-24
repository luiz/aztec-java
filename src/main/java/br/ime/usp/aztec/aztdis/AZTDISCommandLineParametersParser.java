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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import br.ime.usp.aztec.CommandLineParametersParser;
import br.ime.usp.aztec.io.SignalParser;
import br.ime.usp.aztec.io.WriterEncodingOutput;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class AZTDISCommandLineParametersParser extends
		CommandLineParametersParser<AZTDISParameters> {

	@Override
	protected AZTDISParameters buildParameters(CommandLine options)
			throws IllegalArgumentException {
		if (options.hasOption('t') && options.hasOption('e')) {
			double threshold = Double.parseDouble(options.getOptionValue('t'));
			double displacement = Double.parseDouble(options
					.getOptionValue('e'));
			double minimumDistance = Double.parseDouble(options.getOptionValue(
					'k', Double.toString(AZTDISParameters.DEFAULT_K)));
			return new AZTDISParameters.Builder()
					.withThreshold(threshold)
					.withMaximumDisplacement(displacement)
					.withMinimumDistanceBetweenEvents(minimumDistance)
					.withInput(new SignalParser(this.openInputGivenIn(options)))
					.withOutput(
							new WriterEncodingOutput(this
									.openOutputGivenIn(options))).build();
		}
		throw new IllegalArgumentException("Mandatory argument not given."
				+ " Threshold and maximum displacement are mandatory.");
	}

	@Override
	protected void addCustomCommandLineOptions(Options defaultOptions) {
		defaultOptions.addOption("t", true,
				"Maximum variation of the signal between event points");
		defaultOptions
				.addOption(
						"e",
						true,
						"Maximum displacement between a value of the encoded signal"
								+ " and the original signal. Should be >= 3"
								+ " (or 0.3, according to the scale of the ECG you want to encode).");
		defaultOptions
				.addOption(
						"k",
						true,
						"Minimum distance between two consecutive event points."
								+ " If an event point is found before reaching this distance,"
								+ " it's considered a candidate point.");
	}

	@Override
	public void printHelp() {
		HelpFormatter help = new HelpFormatter();
		help.printHelp(60, "aztdis", "AZTDIS algorithm encoder",
				this.getCommandLineOptions(),
				"Feel free to send any comments to "
						+ "lreal at ime dot usp dot br", true);
	}

}
