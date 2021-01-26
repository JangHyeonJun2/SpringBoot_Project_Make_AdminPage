package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import me.jangjangyi.study.controller.CrudController;
import me.jangjangyi.study.ifs.CrudInterface;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.ItemApiRequest;
import me.jangjangyi.study.model.network.response.ItemApiResponse;
import me.jangjangyi.study.service.ItemApiLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemApiController extends CrudController<ItemApiRequest,ItemApiResponse> {

    private final ItemApiLogicService itemApiLogicService;

    @PostConstruct
    public void init(){
        this.baseService = itemApiLogicService;
    }
}
