package me.jangjangyi.study.repository;

import me.jangjangyi.study.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.Optional;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void create() {
        User user = new User();
        user.setAccount("TestUser02");
        user.setEmail("TestUser02@naver.com");
        user.setPhoneNumber("010-2222-2222");
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy("admin");


        User newUser = userRepository.save(user);

        System.out.println("newUser : " + newUser);

    }

    @Test
    public void read() {
        Optional<User> user = userRepository.findById(1L);

        user.ifPresent(user1 -> {
            System.out.println("user : " +user1);
            System.out.println("email : " + user1.getEmail());

        });

    }
    @Test
    public void update() {
        Optional<User> user = userRepository.findById(1L);

        user.ifPresent(user1 -> {
            user1.setAccount("pppp");
            user1.setUpdatedAt(LocalDateTime.now());
            user1.setUpdatedBy("update method()");

            userRepository.save(user1);
        });
    }

    public void delete() {

    }
}
