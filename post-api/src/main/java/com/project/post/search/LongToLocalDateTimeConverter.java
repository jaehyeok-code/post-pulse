package com.project.post.search;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {
  @Override
  public LocalDateTime convert(Long source) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(source), ZoneId.systemDefault());
  }
}
