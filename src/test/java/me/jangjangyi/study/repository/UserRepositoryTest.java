package me.jangjangyi.study.repository;

import me.jangjangyi.study.model.entity.Item;
import me.jangjangyi.study.model.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

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
    @Transactional
    public void read() {
        Optional<User> user = userRepository.findById(1L);

        user.ifPresent(user1 -> {
            user1.getOrderDetailList().stream().forEach(orderDetail -> {
                Item item = orderDetail.getItem();
                System.out.println(item);
            });

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
    @Test
    public void delete() {
//        Optional<User> user = userRepository.findById(10L);
//
//        assertThat(user.isPresent());

//        user.ifPresent(user1 ->
//                System.out.println("ID" + user1.getId()));

//        user.ifPresent(user1 -> {
//            userRepository.delete(user1);
//        });

        Optional<User> deleteUser = userRepository.findById(1L);
        assertThat(deleteUser).isEmpty();
    }
}
