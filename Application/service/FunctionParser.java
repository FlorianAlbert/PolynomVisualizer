package service;

public class FunctionParser {

	private double[] factors;

	public double[] parse(String term) {
		factors = new double[getDegreeOfTerm(term) + 1];

		return analyseTerm(term);
	}

	private double[] analyseTerm(String term) {
		boolean currentlyReadingFactor = true;
		boolean signBelongsToFactor = true;
		boolean isFirstCharacter = true;
		boolean exponentIsZero = true;

		StringBuffer factorBuffer = new StringBuffer();
		StringBuffer exponentBuffer = new StringBuffer();

		for (char character : term.toCharArray()) {
			if (Character.isDigit(character) || character == '.') {
				if (currentlyReadingFactor) {
					if (isFirstCharacter) {
						isFirstCharacter = false;
					}

					factorBuffer.append(character);
				} else {
					exponentBuffer.append(character);
					signBelongsToFactor = true;
				}
			} else if (character == 'x' || character == 'X') {
				if (isFirstCharacter) {
					isFirstCharacter = false;
				}

				exponentIsZero = false;

			} else if (character == '-') {
				if (signBelongsToFactor) {
					if (isFirstCharacter) {
						isFirstCharacter = false;
						factorBuffer.append(character);
					} else {
						double factor;
						if (factorBuffer.toString().isEmpty()) {
							factor = 1;
						} else if (factorBuffer.toString().equals("-")) {
							factor = -1;
						} else {
							factor = Double.parseDouble(factorBuffer.toString());
						}

						int exponent;
						if (!exponentBuffer.toString().isEmpty()) {
							exponent = Integer.parseInt(exponentBuffer.toString());
						} else {
							if (exponentIsZero) {
								exponent = 0;
							} else {
								exponent = 1;
							}
						}

						factors[exponent] += factor;

						exponentBuffer = new StringBuffer();
						factorBuffer = new StringBuffer();
						currentlyReadingFactor = true;
						exponentIsZero = true;
						factorBuffer.append(character);

					}

				} else {
					signBelongsToFactor = true;
					exponentBuffer.append(character);
				}

			} else if (character == '+') {
				if (signBelongsToFactor) {
					if (isFirstCharacter) {
						isFirstCharacter = false;
					} else {
						double factor;
						if (factorBuffer.toString().isEmpty()) {
							factor = 1;
						} else if (factorBuffer.toString().equals("-")) {
							factor = -1;
						} else {
							factor = Double.parseDouble(factorBuffer.toString());
						}

						int exponent;
						if (!exponentBuffer.toString().isEmpty()) {
							exponent = Integer.parseInt(exponentBuffer.toString());
						} else {
							if (exponentIsZero) {
								exponent = 0;
							} else {
								exponent = 1;
							}
						}

						factors[exponent] += factor;

						exponentBuffer = new StringBuffer();
						factorBuffer = new StringBuffer();
						currentlyReadingFactor = true;
						exponentIsZero = true;

					}

				} else {
					signBelongsToFactor = true;
				}

			} else if (character == '^') {
				currentlyReadingFactor = false;
				signBelongsToFactor = false;
			}
		}

		double factor;
		if (factorBuffer.toString().isEmpty()) {
			factor = 1;
		} else if (factorBuffer.toString().equals("-")) {
			factor = -1;
		} else {
			factor = Double.parseDouble(factorBuffer.toString());
		}

		int exponent;
		if (!exponentBuffer.toString().isEmpty()) {
			exponent = Integer.parseInt(exponentBuffer.toString());
		} else {
			if (exponentIsZero) {
				exponent = 0;
			} else {
				exponent = 1;
			}
		}

		factors[exponent] += factor;

		return factors;
	}

	public boolean checkTerm(String term) {
		boolean termIsValid = term
				.matches("[+-]?\\d*((?<=\\d)(\\.\\d+))?(((?<!\\d)([xX](\\^[+-]?\\d+)?))|((?<=\\d)(([xX](\\^[+-]?\\d+)?)?)))([+-]\\d*((?<=\\d)(\\.\\d+))?(((?<!\\d)([xX](\\^[+-]?\\d+)?))|((?<=\\d)(([xX](\\^[+-]?\\d+)?)?))))*");

		return termIsValid;
	}

	private int getDegreeOfTerm(String term) {
		StringBuffer exponentBuffer = new StringBuffer();
		boolean isExponent = false;
		boolean signBelongsToExponent = false;
		boolean exponentIsZero = true;

		int degree = 0;

		for (char character : term.toCharArray()) {
			if (Character.isDigit(character)) {
				if (isExponent) {
					exponentBuffer.append(character);
					signBelongsToExponent = false;
				}
			} else if (character == '^') {
				isExponent = true;
				signBelongsToExponent = true;
			} else if (character == '-') {
				int comparingExponent;
				if (exponentBuffer.toString().isEmpty()) {
					if (exponentIsZero) {
						comparingExponent = 0;
					} else {
						comparingExponent = 1;
					}
				} else {
					comparingExponent = Integer.parseInt(exponentBuffer.toString());
				}

				if (comparingExponent > degree) {
					degree = comparingExponent;
				}

				exponentBuffer = new StringBuffer();
				isExponent = false;
				exponentIsZero = true;
			} else if (character == '+') {
				if (signBelongsToExponent) {
					signBelongsToExponent = false;
				} else {
					int comparingExponent;
					if (exponentBuffer.toString().isEmpty()) {
						if (exponentIsZero) {
							comparingExponent = 0;
						} else {
							comparingExponent = 1;
						}
					} else {
						comparingExponent = Integer.parseInt(exponentBuffer.toString());
					}

					if (comparingExponent > degree) {
						degree = comparingExponent;
					}

					exponentBuffer = new StringBuffer();
					isExponent = false;
					exponentIsZero = true;
				}
			} else if (character == 'x' || character == 'X') {
				exponentIsZero = false;
			}
		}

		int comparingExponent;
		if (exponentBuffer.toString().isEmpty()) {
			if (exponentIsZero) {
				comparingExponent = 0;
			} else {
				comparingExponent = 1;
			}
		} else {
			comparingExponent = Integer.parseInt(exponentBuffer.toString());
		}

		if (comparingExponent > degree) {
			degree = comparingExponent;
		}

		return degree;
	}
}
