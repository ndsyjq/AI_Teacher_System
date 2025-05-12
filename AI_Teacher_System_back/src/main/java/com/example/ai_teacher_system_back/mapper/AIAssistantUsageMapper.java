package com.example.ai_teacher_system_back.mapper;

import com.example.ai_teacher_system_back.entity.AIAssistantUsage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI助手使用记录Mapper接口
 */
@Mapper
public interface AIAssistantUsageMapper {
    
    /**
     * 保存AI助手使用记录
     * @param aiAssistantUsage AI助手使用记录对象
     * @return 影响行数
     */
    int insert(AIAssistantUsage aiAssistantUsage);
    
    /**
     * 根据用户ID查询AI助手使用记录列表
     * @param userId 用户ID
     * @return AI助手使用记录列表
     */
    List<AIAssistantUsage> selectByUserId(@Param("userId") Integer userId);
    
    /**
     * 根据ID查询AI助手使用记录
     * @param id 记录ID
     * @return AI助手使用记录对象
     */
    AIAssistantUsage selectById(@Param("id") Integer id);
    
    /**
     * 查询用户最近的AI助手使用记录
     * @param userId 用户ID
     * @param limit 限制条数
     * @return AI助手使用记录列表
     */
    List<AIAssistantUsage> selectRecentByUserId(@Param("userId") Integer userId, @Param("limit") Integer limit);
    
    /**
     * 统计用户AI助手使用次数
     * @param userId 用户ID
     * @return 使用次数
     */
    int countByUserId(@Param("userId") Integer userId);
}