package org.example.services;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Calculator {
    private List<Integer> numbers = new ArrayList<>();

    private Double lastResult;

    public Double getLastResult() {
        if (lastResult == null) {
            throw new IllegalStateException("there is no last result");
        }
        return lastResult;
    }

    public void calculate(String operation) {
        if (operation == null || operation.length() == 0) {
            throw new InvalidParameterException("calculate requiores an operation");
        }
        if (numbers.size() < 2) {
            throw new IllegalStateException("need two numbers for operation");
        }
        if (numbers.size() > 2) {
            throw new IllegalStateException("adding more than two numbers is currently not supported");
        }

        switch(operation) {
            case "+":
                lastResult = (double) (numbers.get(0) + numbers.get(1));
                break;
            case "*":
                lastResult = (double) (numbers.get(0) * numbers.get(1));
                break;
            default:
                throw new InvalidParameterException("got unknown operator");
        }
    }

    public void enterNumber(Integer number) {
        if (number == null) {
            throw new InvalidParameterException("you need to enter a number");
        }

        numbers.add(number);
    }
}
