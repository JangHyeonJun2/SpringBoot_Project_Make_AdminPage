package me.jangjangyi.study.service;

import lombok.RequiredArgsConstructor;
import me.jangjangyi.study.ifs.CrudInterface;
import me.jangjangyi.study.model.entity.OrderGroup;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.OrderGroupApiRequest;
import me.jangjangyi.study.model.network.response.OrderGroupApiReponse;
import me.jangjangyi.study.repository.OrderGroupRepository;
import me.jangjangyi.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderGroupApiLogicService implements CrudInterface<OrderGroupApiRequest, OrderGroupApiReponse> {

    @Autowired
    OrderGroupRepository orderGroupRepository;

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

        OrderGroup newOrderGroup = orderGroupRepository.save(orderGroup);

        return response(newOrderGroup);
    }

    @Override
    public Header<OrderGroupApiReponse> read(Long id) {
        return orderGroupRepository.findById(id)
                .map(this::response)
                .orElseGet(() -> Header.ERROR("데이터가 없습니다."));

    }

    @Override
    public Header<OrderGroupApiReponse> update(Header<OrderGroupApiRequest> request) {
        OrderGroupApiRequest body = request.getData();

        return orderGroupRepository.findById(body.getId())
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
                .map(orderGroup -> orderGroupRepository.save(orderGroup))
                .map(orderGroup -> response(orderGroup))
                .orElseGet(() -> Header.ERROR("데이터가 없습니다."));

    }

    @Override
    public Header delete(Long id) {
        return orderGroupRepository.findById(id)
                .map(orderGroup -> {
                    orderGroupRepository.delete(orderGroup); //delete는 반환값이 없기 떄문에 따로 return값을 설정해줘야한다.
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터 없습니다."));

    }

    private Header<OrderGroupApiReponse> response(OrderGroup orderGroup) {
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
