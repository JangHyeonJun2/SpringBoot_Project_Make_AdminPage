package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import me.jangjangyi.study.controller.CrudController;
import me.jangjangyi.study.model.entity.OrderGroup;
import me.jangjangyi.study.model.network.request.OrderGroupApiRequest;
import me.jangjangyi.study.model.network.response.OrderGroupApiReponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orderGroup")
@RequiredArgsConstructor
public class OrderGroupApiController extends CrudController<OrderGroupApiRequest, OrderGroupApiReponse, OrderGroup> {

}
