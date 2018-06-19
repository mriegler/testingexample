package org.example.services

import io.kotlintest.shouldThrow
import io.kotlintest.specs.BehaviorSpec

class CalculatorKTest: BehaviorSpec({
    Given("a new Calculator") {
        val calculator = Calculator()
        When("i calculate") {
            Then("it throws an exception") {
                shouldThrow<IllegalStateException> {
                    calculator.calculate("+")
                }
            }
        }
        When("i get the last result") {
            Then("it throws") {
                shouldThrow<IllegalStateException> {
                    calculator.lastResult
                }
            }
        }

    }
})