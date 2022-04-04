package com.example.demo.service

import com.example.demo.repository.BtcRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@Service
class StatisticService(@Autowired val repository: BtcRepository) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(cron = "0 1 * * * *") // Every hour
    fun gatherStatistic() {
        logger.info("Started gathering statistics.")
        val now = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS)
        val previousHour = now.minusHours(1)

        val res = repository.gatherStatistics(previousHour, now)
        if (res != null) {
            repository.saveStatistics(res)
            logger.info("Gathered statistic by $now with amount ${res.amount}.")
        } else {
            repository.saveDefaultStatistics()
            logger.info("No amount change by $now")
        }
    }
}