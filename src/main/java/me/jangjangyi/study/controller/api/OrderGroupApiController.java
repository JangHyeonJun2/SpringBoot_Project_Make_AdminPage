package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import me.jangjangyi.study.controller.CrudController;
import me.jangjangyi.study.ifs.CrudInterface;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.OrderGroupApiRequest;
import me.jangjangyi.study.model.network.response.OrderGroupApiReponse;
import me.jangjangyi.study.service.OrderGroupApiLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/api/orderGroup")
@RequiredArgsConstructor
public class OrderGroupApiController extends CrudController<OrderGroupApiRequest, OrderGroupApiReponse> {

    private final OrderGroupApiLogicService orderGroupApiLogicService;

    @PostConstruct
    public void init() {
        this.baseService = orderGroupApiLogicService;
    }
}
