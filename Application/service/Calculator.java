package service;

import java.util.ArrayList;

public class Calculator {

	ArrayList<Double> factors = new ArrayList<Double>();
	ArrayList<Double> exponents = new ArrayList<Double>();

	public Calculator() {

	}

	public double calculate(double input) {
		if (!factors.isEmpty() && !exponents.isEmpty()) {
			double result = 0;

			for (int i = 0; i < factors.size(); i++) {
				result += factors.get(i) * Math.pow(input, exponents.get(i));
			}

			return result;
		}

		return (Double) null;
	}

	public boolean checkTerm(String term) {
		boolean termIsValid = term.matches(
				"[+-]?(\\d+(.\\d+)?)?([xX](\\^[+-]?\\d+(.\\d+)?)?)?([+-](\\d+(.\\d+)?)?([xX](\\^[+-]?\\d+(.\\d+)?)?)?)*");

		return termIsValid;
	}

	public boolean setTerm(String term) {
		boolean termParsable = checkTerm(term);

		if (termParsable) {
			boolean currentlyReadingFactor = true;
			boolean signBelongsToFactor = true;
			boolean isFirstCharacter = true;
			boolean exponentIsZero = true;
			
			StringBuffer factorBuffer = new StringBuffer();
			StringBuffer exponentBuffer = new StringBuffer();

			factors.clear();
			exponents.clear();

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
					if(signBelongsToFactor) {
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
				
							double exponent;
							if (!exponentBuffer.toString().isEmpty()) {
								exponent = Double.parseDouble(exponentBuffer.toString());
							} else {
								if (exponentIsZero) {
									exponent = 0;
								} else {
									exponent = 1;
								}
							}
				
							factors.add(factor);
							exponents.add(exponent);
							
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
					if(signBelongsToFactor) {
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
				
							double exponent;
							if (!exponentBuffer.toString().isEmpty()) {
								exponent = Double.parseDouble(exponentBuffer.toString());
							} else {
								if (exponentIsZero) {
									exponent = 0;
								} else {
									exponent = 1;
								}
							}
				
							factors.add(factor);
							exponents.add(exponent);
							
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

			double exponent;
			if (!exponentBuffer.toString().isEmpty()) {
				exponent = Double.parseDouble(exponentBuffer.toString());
			} else {
				if (exponentIsZero) {
					exponent = 0;
				} else {
					exponent = 1;
				}
			}

			factors.add(factor);
			exponents.add(exponent);
			
		}

		return termParsable;
	}
}