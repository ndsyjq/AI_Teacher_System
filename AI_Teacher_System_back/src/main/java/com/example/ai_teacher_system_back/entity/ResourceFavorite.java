package com.example.ai_teacher_system_back.entity;

import java.util.Date;

/**
 * 资源收藏实体类
 */
public class ResourceFavorite {
    private Integer id;
    private Integer resourceId;
    private Integer userId;
    private Date createTime;

    public ResourceFavorite() {
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ResourceFavorite{" +
                "id=" + id +
                ", resourceId=" + resourceId +
                ", userId=" + userId +
                ", createTime=" + createTime +
                '}';
    }
}