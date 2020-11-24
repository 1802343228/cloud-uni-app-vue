package com.soft1851.pojo;

import org.springframework.data.annotation.Id;

import javax.persistence.Column;

/**
 * @author crq
 */
public class Category {
    @Id
    private Integer id;

    public Integer getId() {
        return id;
    }

    public Category setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public String getTagColor() {
        return tagColor;
    }

    public Category setTagColor(String tagColor) {
        this.tagColor = tagColor;
        return this;
    }

    /**
     * 分类名
     */
    private String name;

    /**
     * 标签颜色
     */
    @Column(name = "tag_color")
    private String tagColor;



}
