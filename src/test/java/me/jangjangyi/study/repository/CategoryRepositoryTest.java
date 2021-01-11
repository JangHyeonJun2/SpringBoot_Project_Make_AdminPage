package me.jangjangyi.study.repository;

import me.jangjangyi.study.model.entity.Category;
import org.apache.tomcat.jni.Local;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void create() {
        String type = "COMPUTER";
        String title = "컴퓨터";
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "AdminServer";

        Category category = new Category();

        category.setType(type);
        category.setTitle(title);
        category.setCreatedAt(createdAt);
        category.setCreatedBy(createdBy);

        Category newCategory = categoryRepository.save(category);

        assertThat(newCategory).isNotNull();
        assertThat(newCategory.getType()).isEqualTo(type);
        assertThat(newCategory.getTitle()).isEqualTo(title);
    }

    @Test
    public void read() {
        Optional<Category> id = categoryRepository.findByType("COMPUTER");
        id.ifPresent(findCa -> {
            System.out.println(findCa.getId());
            System.out.println(findCa.getType());
            System.out.println(findCa.getTitle());
        });
    }
}
