package serviceTests;

import org.junit.Assert;
import org.junit.Test;

import service.Calculator;

public class CalculatorTest {

	@Test
	public void testCheckTerm() {
		Calculator calc = new Calculator();
		calc.setTerm("-3.5x+x^2");
		for (int i = 0; i <= 100; i++) {
			System.out.println(calc.calculateValue(i) + " = " + (-3.5*Math.pow(i, 1)+Math.pow(i, 2)));
			Assert.assertTrue(calc.calculateValue(i) == -3.5*Math.pow(i, 1)+Math.pow(i, 2));
		}
	}
}