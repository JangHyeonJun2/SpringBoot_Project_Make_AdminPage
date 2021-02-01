package me.jangjangyi.study.service;

import me.jangjangyi.study.model.entity.Category;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.CategoryApiRequest;
import me.jangjangyi.study.model.network.response.CategoryApiResponse;
import me.jangjangyi.study.model.network.response.UserApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryApiService extends BaseService<CategoryApiRequest, CategoryApiResponse,Category> {


    @Override
    public Header<CategoryApiResponse> create(Header<CategoryApiRequest> request) {
        CategoryApiRequest body = request.getData();
        Category category = Category.builder()
                .type(body.getType())
                .title(body.getTitle())
                .build();
        Category save = baseRepository.save(category);
        return response(save);
    }

    @Override
    public Header<CategoryApiResponse> read(Long id) {
        return baseRepository.findById(id).map(this::response).orElseGet(() -> Header.ERROR("데이터가 없습니다."));
    }

    @Override
    public Header<CategoryApiResponse> update(Header<CategoryApiRequest> request) {
        CategoryApiRequest body = request.getData();
        return baseRepository.findById(body.getId())
                .map(category -> {
                    category
                            .setType(body.getType())
                            .setTitle(body.getTitle())
                            .setUpdatedAt(LocalDateTime.now());
                    return category;
                }).map(newCategory -> baseRepository.save(newCategory))
                .map(category -> response(category))
                .orElseGet(() -> Header.ERROR("데이터가 없습니다."));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id).map(category -> {
            baseRepository.delete(category);
            return Header.OK();
        }).orElseGet(() -> Header.ERROR("데이터가 없습니다."));

    }

    @Override
    public Header search(Pageable pageable) {
        return null;
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
