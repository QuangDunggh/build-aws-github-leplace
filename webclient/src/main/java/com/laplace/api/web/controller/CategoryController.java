package com.laplace.api.web.controller;

import com.laplace.api.common.util.BaseResponse;
import com.laplace.api.web.constants.APIEndPoints;
import com.laplace.api.web.service.WMCCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIEndPoints.CATEGORY)
public class CategoryController {

    private final WMCCategoryService wmcCategoryService;

    @Autowired
    public CategoryController(WMCCategoryService wmcCategoryService) {
        this.wmcCategoryService = wmcCategoryService;
    }

    @GetMapping()
    public BaseResponse getAllCategories() {
        return BaseResponse.create(wmcCategoryService.getAllCategories());
    }
}
