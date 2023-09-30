package com.laplace.api.common.util;

import static com.laplace.api.common.constants.ApplicationConstants.LAST_HOUR;
import static com.laplace.api.common.constants.ApplicationConstants.LAST_MIN;
import static com.laplace.api.common.constants.ApplicationConstants.LAST_NANO_SEC;
import static com.laplace.api.common.constants.ApplicationConstants.LAST_SEC;
import static com.laplace.api.common.constants.ApplicationConstants.ONE;
import static com.laplace.api.common.constants.ApplicationConstants.VALUE_ZERO;

import com.datastax.driver.core.utils.UUIDs;
import com.laplace.api.common.constants.StatusConstants;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class DateUtil {

  private static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;

  public static final DateTimeFormatter DATETIME_MILLI_FORMATTER = DateTimeFormatter
      .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
  public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm");
  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
      .ofPattern("yyyy-MM-dd");

  public static ZonedDateTime timeNow() {
    return ZonedDateTime.now(ZONE_OFFSET);
  }

  public static ZonedDateTime fromEpochMilli(Long milli) {
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(milli), ZONE_OFFSET);
  }

  public static Long timeNowToEpochMilli() {
    return toEpochMilli(timeNow());
  }

  public static Long toEpochMilli(ZonedDateTime zonedDateTime) {
    return zonedDateTime == null ? null : zonedDateTime.toInstant().toEpochMilli();
  }

  public static Long toEpochMilli(Date dateToConvert) {
    return dateToConvert == null ? null : toEpochMilli(getZoneDateTime(dateToConvert));
  }

  public static ZonedDateTime getZoneDateTime(Date dateToConvert) {
    return ZonedDateTime.ofInstant(dateToConvert.toInstant(), ZONE_OFFSET);
  }

  public static Date getDate(ZonedDateTime dateToConvert) {
    return Date.from(dateToConvert.toInstant());
  }

  public static String getFormattedDate(Long milli) {
    return getFormattedDatetimeMilli(fromEpochMilli(milli));
  }

  public static String getFormattedDate(Long milli, String format) {
    return DateTimeFormatter.ofPattern(format).format(fromEpochMilli(milli));
  }

  public static String getFormattedDatetimeMilli(ZonedDateTime dateTime) {
    return DATETIME_MILLI_FORMATTER.format(dateTime);
  }

  public static String getFormattedDate(ZonedDateTime dateTime, String format) {
    return DateTimeFormatter.ofPattern(format).format(dateTime);
  }

  public static String getUniqueTimeBasedUUID() {
    return UUIDs.timeBased().toString();
  }

  public static ZonedDateTime getFirstDayOfMonth(int year, int month) {
    return ZonedDateTime
        .of(year, month, ONE, VALUE_ZERO, VALUE_ZERO, VALUE_ZERO, VALUE_ZERO, ZONE_OFFSET);
  }

  public static ZonedDateTime getLastDayOfMonth(int year, int month) {
    return ZonedDateTime
        .of(year, month, ONE, LAST_HOUR, LAST_MIN, LAST_SEC, LAST_NANO_SEC, ZONE_OFFSET)
        .with(TemporalAdjusters.lastDayOfMonth());
  }

  public static String toJapaneseDatetimeAnnotation(LocalDateTime dateTime) {
    return dateTime.getYear() + "年" + dateTime.getMonthValue() + "月" + dateTime.getDayOfMonth()
        + "日（" + toJapaneseDay(dateTime.getDayOfWeek()) + "） " + dateTime.getHour() + ":" + dateTime
        .getMinute();
  }

  public static String toJapaneseDateAnnotation(LocalDateTime dateTime) {
    return dateTime.getYear() + "年" + dateTime.getMonthValue() + "月" + dateTime.getDayOfMonth()
        + "日（" + toJapaneseDay(dateTime.getDayOfWeek()) + "）";
  }

  public static String toJapaneseDay(DayOfWeek dayOfWeek) {
    switch (dayOfWeek) {
      case SUNDAY:
        return "日";
      case MONDAY:
        return "月";
      case TUESDAY:
        return "火";
      case WEDNESDAY:
        return "水";
      case THURSDAY:
        return "木";
      case FRIDAY:
        return "金";
      case SATURDAY:
        return "土";
      default:
        return "Can't Determined";
    }
  }

  public static Long addMonths(ZonedDateTime displayRequestDate, Integer months) {
    return toEpochMilli(displayRequestDate == null ? null : displayRequestDate.plusMonths(months));
  }
}
