package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import me.jangjangyi.study.controller.CrudController;
import me.jangjangyi.study.model.entity.Category;
import me.jangjangyi.study.model.network.request.CategoryApiRequest;
import me.jangjangyi.study.model.network.response.CategoryApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryApiController extends CrudController<CategoryApiRequest, CategoryApiResponse, Category> {


}
