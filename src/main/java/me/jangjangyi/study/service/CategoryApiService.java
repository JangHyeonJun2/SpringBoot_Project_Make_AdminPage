package me.jangjangyi.study.service;

import me.jangjangyi.study.ifs.CrudInterface;
import me.jangjangyi.study.model.entity.Category;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.CategoryApiRequest;
import me.jangjangyi.study.model.network.response.CategoryApiResponse;
import me.jangjangyi.study.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryApiService implements CrudInterface<CategoryApiRequest, CategoryApiResponse> {
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public Header<CategoryApiResponse> create(Header<CategoryApiRequest> request) {
        CategoryApiRequest body = request.getData();
        Category category = Category.builder()
                .type(body.getType())
                .title(body.getTitle())
                .build();
        Category save = categoryRepository.save(category);
        return response(save);
    }

    @Override
    public Header<CategoryApiResponse> read(Long id) {
        return categoryRepository.findById(id).map(this::response).orElseGet(() -> Header.ERROR("데이터가 없습니다."));
    }

    @Override
    public Header<CategoryApiResponse> update(Header<CategoryApiRequest> request) {
        CategoryApiRequest body = request.getData();
        return categoryRepository.findById(body.getId())
                .map(category -> {
                    category
                            .setType(body.getType())
                            .setTitle(body.getTitle())
                            .setUpdatedAt(LocalDateTime.now());
                    return category;
                }).map(newCategory -> categoryRepository.save(newCategory))
                .map(category -> response(category))
                .orElseGet(() -> Header.ERROR("데이터가 없습니다."));
    }

    @Override
    public Header delete(Long id) {
        return categoryRepository.findById(id).map(category -> {
            categoryRepository.delete(category);
            return Header.OK();
        }).orElseGet(() -> Header.ERROR("데이터가 없습니다."));

    }

    private Header<CategoryApiResponse> response(Category category) {
        CategoryApiResponse body = CategoryApiResponse.builder()
                .id(category.getId())
                .type(category.getType())
                .title(category.getTitle())
                .build();
        return Header.OK(body);
    }
}
