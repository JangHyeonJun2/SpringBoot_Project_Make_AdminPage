package me.jangjangyi.study.itemService;

import me.jangjangyi.study.model.entity.Item;
import me.jangjangyi.study.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class ReadItemService {

    @Autowired
    ItemRepository itemRepository;
    @Test
    public void readItem() {

        Optional<Item> byId = itemRepository.findById(6L);
        byId.ifPresent(item -> System.out.println(item.getName()));
    }
}
