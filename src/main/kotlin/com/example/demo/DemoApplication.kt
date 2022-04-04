package com.example.demo

import com.example.demo.service.RestoreService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.ZonedDateTime

@SpringBootApplication
class DemoApplication {
    companion object{
        val APPLICATION_CREATION_DATE: ZonedDateTime = ZonedDateTime.parse("2022-04-01T01:00:00+03:00")
    }
}

suspend fun main(args: Array<String>) {
    val app = runApplication<DemoApplication>(*args)
    val restoreService = app.getBean(RestoreService::class.java)
    restoreService.fillGapsIfRequired()
}