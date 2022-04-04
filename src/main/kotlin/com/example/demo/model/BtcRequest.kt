package com.example.demo.model

import com.example.demo.validation.AmountValidation
import com.example.demo.validation.DateValidation
import com.example.demo.validation.Validatable
import java.time.ZonedDateTime

data class BtcRequest(
    val datetime: ZonedDateTime,
    val amount: Double
) : Validatable {

    override fun validate(): List<String> {
        val dateViolations = DateValidation.values().filter { it.check(datetime) }.map { it.message }
        val amountViolations = AmountValidation.values().filter { it.check(amount) }.map { it.message }
        return dateViolations + amountViolations
    }
}