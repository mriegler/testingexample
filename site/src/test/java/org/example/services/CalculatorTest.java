package org.example.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void givenNewCalculator_whenIGetLastResult_itThrowsException() {
        Calculator calculator = new Calculator();
        assertThrows(IllegalStateException.class, calculator::getLastResult);
    }

    @Test
    void givenNewCalculator_whenICalculate_itThrowsException() {
        Calculator calculator = new Calculator();
        assertThrows(IllegalStateException.class, () -> calculator.calculate("+"));
    }

    @Test
    void givenNewCalculator_whenIAddTwoNumbersAndAddThem_thenLastResultIsCorrect() {
        Calculator calculator = new Calculator();
        calculator.enterNumber(5);
        calculator.enterNumber(4);
        calculator.calculate("+");
        assertEquals(Integer.valueOf(9), calculator.getLastResult());
    }

    @Test
    void givenCalculatorWithMaxIntValueNumber_whenIAddAnyNumber_thenTheResultOverflows() {
        Calculator calculator = new Calculator();
        calculator.enterNumber(Integer.MAX_VALUE);
        calculator.enterNumber(5);

        calculator.calculate("+");

        assertEquals(Integer.valueOf(Integer.MIN_VALUE + 5 - 1), calculator.getLastResult());
    }

    @Test
    void calculate() {
    }

    @Test
    void enterNumber() {
    }
}