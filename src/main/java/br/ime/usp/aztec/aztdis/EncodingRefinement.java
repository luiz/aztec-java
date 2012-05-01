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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the second phase of the AZTDIS algorithm. Here we refine
 * the resulting encoding of the AZTEC algorithm by comparing the linear
 * approximation given by it and the original signal. Samples where the
 * displacement is too large are marked as important and added to the encoding.
 * 
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class EncodingRefinement {

	/**
	 * Refines the given encoding based on the given signal, yielding a new
	 * encoding with significant samples that were not in the given encoding. A
	 * sample is said to be significant if the difference of value between it
	 * and the approximation given by the encoding in the same instant is bigger
	 * than the threshold given as parameter.
	 * 
	 * @param encoding
	 *            Encoding to be refined
	 * @param signal
	 *            Signal to be used as reference for refinement
	 * @param threshold
	 *            Maximum acceptable distance between a sample of the signal and
	 *            the correspondent sample of the encoding
	 * @return A new encoding with significant samples added
	 */
	public Iterable<Double> refine(Iterable<Double> encoding,
			Iterable<Double> signal, double threshold) {
		List<Double> refinement = new LinkedList<Double>();
		Iterator<Double> encodingIterator = encoding.iterator();
		Slope currentSlope = this.setup(refinement, encodingIterator);
		SignalReader signalReader = new SignalReader(signal.iterator());
		while (signalReader.hasNext()) {
			signalReader.step();
			if (signalReader.tooFarFromSlopeNow(currentSlope, threshold)) {
				signalReader.refineAndStepTo(refinement, currentSlope.nextT());
				currentSlope.outputRightBoundTo(refinement);
				currentSlope = currentSlope.makeNext(encodingIterator.next(),
						encodingIterator.next());
			} else if (currentSlope.outOfBounds(signalReader.currentT())) {
				currentSlope.outputRightBoundTo(refinement);
				if (encodingIterator.hasNext()) {
					currentSlope = currentSlope.makeNext(
							encodingIterator.next(), encodingIterator.next());
				}
			}
		}
		return refinement;
	}

	private Slope setup(List<Double> refined, Iterator<Double> encodingIterator) {
		double firstX = encodingIterator.next();
		double firstY = encodingIterator.next();
		refined.add(firstX);
		refined.add(firstY);
		Slope currentSlope = new Slope(firstX, firstY, encodingIterator.next(),
				encodingIterator.next());
		return currentSlope;
	}

	private static class SignalReader {
		private int t = -1;
		private double current;
		private final Iterator<Double> values;

		SignalReader(Iterator<Double> values) {
			this.values = values;
			this.current = Double.NaN;
		}

		boolean hasNext() {
			return this.values.hasNext();
		}

		int currentT() {
			return this.t;
		}

		void step() {
			this.t++;
			this.current = this.values.next();
		}

		boolean tooFarFromSlopeNow(Slope s, double threshold) {
			return Math.abs(this.current - s.valueAt(this.t)) > threshold;
		}

		void refineAndStepTo(List<Double> refinement, double newT) {
			refinement.add((double) this.t);
			refinement.add(this.current);
			while (this.t < newT) {
				this.step();
			}
		}
	}

	private static class Slope {
		private final double x0;
		private final double y0;
		private final double x1;
		private final double y1;
		private final double m;

		Slope(double x0, double y0, double x1, double y1) {
			this.x0 = x0;
			this.y0 = y0;
			this.x1 = x1;
			this.y1 = y1;
			this.m = (y1 - y0) / (x1 - x0);
		}

		void outputRightBoundTo(List<Double> refinement) {
			refinement.add(this.x1);
			refinement.add(this.y1);
		}

		Slope makeNext(double newX1, double newY1) {
			return new Slope(this.x1, this.y1, newX1, newY1);
		}

		double valueAt(double x) {
			return this.m * (x - this.x0) + this.y0;
		}

		int nextT() {
			return (int) this.x1;
		}

		boolean outOfBounds(double x) {
			return x < this.x0 || x >= this.x1;
		}
	}

}
