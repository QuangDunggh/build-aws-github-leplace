package com.laplace.api.common.constants.enums;

import com.laplace.api.common.constants.Messages;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum ChannelTopics {
  PAGE_PUBLISH_PRIVATE("page:status"),
  PAGE_UPDATE("page:update"),
  LOGO_FAVICON("resources:logo-favicon");

  private String value;

  ChannelTopics(String value) {
    this.value = value;
  }

  private static Supplier<Stream<ChannelTopics>> topicSupplier() {
    return () -> Stream.of(ChannelTopics.values());
  }

  public static ChannelTopics forValue(String value) {
    return topicSupplier().get()
        .filter(topic -> topic.getValue().equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(String.format(Messages.INVALID_ENUM_ARGS,
            value, ChannelTopics.class.getName())));
  }

  public String getValue() {
    return value;
  }
}
