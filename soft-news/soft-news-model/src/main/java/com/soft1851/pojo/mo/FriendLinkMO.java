package com.soft1851.pojo.mo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author crq
 */
@Document("FriendLink")
public class FriendLinkMO {
    @Id
    private String id;

    @Field("link_name")
    private String linkName;

    @Field("link_url")
    private String linkUrl;

    @Field("is_delete")
    private Integer isDelete;

    @Field("create_time")
    private Date createTime;

    @Field("update_time")
    private Date updateTime;

    public String getId() {
        return id;
    }

    public FriendLinkMO setId(String id) {
        this.id = id;
        return this;
    }

    public String getLinkName() {
        return linkName;
    }

    public FriendLinkMO setLinkName(String linkName) {
        this.linkName = linkName;
        return this;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public FriendLinkMO setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
        return this;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public FriendLinkMO setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public FriendLinkMO setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public FriendLinkMO setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
