package com.example.demo.repository

import com.example.demo.model.BtcRequest
import com.example.demo.model.StatusResponse
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.await
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.ZonedDateTime

@Repository
class BtcRepository(private val client: DatabaseClient) {

    suspend fun statusInPeriod(periodStart: ZonedDateTime, periodEnd: ZonedDateTime) =
        client.sql("select status_at, amount from public.statistic where status_at between $1 and $2")
            .bind(0, periodStart)
            .bind(1, periodEnd)
            .map { row ->
                StatusResponse(
                    dateTime = row.get("status_at", ZonedDateTime::class.java)!!,
                    amount = row.get("amount", BigDecimal::class.java)!!
                )
            }
            .flow()

    suspend fun save(record: BtcRequest) =
        client.sql("INSERT INTO incoming (received, amount) VALUES($1, $2)")
            .bind(0, record.datetime)
            .bind(1, record.amount)
            .then()
            .awaitSingleOrNull()

    suspend fun getLatestStatistic(): StatusResponse = client.sql("select status_at, amount from statistic order by status_at DESC limit 1")
            .map { row -> StatusResponse(row.get("status_at", ZonedDateTime::class.java)!!, row.get("amount", BigDecimal::class.java)!!) }
            .one()
            .awaitSingle()

    suspend fun fillGapBetweenDates(amount: BigDecimal, startDate: ZonedDateTime, finishDate: ZonedDateTime) =
        client.sql("insert into statistic(status_at, amount) select generate_series as status_at, $1 as amount from generate_series($2, $3, INTERVAL '1 hour')")
            .bind(0, amount)
            .bind(1, startDate)
            .bind(2, finishDate)
            .await()

    // blocking!
    fun gatherStatistics(startDate: ZonedDateTime, finishDate: ZonedDateTime) =
        client.sql("select date_trunc('hour', received) as date_time, sum(amount) as amount from public.incoming where received between $1 and $2 group by 1")
            .bind(0, startDate)
            .bind(1, finishDate)
            .map { row ->
                StatusResponse(
                    dateTime = row.get("date_time", ZonedDateTime::class.java)!!,
                    amount = row.get("amount", BigDecimal::class.java)!!
                )
            }
            .one()
            .block()

    // blocking!
    fun saveStatistics(res: StatusResponse) =
        client.sql("insert into statistic(status_at, amount) select status_at + interval '1 hour', amount + $1 from statistic where status_at = $2")
            .bind(0, res.amount)
            .bind(1, res.dateTime)
            .then()
            .block()

    // blocking!
    fun saveDefaultStatistics() =
        client.sql("insert into statistic(status_at, amount) select status_at + interval '1 hour', amount from statistic order by status_at DESC limit 1")
            .then()
            .block()

}