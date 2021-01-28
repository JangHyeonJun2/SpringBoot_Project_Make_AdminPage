package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import me.jangjangyi.study.controller.CrudController;
import me.jangjangyi.study.model.entity.Item;
import me.jangjangyi.study.model.network.request.ItemApiRequest;
import me.jangjangyi.study.model.network.response.ItemApiResponse;
import me.jangjangyi.study.service.ItemApiLogicService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemApiController extends CrudController<ItemApiRequest,ItemApiResponse, Item> {



}
