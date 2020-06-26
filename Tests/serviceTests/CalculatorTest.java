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
			int value = (int) (5000 * Math.random());
			System.out.println(calc.calculateValue(value) + " = " + (-3.5*Math.pow(value, 1)+Math.pow(value, 2)));
			Assert.assertTrue(calc.calculateValue(value) == -3.5*Math.pow(value, 1)+Math.pow(value, 2));
		}
	}
}