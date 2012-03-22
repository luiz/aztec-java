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

import org.apache.commons.cli.ParseException;

/**
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class AZTECMain {

	public static void main(String[] args) throws Exception {
		CommandLineParametersParser parser = new CommandLineParametersParser();
		try {
			AZTECParameters params = parser.parse(args);
			new AZTEC(params).encode(params.getInput());
			params.getOutput().close();
		} catch (PleaseHelpMeException e) {
			parser.printHelp();
		} catch (ParseException e) {
			parser.printHelp();
			System.exit(1);
		} catch (IllegalArgumentException e) {
			handleIOError(e);
		} catch (ReadOnlyOutputException e) {
			handleIOError(e);
		}
	}

	private static void handleIOError(RuntimeException e) {
		System.err.println(e.getMessage());
		System.exit(2);
	}

}
