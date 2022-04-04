package com.example.demo.service

import com.example.demo.repository.BtcRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@Service
class RestoreService(@Autowired val repository: BtcRepository) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    suspend fun fillGapsIfRequired() {
        logger.info("Checking for restoration.")
        val latestRecord = repository.getLatestStatistic()
        val hourBefore = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS).minusHours(1)
        if (latestRecord.dateTime.isBefore(hourBefore)) {
            val gapStart = latestRecord.dateTime.plusHours(1)
            repository.fillGapBetweenDates(latestRecord.amount, gapStart, hourBefore)
            logger.info("Filled in gap between $gapStart and $hourBefore with value ${latestRecord.amount}.")
        } else {
            logger.info("We are up to date.")
        }
    }
}