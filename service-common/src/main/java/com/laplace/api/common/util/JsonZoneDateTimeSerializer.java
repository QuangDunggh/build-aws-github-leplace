package com.laplace.api.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonZoneDateTimeSerializer extends JsonSerializer<ZonedDateTime> {

  @Override
  public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    gen.writeNumber(value.toInstant().toEpochMilli());
  }
}
