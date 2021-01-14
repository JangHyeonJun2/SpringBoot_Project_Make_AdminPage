package me.jangjangyi.study.model.entity;

import me.jangjangyi.study.repository.AdminUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminUserRepositoryTest {
    @Autowired
    private AdminUserRepository adminUserRepository;

    @Test
    public void create() {
        AdminUser adminUser = new AdminUser();
        adminUser.setAccount("AdminUser01");
        adminUser.setPassword("AdminUser01");
        adminUser.setStatus("REGISTERED");
        adminUser.setRole("SUPER");


        AdminUser newAdminUser = adminUserRepository.save(adminUser);
        Assertions.assertThat(newAdminUser).isNotNull();

        newAdminUser.setAccount("CHANGE");
        adminUserRepository.save(newAdminUser);
    }

}