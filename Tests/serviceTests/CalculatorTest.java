package serviceTests;

import org.junit.Assert;
import org.junit.Test;

import service.Calculator;

public class CalculatorTest {

	@Test
	public void testCheckTerm() {
		Calculator calc = new Calculator();
		Assert.assertTrue(calc.checkTerm("x^-3"));
	}
}