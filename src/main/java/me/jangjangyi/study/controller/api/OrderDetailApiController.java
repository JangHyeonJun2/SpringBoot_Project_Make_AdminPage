package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import me.jangjangyi.study.controller.CrudController;
import me.jangjangyi.study.model.entity.OrderDetail;
import me.jangjangyi.study.model.network.request.OrderDetailApiRequest;
import me.jangjangyi.study.model.network.response.OrderDetailApiReponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orderDetail")
@RequiredArgsConstructor
public class OrderDetailApiController extends CrudController<OrderDetailApiRequest, OrderDetailApiReponse, OrderDetail> {


}
