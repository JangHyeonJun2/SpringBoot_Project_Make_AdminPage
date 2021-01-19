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
        return null;
    }

    @Override
    public Header<OrderGroupApiReponse> update(Header<OrderGroupApiRequest> request) {
        return null;
    }

    @Override
    public Header delete(Long id) {
        return null;
    }

    private Header<OrderGroupApiReponse> response(OrderGroup orderGroup) {
        OrderGroupApiReponse body = OrderGroupApiReponse.builder()
                                            .id(orderGroup.getId())
                                            .status(orderGroup.getStatus())
                                            .orderType(orderGroup.getOrderType())
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
