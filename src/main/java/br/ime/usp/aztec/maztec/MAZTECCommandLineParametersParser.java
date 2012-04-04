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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import br.ime.usp.aztec.CommandLineParametersParser;
import br.ime.usp.aztec.io.SignalParser;
import br.ime.usp.aztec.io.WriterEncodingOutput;
import br.ime.usp.aztec.maztec.MAZTECParameters.Builder;

/**
 * Handles command-line options for the improved AZTEC algorithm and prints a
 * help message for them.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class MAZTECCommandLineParametersParser extends
		CommandLineParametersParser<MAZTECParameters> {

	@Override
	protected MAZTECParameters buildParameters(CommandLine options) {
		Builder builder = new MAZTECParameters.Builder();
		if (options.hasOption('I')) {
			builder.improved();
		}
		return builder
				.withInput(new SignalParser(this.openInputGivenIn(options)))
				.withOutput(
						new WriterEncodingOutput(this
								.openOutputGivenIn(options)))
				.withMinimumThreshold(
						Double.parseDouble(options.getOptionValue('t',
								String.valueOf(MAZTECParameters.DEFAULT_T_MIN))))
				.withMaximumThreshold(
						Double.parseDouble(options.getOptionValue('T',
								String.valueOf(MAZTECParameters.DEFAULT_T_MAX))))
				.withInitialThreshold(
						Double.parseDouble(options.getOptionValue('0', String
								.valueOf(MAZTECParameters.DEFAULT_INITIAL_T))))
				.withCriterionFunctionWeight(
						Double.parseDouble(options.getOptionValue('1',
								String.valueOf(MAZTECParameters.DEFAULT_C1))))
				.withLastThresholdWeight(
						Double.parseDouble(options.getOptionValue('2',
								String.valueOf(MAZTECParameters.DEFAULT_C2))))
				.build();
	}

	@Override
	protected void addCustomCommandLineOptions(Options defaultOptions) {
		defaultOptions
				.addOption("t", true,
						"Defines the minimum value the threshold can assume. Defaults to 0.0");
		defaultOptions.addOption("T", true,
				"Defines the maximum value the threshold can assume."
						+ " Defaults to infinity (no limit)");
		defaultOptions
				.addOption(
						"0",
						true,
						"Defines the initial value for the threshold."
								+ " Must be between the minimum and the maximum values for the threshold");
		defaultOptions.addOption("1", true,
				"Defines the weight of the criterion function (the constant c1)."
						+ " Defaults to 1");
		defaultOptions.addOption("2", true,
				"Defines the weight of the last threshold in"
						+ " the calculation of the new one (the constant c2)."
						+ " Defaults to 0.08");
		defaultOptions.addOption("I", false,
				"Use the improved version of modified AZTEC algorithm");
	}

	@Override
	public void printHelp() {
		HelpFormatter help = new HelpFormatter();
		help.printHelp(60, "MAZTEC", "Modified AZTEC algorithm encoder",
				this.getCommandLineOptions(),
				"Feel free to send any comments to "
						+ "lreal at ime dot usp dot br", true);
	}
}
