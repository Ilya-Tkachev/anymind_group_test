package com.example.demo.model

import com.example.demo.validation.DateStartEndValidation
import com.example.demo.validation.DateValidation
import com.example.demo.validation.Validatable
import java.math.BigDecimal
import java.time.ZonedDateTime

data class StatusRequest(
    val startDatetime: ZonedDateTime,
    val endDatetime: ZonedDateTime
) : Validatable {
    override fun validate(): List<String> {

        val dateViolations = listOf(endDatetime, startDatetime)
            .map { field ->
                DateValidation.values()
                    .filter { it.check(field) }
                    .map { it.message }
            }
            .flatten()

        val startEndViolations = DateStartEndValidation.values()
            .filter { it.check(startDatetime, endDatetime) }
            .map { it.message }

        return dateViolations + startEndViolations
    }
}

data class StatusResponse(
    val dateTime: ZonedDateTime,
    val amount: BigDecimal
)