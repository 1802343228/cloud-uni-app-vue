package com.soft1851.article.controller;

import com.soft1851.api.BaseController;
import com.soft1851.api.controller.article.ArticleControllerApi;
import com.soft1851.article.service.ArticleService;
import com.soft1851.enums.ArticleCoverType;
import com.soft1851.enums.ArticleReviewStatus;
import com.soft1851.enums.YesOrNo;
import com.soft1851.pojo.Category;
import com.soft1851.pojo.bo.NewArticleBO;
import com.soft1851.pojo.vo.AppUserVO;
import com.soft1851.pojo.vo.ArticleDetailVO;
import com.soft1851.result.GraceResult;
import com.soft1851.result.ResponseStatusEnum;
import com.soft1851.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author crq
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleController extends BaseController implements ArticleControllerApi {

    private final ArticleService articleService;
    private final RestTemplate restTemplate;


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

    @Override
    public GraceResult detail(String articleId) {
        ArticleDetailVO detailVO = articleService.queryDetail(articleId);
        Set<String> idSet = new HashSet<>();
        idSet.add(detailVO.getPublishUserId());
        List<AppUserVO> publisherList = getPublisherList(idSet);
        if(!publisherList.isEmpty()) {
            detailVO.setPublishUserName(publisherList.get(0).getNickname());
        }
        detailVO.setReadCounts(getCountsFromRedis(REDIS_ARTICLE_READ_COUNTS + ":" +articleId));
        return GraceResult.ok(detailVO);
    }

    /**
     * 发起远程调用，获得用户的基本信息
     *
     * @param idSet id集合
     * @return List<AppUserVO>
     */
    private List<AppUserVO> getPublisherList(Set<String> idSet) {
        String userServerUrlExecute = "http://crq.com:8003/user/queryByIds?userIds=" + JsonUtil.objectToJson(idSet);
        ResponseEntity<GraceResult> responseEntity
                = restTemplate.getForEntity(userServerUrlExecute, GraceResult.class);
        GraceResult bodyResult = responseEntity.getBody();
        List<AppUserVO> publisherList = null;
        assert bodyResult != null;
        if (bodyResult.getStatus() == 200) {
            String userJson = JsonUtil.objectToJson(bodyResult.getData());
            publisherList = JsonUtil.jsonToList(userJson, AppUserVO.class);
        }
        return publisherList;
    }

    /**
     * 从redis根据key读取阅读量
     *
     * @param key key
     * @return value
     */
    private Integer getCountsFromRedis(String key) {
        String countsStr = redis.get(key);
        if (StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }
        return Integer.valueOf(countsStr);
    }

}
