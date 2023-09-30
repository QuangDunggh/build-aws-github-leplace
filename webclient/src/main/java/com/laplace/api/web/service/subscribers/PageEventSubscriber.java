package com.laplace.api.web.service.subscribers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laplace.api.common.constants.enums.ChannelTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;

@Service
@Slf4j
public class PageEventSubscriber implements MessageListener {

  private final ObjectMapper objectMapper;

  @Autowired
  public PageEventSubscriber(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * Callback for processing received events of page
   *
   * @param message message must not be {@literal null}.
   * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
   */

  @Override
  public void onMessage(@Nonnull Message message, byte[] pattern) {
    try {
      String channel = new String(message.getChannel());
      ChannelTopics channelTopics = ChannelTopics.forValue(channel);
      switch (channelTopics) {
        case PAGE_PUBLISH_PRIVATE:
        case PAGE_UPDATE:
          break;
        case LOGO_FAVICON:
          TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
          };
          HashMap<String, Object> logoFavIcon = objectMapper.readValue(message.toString(), typeRef);
          log.debug("topic:{}, logo favicon: {}", channelTopics.toString(), logoFavIcon);
          break;
        default:
      }

    } catch (IOException e) {
      log.error(e.getLocalizedMessage());
    }
  }
}
