package com.soft1851.article.task;

import com.soft1851.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * @author crq
 */

@Configuration
@EnableScheduling
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskPublishArticle {
    private final ArticleService articleService;

    @Scheduled(cron = "0/10 * * * * ?")
    private void publishArticles() {
        System.out.println("执行定时任务:"+ LocalDateTime.now());
        articleService.updateAppointToPublish();
    }
}
