package com.taras.webpeopledb.formatters;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class LocalDateFormatter implements Formatter<LocalDate> {

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

  @Override
  public LocalDate parse(String text, Locale locale) throws ParseException {
    return LocalDate.parse(text, dateTimeFormatter);
  }

  @Override
  public String print(LocalDate object, Locale locale) {
    return dateTimeFormatter.format(object);
  }
}
