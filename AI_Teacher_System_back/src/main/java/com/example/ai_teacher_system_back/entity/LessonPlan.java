package com.example.ai_teacher_system_back.entity;

import java.util.Date;

/**
 * 备课计划实体类
 */
public class LessonPlan {
    private Integer id;
    private String name;
    private Integer courseId;
    private String chapter;
    private String objectives;
    private String content;
    private String status;
    private Date planDate;
    private Integer creatorId;
    private Boolean isAiGenerated;
    private Date createTime;
    private Date updateTime;

    public LessonPlan() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Boolean getIsAiGenerated() {
        return isAiGenerated;
    }

    public void setIsAiGenerated(Boolean isAiGenerated) {
        this.isAiGenerated = isAiGenerated;
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
        return "LessonPlan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", courseId=" + courseId +
                ", chapter='" + chapter + '\'' +
                ", objectives='" + objectives + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", planDate=" + planDate +
                ", creatorId=" + creatorId +
                ", isAiGenerated=" + isAiGenerated +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}