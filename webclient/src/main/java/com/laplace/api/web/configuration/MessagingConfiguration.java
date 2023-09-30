package com.laplace.api.web.configuration;

import com.laplace.api.common.constants.enums.ChannelTopics;
import com.laplace.api.web.service.subscribers.PageEventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class MessagingConfiguration {

  private final RedisConnectionFactory redisConnectionFactory;
  private final PageEventSubscriber pageEventSubscriber;

  @Autowired
  public MessagingConfiguration(RedisConnectionFactory redisConnectionFactory,
      PageEventSubscriber pageEventSubscriber) {
    this.redisConnectionFactory = redisConnectionFactory;
    this.pageEventSubscriber = pageEventSubscriber;
  }

  @Bean
  MessageListenerAdapter pageListener() {
    return new MessageListenerAdapter(pageEventSubscriber);
  }

  @Bean
  RedisMessageListenerContainer redisMessageListenerContainer() {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(redisConnectionFactory);
    container.addMessageListener(pageListener(),
        new ChannelTopic(ChannelTopics.PAGE_PUBLISH_PRIVATE.getValue()));
    container
        .addMessageListener(pageListener(), new ChannelTopic(ChannelTopics.PAGE_UPDATE.getValue()));
    container.addMessageListener(pageListener(),
        new ChannelTopic(ChannelTopics.LOGO_FAVICON.getValue()));
    return container;
  }
}
