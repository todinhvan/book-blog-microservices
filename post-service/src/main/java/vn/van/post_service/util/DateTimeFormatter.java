package vn.van.post_service.util;

import org.springframework.stereotype.Component;
import vn.van.post_service.constant.ResponseMessage;
import vn.van.post_service.exception.ApplicationException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class DateTimeFormatter {

    private final Map<Long, Function<Instant, String>> strategyMap = new LinkedHashMap<>();

    public DateTimeFormatter() {
        strategyMap.put(60L, this::formatInSeconds);
        strategyMap.put(3600L, this::formatInMinutes);
        strategyMap.put(86400L, this::formatInHours);
        strategyMap.put(Long.MAX_VALUE, this::formatInDate);
    }

    public String format(Instant instant) {
        long seconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        var entry = strategyMap.entrySet().stream()
                .filter(longFunctionEntry -> seconds < longFunctionEntry.getKey())
                .findFirst().orElseThrow(() -> new ApplicationException(ResponseMessage.INVALID_FORMAT_DATE));
        return entry.getValue().apply(instant);
    }

    private String formatInSeconds(Instant instant) {
        long seconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        return seconds + " seconds";
    }

    private String formatInMinutes(Instant instant) {
        long minutes = ChronoUnit.MINUTES.between(instant, Instant.now());
        return minutes + " minutes";
    }

    private String formatInHours(Instant instant) {
        long hours = ChronoUnit.HOURS.between(instant, Instant.now());
        return hours + " hours";
    }

    private String formatInDate(Instant instant) {
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ISO_DATE;
        return localDateTime.format(formatter);
    }
}
