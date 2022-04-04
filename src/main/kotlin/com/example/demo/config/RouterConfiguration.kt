package com.example.demo.config

import com.example.demo.handler.BtcHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfiguration {

    @Bean
    fun routes(service: BtcHandler) = coRouter {
        "/btc".nest {
            GET("/ping", service::ping)
            POST("/save", service::save)
            POST("/list", service::list)
        }
    }

}