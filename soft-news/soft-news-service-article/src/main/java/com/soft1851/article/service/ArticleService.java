package com.soft1851.article.service;

import com.soft1851.pojo.Category;
import com.soft1851.pojo.bo.NewArticleBO;

/**
 * @author crq
 */
public interface ArticleService {

    /**
     * 发布文章
     * @param newArticleBO
     * @param category
     */
    void createArticle(NewArticleBO newArticleBO, Category category);

    /**
     * 更改文章状态
     * @param articleId
     * @param pendingStatus
     */
    void updateArticleStatus(String articleId,Integer pendingStatus);

    /**
     * 更新定时发布为即使发布
     */
    void updateAppointToPublish();
}
