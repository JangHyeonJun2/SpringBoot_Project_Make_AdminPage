package me.jangjangyi.study.orderDetailService;

import me.jangjangyi.study.model.entity.OrderDetail;
import me.jangjangyi.study.repository.OrderDetailRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class ReadOrderDetailService {
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Test
    public void findOrderDetail() {
        Optional<OrderDetail> byId = orderDetailRepository.findById(2L);

        Assertions.assertThat(byId).isNotEmpty();
    }
}

