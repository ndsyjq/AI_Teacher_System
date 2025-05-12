package com.example.ai_teacher_system_back.entity;

import java.util.Date;

/**
 * 资源评论实体类
 */
public class ResourceComment {
    private Integer id;
    private Integer resourceId;
    private Integer userId;
    private String content;
    private Integer rating;
    private Date createTime;
    private Date updateTime;

    public ResourceComment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ResourceComment{" +
                "id=" + id +
                ", resourceId=" + resourceId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}