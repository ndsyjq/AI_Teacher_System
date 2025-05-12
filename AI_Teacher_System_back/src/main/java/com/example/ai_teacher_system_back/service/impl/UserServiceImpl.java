package com.example.ai_teacher_system_back.service.impl;

import com.example.ai_teacher_system_back.entity.User;
import com.example.ai_teacher_system_back.exception.BusinessException;
import com.example.ai_teacher_system_back.repository.UserRepository;
import com.example.ai_teacher_system_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户名不存在"));
    }

    @Override
    public User createUser(User user) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        
        // 设置用户状态和时间
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(true);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        // 保存用户
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        // 检查用户是否存在
        if (!userRepository.existsById(user.getId())) {
            throw new BusinessException("用户不存在");
        }
        
        // 更新用户信息
        user.setUpdateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean changePassword(Integer userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("旧密码不正确");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        userRepository.save(user);
        
        return true;
    }
    
    @Override
    public User updateAvatar(Integer userId, String avatarUrl) {
        User user = getUserById(userId);
        
        // 更新头像
        user.setAvatar(avatarUrl);
        user.setUpdateTime(LocalDateTime.now());
        return userRepository.save(user);
    }
}