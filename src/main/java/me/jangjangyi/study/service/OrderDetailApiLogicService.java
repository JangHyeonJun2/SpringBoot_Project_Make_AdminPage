package me.jangjangyi.study.service;

import me.jangjangyi.study.model.entity.OrderDetail;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.OrderDetailApiRequest;
import me.jangjangyi.study.model.network.response.OrderDetailApiReponse;
import me.jangjangyi.study.model.network.response.UserApiResponse;
import me.jangjangyi.study.repository.ItemRepository;
import me.jangjangyi.study.repository.OrderGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderDetailApiLogicService extends BaseService<OrderDetailApiRequest, OrderDetailApiReponse,OrderDetail> {

    @Autowired
    private OrderGroupRepository orderGroupRepository;

    @Autowired
    private ItemRepository itemRepository;

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

        OrderDetail newOrderDetail = baseRepository.save(orderDetail);

        return response(newOrderDetail);
    }

    @Override
    public Header<OrderDetailApiReponse> read(Long id) {
        return baseRepository.findById(id).map(orderDetail -> response(orderDetail)).orElseGet(() -> Header.ERROR("데이터가 없습니다."));
    }

    @Override
    public Header<OrderDetailApiReponse> update(Header<OrderDetailApiRequest> request) {
        OrderDetailApiRequest body = request.getData();
        return baseRepository.findById(body.getId())
                .map(orderDetail -> {
                    orderDetail
                            .setStatus(body.getStatus())
                            .setQuantity(body.getQuantity())
                            .setTotalPrice(body.getTotalPrice())
                            .setArrivalDate(LocalDateTime.now())
                            .setItem(itemRepository.getOne(body.getItemId()))
                            .setOrderGroup(orderGroupRepository.getOne(body.getOrderGroupId()));

                    return orderDetail;
                }).map(newOrderDetail -> {
                    baseRepository.save(newOrderDetail);
                    return newOrderDetail;
                }).map(orderDetail -> response(orderDetail)).orElseGet(() -> Header.ERROR("데이터가 없습니다."));

    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id).map(orderDetail -> {
            baseRepository.delete(orderDetail);
            return Header.OK();
        }).orElseGet(() -> Header.ERROR("데이터가 없습니다."));
    }

    @Override
    public Header search(Pageable pageable) {
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
