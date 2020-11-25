package com.soft1851.article.controller;

import com.soft1851.api.BaseController;
import com.soft1851.api.controller.article.ArticleControllerApi;
import com.soft1851.article.service.ArticleService;
import com.soft1851.enums.ArticleCoverType;
import com.soft1851.enums.ArticleReviewStatus;
import com.soft1851.enums.YesOrNo;
import com.soft1851.pojo.Category;
import com.soft1851.pojo.bo.NewArticleBO;
import com.soft1851.result.GraceResult;
import com.soft1851.result.ResponseStatusEnum;
import com.soft1851.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author crq
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleController extends BaseController implements ArticleControllerApi {

    private final ArticleService articleService;


    @Override
    public GraceResult createArticle(@Valid NewArticleBO newArticleBO, BindingResult result) {
        System.out.println(newArticleBO);
        System.out.println(result);
        if(result.hasErrors()) {
            Map<String,String> errorMap = getErrors(result);
            return GraceResult.errorMap(errorMap);
        }
        if(newArticleBO.getArticleType().equals(ArticleCoverType.ONE_IMAGE.type)) {
            if(StringUtils.isBlank(newArticleBO.getArticleCover())) {
                return GraceResult.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
            }
        } else if(newArticleBO.getArticleType().equals(ArticleCoverType.WORDS.type)) {
            newArticleBO.setArticleCover("");
        }
        String allCatJson = redis.get(REDIS_ALL_CATEGORY);
        Category temp = null;
        if(StringUtils.isBlank(allCatJson)) {
            return GraceResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        } else  {
            List<Category> categories = JsonUtil.jsonToList(allCatJson,Category.class);
            assert categories != null;
            for(Category c: categories) {
                if(c.getId().equals(newArticleBO.getCategoryId())) {
                    temp = c;
                    break;
                }
            }
            if(temp == null) {
                return GraceResult.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
            }
        }
        System.out.println(newArticleBO.toString());
        articleService.createArticle(newArticleBO,temp);
        return GraceResult.ok();
    }

    @Override
    public GraceResult doReview(String articleId, Integer passOrNot) {
        Integer pendingStatus;
        if(passOrNot.equals(YesOrNo.YES.type)) {
            pendingStatus = ArticleReviewStatus.SUCCESS.type;
        }else if(YesOrNo.NO.type.equals(passOrNot)) {
            pendingStatus = ArticleReviewStatus.FAILED.type;
        }else {
            return GraceResult.errorCustom(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
        articleService.updateArticleStatus(articleId,pendingStatus);
        return GraceResult.ok();
    }

    @Override
    public GraceResult delete(String userId, String articleId) {
        articleService.deleteArticle(userId,articleId);
        return GraceResult.ok();
    }

    @Override
    public GraceResult withdraw(String userId, String articleId) {
        articleService.withdrawArticle(userId,articleId);
        return GraceResult.ok();
    }
}
