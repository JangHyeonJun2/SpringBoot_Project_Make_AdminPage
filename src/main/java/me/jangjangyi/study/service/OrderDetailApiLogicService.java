package me.jangjangyi.study.service;

import me.jangjangyi.study.ifs.CrudInterface;
import me.jangjangyi.study.model.entity.OrderDetail;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.OrderDetailApiRequest;
import me.jangjangyi.study.model.network.response.OrderDetailApiReponse;
import me.jangjangyi.study.repository.ItemRepository;
import me.jangjangyi.study.repository.OrderDetailRepository;
import me.jangjangyi.study.repository.OrderGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderDetailApiLogicService implements CrudInterface<OrderDetailApiRequest, OrderDetailApiReponse> {
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    OrderGroupRepository orderGroupRepository;
    @Autowired
    ItemRepository itemRepository;

    @Override
    public Header<OrderDetailApiReponse> create(Header<OrderDetailApiRequest> request) {
        OrderDetailApiRequest body = request.getData();
        OrderDetail orderDetail = OrderDetail.builder()
                .status(body.getStatus())
                .arrivalDate(LocalDateTime.now())
                .quantity(body.getQuantity())
                .totalPrice(body.getTotalPrice())
                .orderGroup(orderGroupRepository.getOne(body.getOrderGroupId()))
                .item(itemRepository.getOne(body.getItemId()))
                .build();

        OrderDetail newOrderDetail = orderDetailRepository.save(orderDetail);

        return response(newOrderDetail);
    }

    @Override
    public Header<OrderDetailApiReponse> read(Long id) {
        return null;
    }

    @Override
    public Header<OrderDetailApiReponse> update(Header<OrderDetailApiRequest> request) {
        return null;
    }

    @Override
    public Header delete(Long id) {
        return null;
    }


    private Header<OrderDetailApiReponse> response(OrderDetail orderDetail){
        OrderDetailApiReponse body = OrderDetailApiReponse.builder()
                .id(orderDetail.getId())
                .status(orderDetail.getStatus())
                .arrivalDate(orderDetail.getArrivalDate())
                .quantity(orderDetail.getQuantity())
                .totalPrice(orderDetail.getTotalPrice())
                .orderGroupId(orderDetail.getOrderGroup().getId())
                .itemId(orderDetail.getItem().getId())
                .build();

        return Header.OK(body);
    }
}
