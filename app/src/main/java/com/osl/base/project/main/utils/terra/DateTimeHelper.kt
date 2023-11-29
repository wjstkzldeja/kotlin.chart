package com.osl.base.project.main.utils.terra

import java.time.*
import java.time.LocalTime.MAX
import java.time.temporal.ChronoUnit

fun startOfDayInstant(zoneId: String): Instant {
    val midnight = LocalDate.now().atStartOfDay().atZone(ZoneId.of(zoneId))
    return midnight.toInstant()
}

fun endOfDayInstant(zoneId: String): Instant {
    val max = LocalDate.now().atTime(MAX).atZone(ZoneId.of(zoneId))
    return max.toInstant()
}

fun startOfDayLocalDate(zoneId: String): LocalDateTime {
    val midnight = LocalDate.now().minusDays(1L).atStartOfDay().atZone(ZoneId.of(zoneId))
    return midnight.toLocalDateTime()
}

fun endOfDayLocalDate(zoneId: String): LocalDateTime {
    val max = LocalDate.now().atTime(MAX).atZone(ZoneId.of(zoneId))
    return max.toLocalDateTime()
}

fun getTimeDuration(timestamp: String): Long {
    val start = ZonedDateTime.parse(timestamp)
    val end = start.plusSeconds(5L)
    return ChronoUnit.SECONDS.between(start, end)
}