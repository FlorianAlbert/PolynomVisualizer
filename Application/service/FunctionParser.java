package service;

public class FunctionParser {

	private double[] factors;

	private boolean currentlyReadingFactor;
	private boolean isFirstCharacter;
	private boolean exponentIsZero;

	private StringBuffer factorBuffer;
	private StringBuffer exponentBuffer;

	private boolean isExponent;

	private int degree;

	public static boolean checkTerm(String term) {

		return term.matches(
				"[+-]?\\d*((?<=\\d)(\\.\\d+))?(((?<!\\d)([xX](\\^\\+?\\d+)?))|((?<=\\d)(([xX](\\^\\+?\\d+)?)?)))([+-]\\d*((?<=\\d)(\\.\\d+))?(((?<!\\d)([xX](\\^\\+?\\d+)?))|((?<=\\d)(([xX](\\^\\+?\\d+)?)?))))*");
	}

	public double[] parse(String term) {
		factors = new double[getDegreeOfTerm(term) + 1];

		return analyseTerm(term);
	}

	private double[] analyseTerm(String term) {
		currentlyReadingFactor = true;
		boolean signBelongsToFactor = true;
		isFirstCharacter = true;
		exponentIsZero = true;

		factorBuffer = new StringBuffer();
		exponentBuffer = new StringBuffer();

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
					
					readFactorAndExponentIfNecessary();
					
					factorBuffer.append(character);

				} else {
					signBelongsToFactor = true;
					exponentBuffer.append(character);
				}

			} else if (character == '+') {
				if (signBelongsToFactor) {
					readFactorAndExponentIfNecessary();

				} else {
					signBelongsToFactor = true;
				}

			} else if (character == '^') {
				currentlyReadingFactor = false;
				signBelongsToFactor = false;
			}
		}

		readFactorAndExponent();

		return factors;
	}

	private void readFactorAndExponent() {
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
	}

	private void readFactorAndExponentIfNecessary() {
		if (isFirstCharacter) {
			isFirstCharacter = false;
		} else {
			readFactorAndExponent();

			exponentBuffer = new StringBuffer();
			factorBuffer = new StringBuffer();
			currentlyReadingFactor = true;
			exponentIsZero = true;

		}
	}

	private int getDegreeOfTerm(String term) {
		exponentBuffer = new StringBuffer();
		isExponent = false;
		boolean signBelongsToExponent = false;
		exponentIsZero = true;

		degree = 0;

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
				updateDegreeIfNecessaryWithResetOfVariables();
			} else if (character == '+') {
				if (signBelongsToExponent) {
					signBelongsToExponent = false;
				} else {
					updateDegreeIfNecessaryWithResetOfVariables();
				}
			} else if (character == 'x' || character == 'X') {
				exponentIsZero = false;
			}
		}

		updateDegreeIfNecessary();

		return degree;
	}

	private void updateDegreeIfNecessaryWithResetOfVariables() {
		updateDegreeIfNecessary();

		exponentBuffer = new StringBuffer();
		isExponent = false;
		exponentIsZero = true;
	}

	private void updateDegreeIfNecessary() {
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
	}
}
