package service;

public class Calculator {
	public Calculator() {
		
	}
	
	boolean isFactor = true;
	boolean plusMinusAfterPowerChar = false;
	String factorBuffer = "";
	String exponentBuffer = "";
	public double calculate(String term, double input) {
		if (checkTerm(term)) {
			for (char character: term.toCharArray()) {
				if (Character.isDigit(character)) {
					if (isFactor) {
						factorBuffer += character;
					} else {
						exponentBuffer += character;
					}
				} else if (character == '-') {
					if (plusMinusAfterPowerChar) {
						exponentBuffer = Character.toString(character);
						plusMinusAfterPowerChar = false;
					} else {
						// Exponent in Array schreiben oder Teillösung berechnen
						exponentBuffer = "";
						isFactor = true;
						factorBuffer =Character.toString(character);
					}
				} else if (character == '+') {
					if (plusMinusAfterPowerChar) {
						plusMinusAfterPowerChar = false;
					} else {
						// Exponent und Faktor in Array schreiben oder Teillösung berechnen
						exponentBuffer = "";
						isFactor = true;
					}
				} else if (character == '^') {
					isFactor = false;
					plusMinusAfterPowerChar = true;
				}
			}
			return 0;
		}
		return 0;
	}
	
	private boolean checkTerm(String term) {
		String[] polynoms = term.split("");
		
		boolean allPolynomsValid = true;
		for (String polynom: polynoms) {
			allPolynomsValid &= polynom.matches("");
		}
		
		return allPolynomsValid;
	}
}
