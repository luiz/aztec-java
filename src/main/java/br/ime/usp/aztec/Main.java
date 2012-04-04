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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.ime.usp.aztec.maztec.MAZTECMain;

/**
 * Entry point for all the implemented algorithms
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class Main {

	private static Map<String, AlgorithmMain> algorithms;

	static {
		algorithms = new HashMap<String, AlgorithmMain>();
		algorithms.put("AZTEC", new AZTECMain());
		algorithms.put("MAZTEC", new MAZTECMain());
	}

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			printHelp();
			System.exit(0);
		}
		String algorithm = args[0];
		String[] algorithmArgs = removeFirst(args);
		try {
			findAlgorithm(algorithm).run(algorithmArgs);
		} catch (IllegalArgumentException e) {
			System.err.println("Algorithm not found: '" + algorithm + "'");
			System.err.println("Possible algorithms:");
			System.err.print(listAlgorithms());
			System.exit(1);
		}
	}

	private static AlgorithmMain findAlgorithm(String algorithm) {
		if (algorithms.containsKey(algorithm)) {
			return algorithms.get(algorithm);
		}
		throw new IllegalArgumentException();
	}

	private static String listAlgorithms() {
		StringBuilder list = new StringBuilder();
		for (String algorithm : algorithms.keySet()) {
			list.append('\t');
			list.append(algorithm);
			list.append('\n');
		}
		return list.toString();
	}

	private static String[] removeFirst(String[] args) {
		String[] algorithmArgs = Arrays.copyOfRange(args, 1, args.length);
		return algorithmArgs;
	}

	private static void printHelp() {
		System.out.println("usage: encoder <algorithm> <algorithm options>");
		System.out.println("where <algorithm> can be one of:");
		System.out.print(listAlgorithms());
		System.out
				.println("and <algorithm options> depend on the chosen algorithm.");
		System.out.println("For more information on the options accepted by");
		System.out
				.println("each algorithm, pass nothing in <algorithm options>.");
		System.out
				.println("Feel free to send any comments to lreal at ime dot usp dot");
		System.out.println("br");
	}
}
