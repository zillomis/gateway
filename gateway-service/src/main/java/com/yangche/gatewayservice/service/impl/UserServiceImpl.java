package com.yangche.gatewayservice.service.impl;

import static com.yangche.gatewayservice.constant.RoleType.*;

import com.yangche.gatewayservice.client.UserClient;
import com.yangche.gatewayservice.model.Role;
import com.yangche.gatewayservice.model.User;
import com.yangche.gatewayservice.model.UserRole;
import com.yangche.gatewayservice.model.to.RegisterTO;
import com.yangche.gatewayservice.repository.IRoleRepo;
import com.yangche.gatewayservice.repository.IUserRepo;
import com.yangche.gatewayservice.repository.IUserRoleRepo;
import com.yangche.gatewayservice.service.UserService;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final IUserRepo userRepo;

    private final IRoleRepo roleRepo;

    private final IUserRoleRepo userRoleRepo;

    private final PasswordEncoder passwordEncoder;

    private final UserClient userClient;

    public UserServiceImpl(IUserRepo userRepo, PasswordEncoder passwordEncoder,
            IRoleRepo roleRepo, IUserRoleRepo userRoleRepo, UserClient userClient) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.userRoleRepo = userRoleRepo;
        this.userClient = userClient;
    }

    @Override
    public User getUserInfo(Long id) {
        return userClient.getUserById(id);
    }

    @Override
    @Transactional
    public void register(RegisterTO to) {
        User user = new User();
        var password = passwordEncoder.encode(to.getPassword());
        user.setUsername(to.getUsername());
        user.setPassword(password);
        user.setEmail(to.getEmail());
        user.setPhone(to.getPhone());
        user.setAddress(to.getAddress());
        userRepo.saveAndFlush(user);

        var roleId = roleRepo.findIdByRoleType(NORMAL.name());
        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId);
        userRole.setUserId(user.getUserId());
        userRoleRepo.saveAndFlush(userRole);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepo.findByUserName(username);
    }

    @Override
    public List<Role> getRoleByUserId(Long id) {
        return roleRepo.findByUserId(id);
    }
}
