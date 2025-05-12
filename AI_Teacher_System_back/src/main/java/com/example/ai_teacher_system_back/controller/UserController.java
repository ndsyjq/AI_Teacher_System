package com.example.ai_teacher_system_back.controller;

import com.example.ai_teacher_system_back.common.Result;
import com.example.ai_teacher_system_back.entity.User;
import com.example.ai_teacher_system_back.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 处理用户相关请求
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取当前用户信息
     * @return 用户信息
     */
    @GetMapping("/current")
    public Result<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return Result.success(user, "获取成功");
    }

    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return Result.success(user, "获取成功");
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新后的用户信息
     */
    @PutMapping
    public Result<User> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
        // 确保只能更新自己的信息
        if (!currentUser.getId().equals(user.getId())) {
            return Result.error("无权更新其他用户信息");
        }
        
        // 保持密码不变
        user.setPassword(currentUser.getPassword());
        User updatedUser = userService.updateUser(user);
        return Result.success(updatedUser, "更新成功");
    }

    /**
     * 修改密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    @PostMapping("/change-password")
    public Result<Void> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        
        boolean success = userService.changePassword(user.getId(), oldPassword, newPassword);
        return Result.success("密码修改成功");
    }

    /**
     * 更新头像
     * @param avatarUrl 头像URL
     * @return 更新后的用户信息
     */
    @PostMapping("/update-avatar")
    public Result<User> updateAvatar(@RequestParam String avatarUrl) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        
        User updatedUser = userService.updateAvatar(user.getId(), avatarUrl);
        return Result.success(updatedUser, "头像更新成功");
    }
}