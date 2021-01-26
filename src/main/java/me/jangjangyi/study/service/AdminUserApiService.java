package me.jangjangyi.study.service;

import me.jangjangyi.study.ifs.CrudInterface;
import me.jangjangyi.study.model.entity.AdminUser;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.AdminUserApiRequest;
import me.jangjangyi.study.model.network.response.AdminUserApiResponse;
import me.jangjangyi.study.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdminUserApiService implements CrudInterface<AdminUserApiRequest, AdminUserApiResponse> {

    @Autowired
    private AdminUserRepository adminUserRepository;
    @Override
    public Header<AdminUserApiResponse> create(Header<AdminUserApiRequest> request) {
        AdminUserApiRequest body = request.getData();
        AdminUser adminUser = AdminUser.builder()
                .account(body.getAccount())
                .password(body.getPassword())
                .status(body.getStatus())
                .role(body.getRole())
                .lastLoginAt(LocalDateTime.now())
                .registeredAt(LocalDateTime.now())
                .unregisteredAt(null)
                .build();
        AdminUser saveUser = adminUserRepository.save(adminUser);

        return response(saveUser);
    }

    @Override
    public Header<AdminUserApiResponse> read(Long id) {
        return adminUserRepository.findById(id)
                .map(adminUser -> response(adminUser))
                .orElseGet(() -> Header.ERROR("데이터가 없습니다."));
    }

    @Override
    public Header<AdminUserApiResponse> update(Header<AdminUserApiRequest> request) {
        AdminUserApiRequest body = request.getData();
        Optional<AdminUser> byId = adminUserRepository.findById(body.getId());
        Optional<String> userPassword = byId.map(adminUser -> {
            return adminUser.getPassword();
        });
        if (userPassword != null) {
            if (!body.getPassword().equals(userPassword)){
                System.out.println("비밀번호 바꾸기");
                return adminUserRepository.findById(body.getId())
                        .map(adminUser -> {
                            adminUser
                                    .setAccount(body.getAccount())
                                    .setPassword(body.getPassword())
                                    .setStatus(body.getStatus())
                                    .setRole(body.getRole())
                                    .setLoginFailCount(body.getLoginFailCount())
                                    .setPasswordUpdatedAt(LocalDateTime.now());
                            return adminUser;
                        })
                        .map(newAdminUser -> adminUserRepository.save(newAdminUser))
                        .map(adminUser -> response(adminUser))
                        .orElseGet(() -> Header.ERROR("데이터가 없습니다."));
            }
        }

        System.out.println("비밀번호 안바꾸기");
        return adminUserRepository.findById(body.getId())
                .map(adminUser -> {
                    adminUser
                            .setAccount(adminUser.getAccount())
                            .setStatus(adminUser.getStatus())
                            .setLoginFailCount(adminUser.getLoginFailCount())
                            .setRole(adminUser.getRole());
                    return adminUser;
                })
                .map(newAdminUser -> adminUserRepository.save(newAdminUser))
                .map(adminUser -> response(adminUser))
                .orElseGet(() -> Header.ERROR("데이터가 없습니다."));

    }

    @Override
    public Header delete(Long id) {
        return adminUserRepository.findById(id)
                .map(adminUser -> {
                    adminUserRepository.delete(adminUser);
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터가 없습니다."));
    }

    private Header<AdminUserApiResponse> response(AdminUser adminUser) {
        AdminUserApiResponse body = AdminUserApiResponse.builder()
                .id(adminUser.getId())
                .account(adminUser.getAccount())
                .password(adminUser.getPassword())
                .status(adminUser.getStatus())
                .role(adminUser.getRole())
                .lastLoginAt(adminUser.getLastLoginAt())
                .passwordUpdatedAt(adminUser.getPasswordUpdatedAt())
                .loginFailCount(adminUser.getLoginFailCount())
                .registeredAt(adminUser.getRegisteredAt())
                .unregisteredAt(adminUser.getUnregisteredAt())
                .build();
        return Header.OK(body);
    }
}
