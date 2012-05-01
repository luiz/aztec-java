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

import java.io.IOException;

import org.apache.commons.cli.ParseException;

import br.ime.usp.aztec.AlgorithmMain;
import br.ime.usp.aztec.io.PleaseHelpMeException;

/**
 * Entry point for the AZTDIS encoder
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public class AZTDISMain implements AlgorithmMain {

	@Override
	public void run(String[] algorithmArgs) throws IOException {
		AZTDISCommandLineParametersParser parametersParser = new AZTDISCommandLineParametersParser();
		try {
			AZTDISParameters parameters = parametersParser.parse(algorithmArgs);
			new AZTDIS().encode(parameters);
			parameters.getOutput().close();
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			parametersParser.printHelp();
			System.exit(1);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			parametersParser.printHelp();
			System.exit(1);
		} catch (PleaseHelpMeException e) {
			parametersParser.printHelp();
		}
	}
}
