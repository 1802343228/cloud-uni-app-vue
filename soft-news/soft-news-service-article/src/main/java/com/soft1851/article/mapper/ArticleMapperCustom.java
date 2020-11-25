package com.soft1851.article.mapper;

import org.springframework.stereotype.Repository;

/**
 * @author crq
 */

@Repository
public interface ArticleMapperCustom {
    /**
     * 更新文章发布状态：定时 ->即使发布
     */
    void updateAppointToPublish();
}
