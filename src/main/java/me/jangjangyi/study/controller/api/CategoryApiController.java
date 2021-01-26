package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import me.jangjangyi.study.controller.CrudController;
import me.jangjangyi.study.ifs.CrudInterface;
import me.jangjangyi.study.model.entity.Category;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.CategoryApiRequest;
import me.jangjangyi.study.model.network.response.CategoryApiResponse;
import me.jangjangyi.study.service.CategoryApiService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryApiController extends CrudController<CategoryApiRequest, CategoryApiResponse, Category> {


}
