package me.jangjangyi.study.repository;

import me.jangjangyi.study.model.entity.Item;
import me.jangjangyi.study.model.entity.OrderDetail;
import me.jangjangyi.study.model.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderDetailRepositoryTest {
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void create() {
        OrderDetail orderDetail = new OrderDetail();
        Optional<Item> findItem = itemRepository.findById(1L);
        Optional<User> findUser = userRepository.findById(1L);

        orderDetail.setOrderAt(LocalDateTime.now());
        orderDetail.setUser(findUser.get());
        orderDetail.setItem(findItem.get());

        orderDetailRepository.save(orderDetail);



    }
    @Test
    public void update() {
        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(1L);
        orderDetail.ifPresent(findItem -> {
            Item item = findItem.getItem();
            item.setId(3L);
            orderDetailRepository.save(findItem);
        });
    }
}