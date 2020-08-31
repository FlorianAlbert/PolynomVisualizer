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

	public String getDerivative(int n) {
		return calculateDerivative(n, factors);
	}

	private String calculateDerivative(int n, double[] factors) {
		double[] buffer = factors;

		String result;
		if (buffer.length > 1) {
			factors = new double[buffer.length - 1];

			for (int i = 0; i < factors.length; i++) {
				factors[i] = buffer[i + 1] * (i + 1);
			}

			if (n > 1) {
				return calculateDerivative(n - 1, factors);
			} else {
				if (factors.length - 1 >= 2) {
					result = Double.toString(factors[factors.length - 1]).concat("x^")
							.concat(Integer.toString(factors.length - 1));
				} else if (factors.length - 1 == 1) {
					result = Double.toString(factors[factors.length - 1]).concat("x");
				} else if (factors.length - 1 == 0) {
					result = Double.toString(factors[factors.length - 1]);
				} else {
					result = "0";
				}
				for (int i = factors.length - 2; i >= 0; i--) {
					if (factors[i] != 0) {
						if (factors[i] > 0) {
							result = result.concat("+");
						}

						if (i >= 2) {
							result = result.concat(Double.toString(factors[i])).concat("x^")
									.concat(Integer.toString(i));
						} else if (i == 1) {
							result = result.concat(Double.toString(factors[1])).concat("x");
						} else {
							result = result.concat(Double.toString(factors[0]));
						}
					}
				}

			}
		} else {
			result = "0";
		}

		return result;
	}
}