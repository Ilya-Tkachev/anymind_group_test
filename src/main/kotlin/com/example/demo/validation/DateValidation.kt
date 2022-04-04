package com.example.demo.validation

import com.example.demo.DemoApplication.Companion.APPLICATION_CREATION_DATE
import java.time.ZonedDateTime

enum class DateValidation(override val message: String, override val check: (checkValue: ZonedDateTime) -> Boolean) : CheckSingle<ZonedDateTime> {
    FUTURE("Date is from future", { date -> date.isAfter(ZonedDateTime.now()) }),
    OLD("Date is too old, must be no earlier than $APPLICATION_CREATION_DATE", { date -> date.isBefore(APPLICATION_CREATION_DATE) }),
}