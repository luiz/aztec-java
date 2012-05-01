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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Converts an encoding given by the AZTEC algorithm, which is composed by
 * horizontal lines and slopes, into an encoding that indicates simply the
 * significant samples. This way, the signal between each pair of samples is
 * always linearly interpolated.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class AZTECToSampleConverter {

	/**
	 * @param aztecEncoding
	 *            The AZTEC-encoded signal to be converted
	 * @return The significant samples represented in the given encoding
	 */
	public Iterable<Double> convert(Iterable<Double> aztecEncoding) {
		List<Double> converted = new LinkedList<Double>();
		Iterator<Double> iterator = aztecEncoding.iterator();
		if (!iterator.hasNext()) {
			return Collections.emptyList();
		}
		double currentPosition = 0.0;
		double lastSignificantSample = 0.0;
		while (iterator.hasNext()) {
			double sampleDuration = iterator.next();
			double sampleValue = iterator.next();
			converted.add(currentPosition);
			if (sampleDuration < 0) {
				sampleDuration *= -1.0;
				converted.add(lastSignificantSample);
				lastSignificantSample += sampleValue;
			} else {
				converted.add(sampleValue);
				lastSignificantSample = sampleValue;
			}
			currentPosition += sampleDuration;
		}
		converted.add(currentPosition - 1.0);
		converted.add(lastSignificantSample);
		return converted;
	}

}
