package org.example.services

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.BehaviorSpec
import io.kotlintest.specs.FreeSpec

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

class CalculatorK2Test: FreeSpec({
    "given a new calculator" - {
        val calculator = Calculator()

        "when i calculate, it throws an exception" {
            shouldThrow<IllegalStateException> {
                calculator.calculate("+")
            }
        }

        "when i get the last result, it throws" {
            shouldThrow<IllegalStateException> {
                calculator.lastResult
            }
        }
    }

    "given a calculator with an entered number, when i add a numder and calculate, i get the result" {
        val calculator = Calculator().apply {
            enterNumber(2)
            enterNumber(2)
            calculate("+")
            lastResult shouldBe 4
        }

    }
})