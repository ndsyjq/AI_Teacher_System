package com.example.ai_teacher_system_back.entity;

import java.util.Date;

/**
 * 数据备份实体类
 */
public class DataBackup {
    private Integer id;
    private String backupName;
    private String backupPath;
    private Long backupSize;
    private String backupType;
    private String backupStatus;
    private Integer creatorId;
    private Date createTime;

    public DataBackup() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBackupName() {
        return backupName;
    }

    public void setBackupName(String backupName) {
        this.backupName = backupName;
    }

    public String getBackupPath() {
        return backupPath;
    }

    public void setBackupPath(String backupPath) {
        this.backupPath = backupPath;
    }

    public Long getBackupSize() {
        return backupSize;
    }

    public void setBackupSize(Long backupSize) {
        this.backupSize = backupSize;
    }

    public String getBackupType() {
        return backupType;
    }

    public void setBackupType(String backupType) {
        this.backupType = backupType;
    }

    public String getBackupStatus() {
        return backupStatus;
    }

    public void setBackupStatus(String backupStatus) {
        this.backupStatus = backupStatus;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "DataBackup{" +
                "id=" + id +
                ", backupName='" + backupName + '\'' +
                ", backupPath='" + backupPath + '\'' +
                ", backupSize=" + backupSize +
                ", backupType='" + backupType + '\'' +
                ", backupStatus='" + backupStatus + '\'' +
                ", creatorId=" + creatorId +
                ", createTime=" + createTime +
                '}';
    }
}