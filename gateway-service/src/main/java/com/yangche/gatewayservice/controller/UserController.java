package com.yangche.gatewayservice.controller;

import com.yangche.gatewayservice.model.User;
import com.yangche.gatewayservice.model.to.RegisterTO;
import com.yangche.gatewayservice.service.UserService;
import jakarta.validation.Valid;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public User getUserInfo(@RequestParam("userId") Long userId) {
        return userService.getUserInfo(userId);
    }

    @PostMapping("/register")
    public String register(@RequestBody @Valid RegisterTO registerTO) {
        userService.register(registerTO);
        return "註冊成功";
    }

    @PostMapping("/login")
    public String login(Authentication authentication) {
        var username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return "登入成功！帳號 " + username + " 的權限為: " + authorities;
    }

}
