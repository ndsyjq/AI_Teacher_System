package com.example.ai_teacher_system_back.controller;

import com.example.ai_teacher_system_back.common.Result;
import com.example.ai_teacher_system_back.dto.LoginRequest;
import com.example.ai_teacher_system_back.dto.RegisterRequest;
import com.example.ai_teacher_system_back.dto.TokenResponse;
import com.example.ai_teacher_system_back.entity.User;
import com.example.ai_teacher_system_back.service.UserService;
import com.example.ai_teacher_system_back.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 认证控制器
 * 处理用户登录、注册等认证相关请求
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * 用户登录
     * @param loginRequest 登录请求参数
     * @return JWT令牌
     */
    @PostMapping("/login")
    public Result<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // 认证用户
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        
        // 设置认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 生成JWT令牌
        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user);
        
        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userService.updateUser(user);
        
        // 返回令牌
        TokenResponse tokenResponse = new TokenResponse(token, user.getUsername(), user.getRealName() != null ? user.getRealName() : "");
        return Result.success(tokenResponse, "登录成功");
    }

    /**
     * 用户注册
     * @param registerRequest 注册请求参数
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return Result.error("用户名已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword()); // 密码加密在Service层处理
        userService.createUser(user);
        
        return Result.success("注册成功");
    }

    /**
     * 退出登录
     * @return 退出结果
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        SecurityContextHolder.clearContext();
        return Result.success("退出成功");
    }
}