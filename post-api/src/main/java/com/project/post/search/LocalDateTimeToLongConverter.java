package com.project.post.search;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class LocalDateTimeToLongConverter implements Converter<LocalDateTime, Long> {
  @Override
  public Long convert(LocalDateTime source) {
    return source.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }
}