package com.laplace.api.common.configuration.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

@Configuration
@EnableCaching(proxyTargetClass = true)
public class RedisConfig {

  @Value("${spring.redis.host}")
  private String host;
  @Value("${spring.redis.port}")
  private Integer port;

  @Bean
  LettuceConnectionFactory lettuceConnectionFactory() {
    return new LettuceConnectionFactory(host, port);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    final RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(lettuceConnectionFactory());
    template.setKeySerializer(new GenericToStringSerializer<>(Object.class));
    template.setValueSerializer(new JdkSerializationRedisSerializer());
    return template;
  }

  @Bean
  public RedisTemplate<String, Object> valueStringTemplate() {
    final RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(lettuceConnectionFactory());
    template.setKeySerializer(new GenericToStringSerializer<>(Object.class));
    template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
    template.setHashKeySerializer(template.getKeySerializer());
    template.setHashValueSerializer(template.getValueSerializer());
    return template;
  }

  @Bean(name = "valueJsonTemplate")
  public RedisTemplate<String, Object> valueJsonRedisTemplate() {
    final RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(lettuceConnectionFactory());
    template.setKeySerializer(new GenericToStringSerializer<>(Object.class));
    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    template.setHashKeySerializer(template.getKeySerializer());
    template.setHashValueSerializer(template.getValueSerializer());
    return template;
  }

  public <T> RedisTemplate<String, T> stringTemplateOfType(Class<T> type) {
    final RedisTemplate<String, T> template = new RedisTemplate<>();
    template.setConnectionFactory(lettuceConnectionFactory());
    template.setKeySerializer(new GenericToStringSerializer<>(String.class));
    template.setValueSerializer(new GenericToStringSerializer<>(type));
    template.setHashKeySerializer(template.getKeySerializer());
    template.setHashValueSerializer(template.getValueSerializer());
    template.afterPropertiesSet();
    return template;
  }
}
