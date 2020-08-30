package service;

public class Calculator {

	private double[] factors;
	private FunctionParser parser;

	public Calculator() {
		parser = new FunctionParser();
	}

	public boolean setTerm(String term) {
		boolean termParsable = FunctionParser.checkTerm(term);

		if (termParsable) {
			factors = parser.parse(term);
		}

		return termParsable;
	}

	public double calculateValue(double input) {
		double result = factors[factors.length - 1];

		for (int i = factors.length - 2; i >= 0; i--) {
			result = result * input + factors[i];
		}

		return result;
	}
}