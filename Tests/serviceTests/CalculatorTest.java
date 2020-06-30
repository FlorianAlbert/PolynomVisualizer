package serviceTests;

import org.junit.Assert;
import org.junit.Test;

import service.Calculator;

public class CalculatorTest {

	@Test
	public void testCalc() {
		Calculator calc = new Calculator();
		calc.setTerm("x+2x^2-2x^2");
		for (int i = 0; i <= 100; i++) {
			int value = (int) (5000 * Math.random());
			System.out.println(calc.calculateValue(i) + " = " + i);
			Assert.assertEquals(value, calc.calculateValue(value), 0.0001);
		}
	}
}