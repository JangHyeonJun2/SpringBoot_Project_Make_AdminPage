package me.jangjangyi.study.service;

import me.jangjangyi.study.model.entity.OrderGroup;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.OrderGroupApiRequest;
import me.jangjangyi.study.model.network.response.OrderGroupApiReponse;
import me.jangjangyi.study.model.network.response.UserApiResponse;
import me.jangjangyi.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderGroupApiLogicService extends BaseService<OrderGroupApiRequest, OrderGroupApiReponse,OrderGroup> {

    @Autowired
    UserRepository userRepository;

    @Override
    public Header<OrderGroupApiReponse> create(Header<OrderGroupApiRequest> request) {
        OrderGroupApiRequest body = request.getData();

        OrderGroup orderGroup = OrderGroup.builder()
                                    .status(body.getStatus())
                                    .orderType(body.getOrderType())
                                    .revAddress(body.getRevAddress())
                                    .revName(body.getRevName())
                                    .paymentType(body.getPaymentType())
                                    .totalPrice(body.getTotalPrice())
                                    .totalQuantity(body.getTotalQuantity())
                                    .orderAt(LocalDateTime.now())
                                    .user(userRepository.getOne(body.getUserId()))
                                    .build();

        OrderGroup newOrderGroup = baseRepository.save(orderGroup);

        return response(newOrderGroup);
    }

    @Override
    public Header<OrderGroupApiReponse> read(Long id) {
        return baseRepository.findById(id)
                .map(this::response)
                .orElseGet(() -> Header.ERROR("데이터가 없습니다."));

    }

    @Override
    public Header<OrderGroupApiReponse> update(Header<OrderGroupApiRequest> request) {
        OrderGroupApiRequest body = request.getData();

        return baseRepository.findById(body.getId())
                .map(orderGroup -> {
                    orderGroup
                            .setStatus(body.getStatus())
                            .setOrderType(body.getOrderType())
                            .setRevAddress(body.getRevAddress())
                            .setRevName(body.getRevName())
                            .setPaymentType(body.getPaymentType())
                            .setTotalQuantity(body.getTotalQuantity())
                            .setTotalPrice(body.getTotalPrice())
                            .setOrderAt(body.getOrderAt())
                            .setArrivalDate(body.getArrivalDate())
                            .setUser(userRepository.getOne(body.getUserId()));
                    return orderGroup;

                })
                .map(orderGroup -> baseRepository.save(orderGroup))
                .map(orderGroup -> response(orderGroup))
                .orElseGet(() -> Header.ERROR("데이터가 없습니다."));

    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(orderGroup -> {
                    baseRepository.delete(orderGroup); //delete는 반환값이 없기 떄문에 따로 return값을 설정해줘야한다.
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터 없습니다."));

    }


    @Override
    public Header search(Pageable pageable) {
        return null;
    }

    public Header<OrderGroupApiReponse> response(OrderGroup orderGroup) {
        OrderGroupApiReponse body = OrderGroupApiReponse.builder()
                                            .id(orderGroup.getId())
                                            .status(orderGroup.getStatus())
                                            .orderType(orderGroup.getOrderType().getTitle())
                                            .revAddress(orderGroup.getRevAddress())
                                            .revName(orderGroup.getRevName())
                                            .paymentType(orderGroup.getPaymentType())
                                            .totalPrice(orderGroup.getTotalPrice())
                                            .totalQuantity(orderGroup.getTotalQuantity())
                                            .orderAt(orderGroup.getOrderAt())
                                            .arrivalDate(orderGroup.getArrivalDate())
                                            .userId(orderGroup.getUser().getId())
                                            .build();
        return Header.OK(body);
    }


}
