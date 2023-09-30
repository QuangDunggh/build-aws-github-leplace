package com.laplace.api.common.repository.elasticsearch;

import com.laplace.api.common.model.elasticsearch.ItemDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDocumentRepository extends ElasticsearchRepository<ItemDocument, String> {

}
