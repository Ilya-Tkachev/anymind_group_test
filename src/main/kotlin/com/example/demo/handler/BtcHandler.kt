package com.example.demo.handler

import com.example.demo.model.BtcRequest
import com.example.demo.model.StatusRequest
import com.example.demo.repository.BtcRepository
import com.example.demo.validation.Validatable
import kotlinx.coroutines.flow.flowOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import java.time.ZonedDateTime

@Component
class BtcHandler(@Autowired val repository: BtcRepository) {

    suspend fun ping(request: ServerRequest): ServerResponse {
        return ok().bodyAndAwait(flowOf("Hello, Ping. ${ZonedDateTime.now()}"))
    }

    suspend fun list(request: ServerRequest): ServerResponse {
        val message = request.awaitBody<StatusRequest>()
        return withValidate(message) {
            val status = repository.statusInPeriod(message.startDatetime, message.endDatetime)
            ok().json().bodyAndAwait(status)
        }
    }

    suspend fun save(request: ServerRequest): ServerResponse {
        val btcRequest = request.awaitBody<BtcRequest>()
        return withValidate(btcRequest) {
            repository.save(btcRequest)
            ok().buildAndAwait()
        }
    }

    private suspend fun withValidate(btcRequest: Validatable, check: suspend (btcRequest: Validatable) -> ServerResponse): ServerResponse {
        val violations = btcRequest.validate()
        return if (violations.isEmpty()) {
            check(btcRequest)
        } else {
            return badRequest().bodyValueAndAwait(violations)
        }
    }

}