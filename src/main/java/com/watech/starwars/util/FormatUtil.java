package com.watech.starwars.util;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class FormatUtil {

  private FormatUtil() {}

  private static final DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  public static String formatOffsetDateDMY(String d) {
    if (d == null || d.isBlank()) return null;

    try {
      return OffsetDateTime.parse(d).format(DMY);
    } catch (DateTimeParseException e) {
      return d;
    }
  }

  public static Double parseDouble(String s) {
    if (s == null || s.isEmpty() || s.equalsIgnoreCase("unknown")) return Double.NaN;

    s.trim();

    try {
      return Double.parseDouble(s);
    } catch (NumberFormatException e) {
      return Double.NaN;
    }
  }
}
