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

/**
 * Thrown by {@link CommandLineParametersParser} if the user needs help to
 * operate the program.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public class PleaseHelpMeException extends Exception {
	private static final long serialVersionUID = -8551139424413834443L;

	/**
	 * Initializes the exception with a funny message
	 */
	public PleaseHelpMeException() {
		// this should appear to the user only if we don't handle this exception
		super("Y U NO give me help?");
	}
}
