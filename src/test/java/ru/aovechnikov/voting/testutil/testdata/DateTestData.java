package ru.aovechnikov.voting.testutil.testdata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Test data of date
 *
 * @author - A.Ovechnikov
 * @date - 12.01.2018
 */

public class DateTestData {

    public static final LocalDate DATE_1 = LocalDate.of(2017, 12, 30);

    public static final LocalDate DATE_2 = LocalDate.now();

    public static final LocalDateTime DATE_TIME1 = LocalDateTime.of(2017, 12, 30, 12, 0, 0);

    public static final LocalDateTime DATE_TIME2_BEFORE = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
    public static final LocalDateTime DATE_TIME2_AFTER = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0));
}
