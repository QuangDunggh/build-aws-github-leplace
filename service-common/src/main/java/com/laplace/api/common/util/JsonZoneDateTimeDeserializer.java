package com.laplace.api.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonZoneDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

  @Override
  public ZonedDateTime deserialize(JsonParser paramJsonParser,
      DeserializationContext paramDeserializationContext)
      throws IOException {
    String str = paramJsonParser.getText().trim();
    return Instant.ofEpochMilli(Long.parseLong(str)).atZone(ZoneOffset.UTC);
  }
}
