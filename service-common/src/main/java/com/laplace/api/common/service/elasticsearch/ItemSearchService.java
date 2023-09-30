package com.laplace.api.common.service.elasticsearch;

import com.laplace.api.common.dto.ItemSearchRequest;
import com.laplace.api.common.helper.ItemSearchQueryBuilderHelper;
import com.laplace.api.common.helper.ItemSortExtractor;
import com.laplace.api.common.model.elasticsearch.ItemDocument;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ItemSearchService {

  private final ElasticsearchOperations operations;
  private final ItemSearchQueryBuilderHelper itemSearchQueryBuilderHelper;

  @Autowired
  public ItemSearchService(
      ElasticsearchOperations operations,
      ItemSearchQueryBuilderHelper itemSearchQueryBuilderHelper) {
    this.operations = operations;
    this.itemSearchQueryBuilderHelper = itemSearchQueryBuilderHelper;
  }

  public Page<ItemDocument> search(ItemSearchRequest itemSearchRequest) {
    Pageable page = ItemSortExtractor.prepareItemSearchPage(itemSearchRequest.getPage(),
        itemSearchRequest.getSize(), itemSearchRequest.getSort());
    NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
        .withQuery(itemSearchQueryBuilderHelper.queryBuilder(itemSearchRequest))
        .withPageable(page).build();

    SearchHits<ItemDocument> searchHits = operations.search(searchQuery, ItemDocument.class);

    List<ItemDocument> docs = searchHits.stream().map(SearchHit::getContent)
        .collect(Collectors.toList());
    return new PageImpl<>(docs, page, searchHits.getTotalHits());
  }
}
