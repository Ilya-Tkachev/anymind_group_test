package com.example.demo.validation

enum class AmountValidation(override val message: String, override val check: (value: Double) -> Boolean) : CheckSingle<Double> {
    ZERO("Amount is zero", { it == 0.0 }),
    NEGATIVE("Amount is negative", { it < 0.0 }),
    TOO_BIG("Amount is too big", { it >= Double.MAX_VALUE })
}