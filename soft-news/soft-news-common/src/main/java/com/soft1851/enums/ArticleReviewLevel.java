package com.soft1851.enums;

/**
 * @author crq
 */

public enum ArticleReviewLevel {
    /**
     * 阿里云图片，文字审核
     */
    PASS("pass","自动审核通过"),
    BLOCK("block","自动审核不通过"),
    REVIEW("review","建议人工复审");

    public final String type;
    public final String value;

    ArticleReviewLevel(String type, String value) {
        this.type = type;
        this.value = value;
    }
}

