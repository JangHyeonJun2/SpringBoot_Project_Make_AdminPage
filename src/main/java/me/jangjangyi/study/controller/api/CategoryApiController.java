package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import me.jangjangyi.study.ifs.CrudInterface;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.CategoryApiRequest;
import me.jangjangyi.study.model.network.response.CategoryApiResponse;
import me.jangjangyi.study.service.CategoryApiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryApiController implements CrudInterface<CategoryApiRequest, CategoryApiResponse> {

    private final CategoryApiService categoryApiService;

    @Override
    @PostMapping("")
    public Header<CategoryApiResponse> create(@RequestBody Header<CategoryApiRequest> request) {
        return categoryApiService.create(request);
    }

    @Override
    @GetMapping("{id}")
    public Header<CategoryApiResponse> read(@PathVariable Long id) {
        return categoryApiService.read(id);
    }

    @Override
    @PutMapping("")
    public Header<CategoryApiResponse> update(@RequestBody Header<CategoryApiRequest> request) {
        return categoryApiService.update(request);
    }

    @Override
    @DeleteMapping("{id}")
    public Header delete(@PathVariable Long id) {
        return categoryApiService.delete(id);
    }
}
