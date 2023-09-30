package com.laplace.api.web.service.impl;

import com.laplace.api.common.constants.enums.Category;
import com.laplace.api.common.constants.enums.LanguagesForRedis;
import com.laplace.api.common.constants.enums.TargetAudience;
import com.laplace.api.common.service.cache.CategoryCacheService;
import com.laplace.api.web.service.WMCCategoryService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class WMCCategoryServiceImpl implements WMCCategoryService {

    private final CategoryCacheService categoryCacheService;

    public WMCCategoryServiceImpl(
            CategoryCacheService categoryCacheService) {
        this.categoryCacheService = categoryCacheService;
    }

    @Override
    public Map<String, List<String>> getAllCategories() {
        Map<String, List<String>> categories = new LinkedHashMap<>();

        TargetAudience.stream().forEach(targetAudience -> {
            LanguagesForRedis.stream().forEach(languagesForRedis -> {
                Category.stream().forEach(category -> categories.put(
                        targetAudience.toString().toLowerCase() + ":" + languagesForRedis.getValue() + ":"
                                + category.toString().toLowerCase(),
                        categoryCacheService.getSubCategories(targetAudience.toString(), languagesForRedis.getValue(),
                                category.toString())));
            });
        });
        return categories;
    }
}
