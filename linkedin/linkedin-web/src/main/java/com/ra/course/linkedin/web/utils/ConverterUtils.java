package com.ra.course.linkedin.web.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ConverterUtils {

    private static final String DD_MM_YYYY = "dd-MM-yyyy";

    public String formattedDate(final LocalDate localDate) {
        final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(DD_MM_YYYY);
        return localDate.format(formatter);
    }

    public LocalDate formattedDate(final String localDate) {
        final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(DD_MM_YYYY);
        return LocalDate.parse(localDate, formatter);
    }
}
