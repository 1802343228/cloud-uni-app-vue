package com.soft1851.article.service.impl;

import com.soft1851.article.mapper.ArticleMapper;
import com.soft1851.article.mapper.ArticleMapperCustom;
import com.soft1851.article.service.ArticleService;
import com.soft1851.enums.ArticleAppointType;
import com.soft1851.enums.ArticleReviewLevel;
import com.soft1851.enums.ArticleReviewStatus;
import com.soft1851.enums.YesOrNo;
import com.soft1851.exception.GraceException;
import com.soft1851.pojo.Article;
import com.soft1851.pojo.Category;
import com.soft1851.pojo.bo.NewArticleBO;
import com.soft1851.result.ResponseStatusEnum;
import com.soft1851.utils.extend.AliTextReviewUtil;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author crq
 */
@Service
@Slf4j
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Resource
    private  Sid sid;
    @Autowired
    private  ArticleMapperCustom articleMapperCustom;
    @Autowired
    private  AliTextReviewUtil aliTextReviewUtil;

    @Override
    public void createArticle(NewArticleBO newArticleBO, Category category) {
        String articleId = sid.nextShort();
        Article article = new Article();

        BeanUtils.copyProperties(newArticleBO,article);
        article.setId(articleId);
        article.setCategoryId(category.getId());
        article.setArticleStatus(ArticleReviewStatus.REVIEWING.type);
        article.setCommentCounts(0);
        article.setReadCounts(0);
        article.setIsDelete(YesOrNo.NO.type);
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        if(article.getIsAppoint().equals(ArticleAppointType.TIMING.type)) {
            article.setPublishTime(newArticleBO.getPublishTime());
        } else if(article.getIsAppoint().equals(ArticleAppointType.IMMEDIATELY.type)) {
            article.setPublishTime(new Date());
        }
        int res = articleMapper.insert(article);
        if(res != 1) {
            GraceException.display(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }

        System.out.println("测试测试测测试");
        System.out.println(newArticleBO.getTitle()
                + newArticleBO.getContent());
        //通过阿里只能AI实现对文章文本的自动检测
        String reviewResult = aliTextReviewUtil.reviewTextContent(newArticleBO.getTitle()
        + newArticleBO.getContent()
        );
        log.info("审核结果"+reviewResult);
        if(ArticleReviewLevel.PASS.type.equalsIgnoreCase(reviewResult)) {
            log.info("审核通过");
            this.updateArticleStatus(articleId,ArticleReviewStatus.SUCCESS.type);
        }else if(ArticleReviewLevel.REVIEW.type.equalsIgnoreCase(reviewResult)) {
            log.info("需要人工复审");
            this.updateArticleStatus(articleId,ArticleReviewStatus.WAITING_MANUAL.type);
        }else if(ArticleReviewLevel.BLOCK.type.equalsIgnoreCase(reviewResult)) {
            log.info("审核不通过");
            this.updateArticleStatus(articleId,ArticleReviewStatus.FAILED.type);
        }

    }

    @Override
    public void updateArticleStatus(String articleId, Integer pendingStatus) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",articleId);
        Article pendingArticle = new Article();
        int res = articleMapper.updateByExampleSelective(pendingArticle,example);
        if(res != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void updateAppointToPublish() {
        articleMapperCustom.updateAppointToPublish();
    }
}