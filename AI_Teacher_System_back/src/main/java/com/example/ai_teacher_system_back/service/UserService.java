package com.example.ai_teacher_system_back.service;

import com.example.ai_teacher_system_back.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户服务接口
 * 定义用户相关的业务方法
 */
public interface UserService extends UserDetailsService {

    /**
     * 创建用户
     * @param user 用户信息
     * @return 创建后的用户
     */
    User createUser(User user);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新后的用户
     */
    User updateUser(User user);

    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户信息
     */
    User getUserById(Integer id);

    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean changePassword(Integer userId, String oldPassword, String newPassword);

    /**
     * 更新用户头像
     * @param userId 用户ID
     * @param avatarUrl 头像URL
     * @return 更新后的用户
     */
    User updateAvatar(Integer userId, String avatarUrl);
}