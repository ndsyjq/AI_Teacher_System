-- AI教师智慧备课系统数据库设计

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS `ai_teacher_system`.`user`;
CREATE TABLE `ai_teacher_system`.`user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码（加密存储）',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `department` varchar(100) DEFAULT NULL COMMENT '所属部门/教研组',
  `position` varchar(50) DEFAULT NULL COMMENT '职位',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`),
  KEY `idx_email` (`email`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- 课程表
-- ----------------------------
DROP TABLE IF EXISTS `ai_teacher_system`.`course`;
CREATE TABLE `ai_teacher_system`.`course` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `name` varchar(100) NOT NULL COMMENT '课程名称',
  `subject` varchar(50) NOT NULL COMMENT '学科',
  `grade` varchar(50) NOT NULL COMMENT '年级',
  `semester` varchar(20) NOT NULL COMMENT '学期',
  `description` text COMMENT '课程描述',
  `status` varchar(20) NOT NULL DEFAULT '未开始' COMMENT '状态：未开始、进行中、已完成',
  `progress` int(3) NOT NULL DEFAULT '0' COMMENT '进度百分比',
  `students_count` int(11) NOT NULL DEFAULT '0' COMMENT '学生数量',
  `creator_id` int(11) NOT NULL COMMENT '创建者ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_creator` (`creator_id`),
  KEY `idx_subject_grade` (`subject`, `grade`),
  CONSTRAINT `fk_course_creator` FOREIGN KEY (`creator_id`) REFERENCES `ai_teacher_system`.`user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- ----------------------------
-- 备课计划表
-- ----------------------------
DROP TABLE IF EXISTS `ai_teacher_system`.`lesson_plan`;
CREATE TABLE `ai_teacher_system`.`lesson_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '备课ID',
  `name` varchar(100) NOT NULL COMMENT '备课名称',
  `course_id` int(11) NOT NULL COMMENT '关联课程ID',
  `chapter` varchar(100) NOT NULL COMMENT '章节',
  `objectives` text COMMENT '教学目标',
  `content` text COMMENT '教学内容',
  `status` varchar(20) NOT NULL DEFAULT '未开始' COMMENT '状态：未开始、进行中、已完成',
  `plan_date` date DEFAULT NULL COMMENT '计划日期',
  `creator_id` int(11) NOT NULL COMMENT '创建者ID',
  `is_ai_generated` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否AI生成：1-是，0-否',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_course` (`course_id`),
  KEY `idx_creator` (`creator_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_lesson_course` FOREIGN KEY (`course_id`) REFERENCES `ai_teacher_system`.`course` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_lesson_creator` FOREIGN KEY (`creator_id`) REFERENCES `ai_teacher_system`.`user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='备课计划表';

-- ----------------------------
-- 教学资源表
-- ----------------------------
DROP TABLE IF EXISTS `ai_teacher_system`.`resource`;
CREATE TABLE `ai_teacher_system`.`resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `name` varchar(255) NOT NULL COMMENT '资源名称',
  `category` varchar(50) NOT NULL COMMENT '资源分类：教案、课件、试题、教学视频、教学素材',
  `subject` varchar(50) NOT NULL COMMENT '学科',
  `grade` varchar(50) NOT NULL COMMENT '年级',
  `format` varchar(20) NOT NULL COMMENT '文件格式',
  `file_path` varchar(255) NOT NULL COMMENT '文件路径',
  `file_size` int(11) NOT NULL COMMENT '文件大小(KB)',
  `downloads` int(11) NOT NULL DEFAULT '0' COMMENT '下载次数',
  `uploader_id` int(11) NOT NULL COMMENT '上传者ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_subject_grade` (`subject`, `grade`),
  KEY `idx_uploader` (`uploader_id`),
  CONSTRAINT `fk_resource_uploader` FOREIGN KEY (`uploader_id`) REFERENCES `ai_teacher_system`.`user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教学资源表';

-- ----------------------------
-- 系统设置表
-- ----------------------------
DROP TABLE IF EXISTS `ai_teacher_system`.`system_setting`;
CREATE TABLE `ai_teacher_system`.`system_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '设置ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `theme` varchar(20) NOT NULL DEFAULT 'light' COMMENT '主题：light、dark、system',
  `font_size` varchar(20) NOT NULL DEFAULT 'medium' COMMENT '字体大小：small、medium、large',
  `language` varchar(20) NOT NULL DEFAULT 'zh-cn' COMMENT '语言：zh-cn、zh-tw、en',
  `notification_email` tinyint(1) NOT NULL DEFAULT '1' COMMENT '邮件通知：1-开启，0-关闭',
  `notification_browser` tinyint(1) NOT NULL DEFAULT '1' COMMENT '浏览器通知：1-开启，0-关闭',
  `notification_sound` tinyint(1) NOT NULL DEFAULT '1' COMMENT '声音提醒：1-开启，0-关闭',
  `auto_save` tinyint(1) NOT NULL DEFAULT '1' COMMENT '自动保存：1-开启，0-关闭',
  `save_interval` int(11) NOT NULL DEFAULT '5' COMMENT '保存间隔(分钟)',
  `data_backup_enabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '数据备份：1-开启，0-关闭',
  `data_backup_frequency` varchar(20) NOT NULL DEFAULT 'weekly' COMMENT '备份频率：daily、weekly、monthly',
  `data_backup_keep_count` int(11) NOT NULL DEFAULT '3' COMMENT '保留备份数量',
  `share_usage_data` tinyint(1) NOT NULL DEFAULT '1' COMMENT '分享使用数据：1-开启，0-关闭',
  `allow_cookies` tinyint(1) NOT NULL DEFAULT '1' COMMENT '允许Cookie：1-开启，0-关闭',
  `ai_assistant_enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'AI助手：1-开启，0-关闭',
  `ai_auto_suggest` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'AI自动建议：1-开启，0-关闭',
  `ai_voice_control` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'AI语音控制：1-开启，0-关闭',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_setting_user` FOREIGN KEY (`user_id`) REFERENCES `ai_teacher_system`.`user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统设置表';

-- ----------------------------
-- 数据备份记录表
-- ----------------------------
DROP TABLE IF EXISTS `ai_teacher_system`.`data_backup`;
CREATE TABLE `ai_teacher_system`.`data_backup` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '备份ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `backup_name` varchar(255) NOT NULL COMMENT '备份名称',
  `backup_path` varchar(255) NOT NULL COMMENT '备份文件路径',
  `backup_size` int(11) NOT NULL COMMENT '备份大小(KB)',
  `backup_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '备份时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_backup_user` FOREIGN KEY (`user_id`) REFERENCES `ai_teacher_system`.`user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据备份记录表';

-- ----------------------------
-- 用户活动记录表
-- ----------------------------
DROP TABLE IF EXISTS `ai_teacher_system`.`user_activity`;
CREATE TABLE `ai_teacher_system`.`user_activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `activity_type` varchar(50) NOT NULL COMMENT '活动类型：登录、备课、课程管理、资源上传等',
  `activity_content` varchar(255) NOT NULL COMMENT '活动内容',
  `activity_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '活动时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_activity_time` (`activity_time`),
  CONSTRAINT `fk_activity_user` FOREIGN KEY (`user_id`) REFERENCES `ai_teacher_system`.`user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户活动记录表';

-- ----------------------------
-- AI助手使用记录表
-- ----------------------------
DROP TABLE IF EXISTS `ai_teacher_system`.`ai_assistant_usage`;
CREATE TABLE `ai_teacher_system`.`ai_assistant_usage` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `query_content` text NOT NULL COMMENT '查询内容',
  `response_content` text NOT NULL COMMENT '响应内容',
  `usage_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '使用时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_usage_time` (`usage_time`),
  CONSTRAINT `fk_ai_usage_user` FOREIGN KEY (`user_id`) REFERENCES `ai_teacher_system`.`user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI助手使用记录表';

-- ----------------------------
-- 资源收藏表
-- ----------------------------
DROP TABLE IF EXISTS `ai_teacher_system`.`resource_favorite`;
CREATE TABLE `ai_teacher_system`.`resource_favorite` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `resource_id` int(11) NOT NULL COMMENT '资源ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_resource` (`user_id`, `resource_id`),
  KEY `idx_resource_id` (`resource_id`),
  CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `ai_teacher_system`.`user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_favorite_resource` FOREIGN KEY (`resource_id`) REFERENCES `ai_teacher_system`.`resource` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源收藏表';

-- ----------------------------
-- 资源评论表
-- ----------------------------
DROP TABLE IF EXISTS `ai_teacher_system`.`resource_comment`;
CREATE TABLE `ai_teacher_system`.`resource_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `resource_id` int(11) NOT NULL COMMENT '资源ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `content` text NOT NULL COMMENT '评论内容',
  `rating` int(1) DEFAULT NULL COMMENT '评分(1-5)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  PRIMARY KEY (`id`),
  KEY `idx_resource_id` (`resource_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_comment_resource` FOREIGN KEY (`resource_id`) REFERENCES `ai_teacher_system`.`resource` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `ai_teacher_system`.`user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源评论表';

SET FOREIGN_KEY_CHECKS = 1;