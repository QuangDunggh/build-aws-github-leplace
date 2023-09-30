package com.laplace.api.common.configuration.elasticsearch;

import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
public class ElasticSearchConfig {

  @Value("${elasticsearch.url}")
  private String elasticsearchHost;

  @Value("${elasticsearch.port:80}")
  private Integer elasticSearchPort;

  @Value("${elasticsearch.protocol:http}")
  private String elasticSearchProtocol;

  @Value("${elasticsearch.host.verify:false}")
  private Boolean hostVerify;

  @Bean(destroyMethod = "close")
  public RestHighLevelClient client() {
    RestClientBuilder builder = RestClient
        .builder(new HttpHost(elasticsearchHost, elasticSearchPort,
            elasticSearchProtocol));
    if (hostVerify) {
      builder.setHttpClientConfigCallback(clientBuilder ->
          clientBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier()));
    }
    return new RestHighLevelClient(builder);
  }

  @Bean
  public ElasticsearchRestTemplate elasticsearchTemplate() {
    return new ElasticsearchRestTemplate(client());
  }
}
