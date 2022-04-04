package com.example.demo.validation

import java.time.ZonedDateTime

enum class DateStartEndValidation(override val message: String, override val check: (start: ZonedDateTime, end: ZonedDateTime) -> Boolean)
    : CheckTuple<ZonedDateTime, ZonedDateTime> {
    START_AFTER_END("Start is after end, dates are probably mixed", { start, end -> start.isAfter(end) })
}