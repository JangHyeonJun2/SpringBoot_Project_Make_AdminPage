package me.jangjangyi.study.repository;

import me.jangjangyi.study.model.entity.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void create() {
        Item item = new Item();
        item.setName("pen");
        item.setPrice(20000);
        item.setContent("is pen");

        Item newItem = itemRepository.save(item);
        Assertions.assertThat(newItem).isNotNull();
    }

    @Test
    public void read() {
        Optional<Item> item = itemRepository.findById(1L);
        item.ifPresent(findItem -> {
            System.out.println(findItem.getId() + ", " + findItem.getName());
        });
    }

    @Test
    public void update() {
        Optional<Item> item = itemRepository.findById(3L);
        item.ifPresent(findItem -> {
            findItem.setId(1L);
            itemRepository.save(findItem);
        });


    }

}