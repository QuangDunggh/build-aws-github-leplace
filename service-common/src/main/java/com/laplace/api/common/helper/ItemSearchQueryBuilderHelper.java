package com.laplace.api.common.helper;

import static com.laplace.api.common.constants.ApplicationConstants.StringUtils.DOT;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.SearchConstants.FilterFields;
import com.laplace.api.common.constants.enums.JudgementStatus;
import com.laplace.api.common.dto.ItemSearchRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class ItemSearchQueryBuilderHelper {

  /**
   * this will build query for item search query in elastic search
   *
   * @param searchRequest search request
   * @return query builder
   * @see ItemSearchRequest
   * @see org.elasticsearch.index.query.QueryBuilder
   */
  public QueryBuilder queryBuilder(ItemSearchRequest searchRequest) {
    return getQueryBuilder(searchRequest);
  }

  private QueryBuilder getQueryBuilder(ItemSearchRequest searchRequest) {
    BoolQueryBuilder boolQueryBuilder = boolQuery();

    if (!ObjectUtils.isEmpty(searchRequest.getSellerId())) {
      boolQueryBuilder.must(termQuery(FilterFields.SELLER_ID, searchRequest.getSellerId()));
    }

    String keywords = searchRequest.getKeyword();
    if (!StringUtils.isEmpty(keywords)) {
      boolQueryBuilder.must(keywordQueryBuilder(keywords));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getStatus())) {
      boolQueryBuilder.must(inMatchQueryBuilder(searchRequest.getStatus(), FilterFields.STATUS));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getNotInStatuses())) {
      boolQueryBuilder.mustNot(termsQuery(FilterFields.STATUS, searchRequest.getNotInStatuses()));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getNotInItemId())) {
      boolQueryBuilder.mustNot(termsQuery(FilterFields.ID, searchRequest.getNotInItemId()));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getTarget())) {
      boolQueryBuilder
          .must(termQuery(FilterFields.TARGET_AUDIENCE, searchRequest.getTarget().name()));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getCategory())) {
      boolQueryBuilder.must(termQuery(FilterFields.CATEGORY, searchRequest.getCategory().name()));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getSubCategories())) {
      boolQueryBuilder
          .must(inQueryBuilder(searchRequest.getSubCategories(), FilterFields.SUB_CATEGORY
              + DOT + searchRequest.getLang()));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getBrands())) {
      boolQueryBuilder
          .must(inQueryBuilder(searchRequest.getBrands(), FilterFields.BRAND_ID));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getPhysicalConditions())) {
      boolQueryBuilder
          .must(inQueryBuilder(searchRequest.getPhysicalConditions(),
              FilterFields.PHYSICAL_CONDITION));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getJudgementStatus())) {
      boolQueryBuilder
          .must(
              matchQuery(FilterFields.JUDGEMENT_STATUS, searchRequest.getJudgementStatus().name()));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getItemSize())) {
      boolQueryBuilder.must(matchQuery(FilterFields.SIZE, searchRequest.getItemSize()));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getHideInvalidItems()) &&
        BooleanUtils.isFalse(searchRequest.getHideInvalidItems())) {
      boolQueryBuilder
          .must(
              matchQuery(FilterFields.JUDGEMENT_STATUS, JudgementStatus.FAKE.name()));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getHidden())) {
      boolQueryBuilder.must(termQuery(FilterFields.HIDDEN, searchRequest.getHidden()));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getIsNotBlacklisted())) {
      boolQueryBuilder
          .mustNot(termQuery(FilterFields.BLACKLISTED, searchRequest.getIsNotBlacklisted()));
    }

    if (BooleanUtils.isTrue(searchRequest.getPriceCut())) {
      boolQueryBuilder.must(rangeQueryBuilder(FilterFields.DISCOUNT_PERCENTAGE,
          ApplicationConstants.ONE_LONG));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getPickUp())) {
      boolQueryBuilder.must(termQuery(FilterFields.PICK_UP, searchRequest.getPickUp()));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getStartPrice()) && !ObjectUtils
        .isEmpty(searchRequest.getEndPrice())) {
      boolQueryBuilder.must(rangeQueryBuilder(FilterFields.DISPLAY_PRICE,
          searchRequest.getStartPrice(), searchRequest.getEndPrice()));
    }

    if (!ObjectUtils.isEmpty(searchRequest.getStartTime()) && !ObjectUtils
        .isEmpty(searchRequest.getEndTime())) {
      boolQueryBuilder.must(rangeQueryBuilder(FilterFields.DISPLAY_REQUEST_DATE,
          searchRequest.getStartTime(), searchRequest.getEndTime()));
    }

    return boolQueryBuilder.must().isEmpty() ? matchAllQuery() : boolQueryBuilder;
  }

  private QueryBuilder inQueryBuilder(Set<?> items, String fieldName) {
    BoolQueryBuilder boolQueryBuilder = boolQuery();
    if (!ObjectUtils.isEmpty(items)) {
      items.forEach(item -> boolQueryBuilder.should(termQuery(fieldName, item)));
    }
    return boolQueryBuilder;
  }

  private QueryBuilder inMatchQueryBuilder(Set<?> items, String fieldName) {
    BoolQueryBuilder boolQueryBuilder = boolQuery();
    if (!ObjectUtils.isEmpty(items)) {
      items.forEach(item -> boolQueryBuilder.should(matchQuery(fieldName, item)));
    }
    return boolQueryBuilder;
  }

  private QueryBuilder keywordQueryBuilder(String keywords) {
    BoolQueryBuilder builder = new BoolQueryBuilder();
    if (StringUtils.isNotEmpty(keywords)) {
      String keywordWithPartialSearch;
      if (keywords.contains(" ")) {
        List<String> aTokenList = Arrays.asList(keywords.split("\\s+"));
        keywordWithPartialSearch = aTokenList.stream().map(p -> "*" + p + "*").collect(
            Collectors.joining(" "));
      } else {
        keywordWithPartialSearch = "*" + keywords + "*";
      }
      builder.should(QueryBuilders.queryStringQuery(keywordWithPartialSearch)
          .defaultOperator(Operator.AND)
          .defaultField(FilterFields.KEYWORDS))
          .should(multiMatchQuery(keywords,
              FilterFields.CATEGORY, FilterFields.SUB_CATEGORY_EN, FilterFields.SUB_CATEGORY_JP,
              FilterFields.ITEM_NAME, FilterFields.BRAND_NAME, FilterFields.KEYWORDS)
              .operator(Operator.AND)
          )
          .should(termQuery(FilterFields.SELLER_EMAIL_KEYWORD, keywords))
          .should(termQuery(FilterFields.ID, keywords));
    }

    return builder;
  }

  private QueryBuilder rangeQueryBuilder(String field, Long start, Long end) {
    return rangeQuery(field).from(start).to(end);
  }

  private QueryBuilder rangeQueryBuilder(String field, Long start) {
    return rangeQuery(field).from(start);
  }
}
