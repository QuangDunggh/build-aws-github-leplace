package com.laplace.api.common.service.cache;

import com.laplace.api.common.constants.ApplicationConstants;
import com.laplace.api.common.constants.ApplicationConstants.StringUtils;
import com.laplace.api.common.constants.enums.Category;
import com.laplace.api.common.constants.enums.TargetAudience;
import com.laplace.api.common.repository.redis.CategoryCacheRepository;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryCacheService {

  private final CategoryCacheRepository categoryCacheRepository;

  @Autowired
  public CategoryCacheService(
      CategoryCacheRepository categoryCacheRepository) {
    this.categoryCacheRepository = categoryCacheRepository;
  }

  public List<String> getSubCategories(String target, String lang, String category) {
    return categoryCacheRepository.getSubCategories(buildKey(target, lang, category),
        ApplicationConstants.VALUE_ZERO, ApplicationConstants.NEGATIVE_ONE);
  }

  public Optional<Pair<String, String>> getSubCategoryByIndex(TargetAudience targetAudience,
      Category category, Integer subCategoryIndex) {
    List<String> enList = getSubCategories(targetAudience.name(), ApplicationConstants.ENGLISH_LANG,
        category.name());
    List<String> jpList = getSubCategories(targetAudience.name(), ApplicationConstants.JP_LANG,
        category.name());

    if (enList.size() > subCategoryIndex && jpList.size() > subCategoryIndex) {
      return Optional.of(Pair.of(enList.get(subCategoryIndex), jpList.get(subCategoryIndex)));
    } else {
      return Optional.empty();
    }
  }

  private String buildKey(String target, String lang, String category) {
    return (ApplicationConstants.CATEGORY_PREFIX + target + StringUtils.COLON + lang
        + StringUtils.COLON + category).toLowerCase();
  }
}
