package com.soft1851.enums;

/**
 * @author crq
 */

public enum ArticleReviewStatus {
    REVIEWING(1,"审核中（用户已提交）"),
    WAITING_MANUAL(2,"机审结束，等待人工审核"),
    SUCCESS(3,"审核通过（已发布）"),
    FAILED(4,"审核未通过"),
    WITHDRAW(5,"文章撤回");

    public final Integer type;
    public final String value;
    ArticleReviewStatus(Integer type,String value) {
        this.type = type;
        this.value = value;
    }

    public static boolean isArticleStatusValid(Integer tempStatus) {
        if(tempStatus != null) {
            if(tempStatus.equals(REVIEWING.type)
                    || tempStatus.equals(WAITING_MANUAL.type)
                    || tempStatus.equals(SUCCESS.type)
                    || tempStatus.equals(WITHDRAW.type)
                    || tempStatus.equals(FAILED.type)
            ) {
                return true;
            }

        }
        return false;
    }
}
