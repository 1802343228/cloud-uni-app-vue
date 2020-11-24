package com.soft1851.article.controller;

import com.soft1851.api.BaseController;
import com.soft1851.api.controller.category.CategoryControllerApi;
import com.soft1851.article.service.CategoryService;
import com.soft1851.pojo.Category;
import com.soft1851.result.GraceResult;
import com.soft1851.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author crq
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryController extends BaseController implements CategoryControllerApi {
    private final CategoryService categoryService;

    @Override
    public GraceResult getAll() {
        String allCategoryJson = redis.get(REDIS_ALL_CATEGORY);
        List<Category> categoryList;
        if(StringUtils.isBlank(allCategoryJson)) {
            categoryList = categoryService.selectAll();
            redis.set(REDIS_ALL_CATEGORY, JsonUtil.objectToJson(categoryList));
        } else {
            categoryList = JsonUtil.jsonToList(allCategoryJson,Category.class);
        }
        return GraceResult.ok(categoryList);
    }
}
