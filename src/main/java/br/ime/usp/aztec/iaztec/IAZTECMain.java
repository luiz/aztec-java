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

import org.apache.commons.cli.ParseException;

import br.ime.usp.aztec.io.PleaseHelpMeException;
import br.ime.usp.aztec.io.ReadOnlyOutputException;

/**
 * Entry point for the improved AZTEC algorithm encoder
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class IAZTECMain {

	public static void main(String[] args) throws Exception {
		IAZTECCommandLineParametersParser parser = new IAZTECCommandLineParametersParser();
		try {
			IAZTECParameters params = parser.parse(args);
			// new IAZTEC().encode(params);
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