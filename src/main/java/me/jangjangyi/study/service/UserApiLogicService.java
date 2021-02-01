package me.jangjangyi.study.service;

import me.jangjangyi.study.controller.api.UserOrderInfoApiResponse;
import me.jangjangyi.study.model.entity.Item;
import me.jangjangyi.study.model.entity.OrderGroup;
import me.jangjangyi.study.model.entity.User;
import me.jangjangyi.study.model.enumclass.UserStatus;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.Pagination;
import me.jangjangyi.study.model.network.request.UserApiRequest;
import me.jangjangyi.study.model.network.response.ItemApiResponse;
import me.jangjangyi.study.model.network.response.OrderGroupApiReponse;
import me.jangjangyi.study.model.network.response.UserApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserApiLogicService extends BaseService<UserApiRequest, UserApiResponse, User> {

    @Autowired
    OrderGroupApiLogicService orderGroupApiLogicService;

    @Autowired
    ItemApiLogicService itemApiLogicService;


    //1. request data
    //2. user 생성
    //3. 생성된 데이터 -> UserApiResponse return
    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request) {
        //1. request Data
        UserApiRequest userApiRequest = request.getData();

        //2. User 생성
        User user = User.builder()
                .account(userApiRequest.getAccount())
                .password(userApiRequest.getPassword())
                .status(UserStatus.REGISTERED)
                .phoneNumber(userApiRequest.getPhoneNumber())
                .email(userApiRequest.getEmail())
                .registeredAt(LocalDateTime.now())
                .build();

        User newUser = baseRepository.save(user);

        //3. 생성된 데이터 -> userApiResponse return
        return Header.OK(response(newUser));
    }

    @Override
    public Header<UserApiResponse> read(Long id) {
        //id -> repository getOne , getById
        //user -> userApiResponse return
        return baseRepository.findById(id)
                .map(user -> response(user))
                .map(userApiResponse -> Header.OK(userApiResponse))
                .orElseGet(() -> Header.ERROR("데이터 없음"));

    }

    @Override
   public Header<UserApiResponse> update(Header<UserApiRequest> request) {
        //1. data
        UserApiRequest userApiRequest = request.getData();

        //2.id -> user 데이터를 찾고
        Optional<User> optionalUser = baseRepository.findById(userApiRequest.getId());

        //3.update
        optionalUser.map(user ->
                user.setAccount(userApiRequest.getAccount())
                    .setPhoneNumber(userApiRequest.getPhoneNumber())
                    .setEmail(userApiRequest.getEmail())
                    .setStatus(userApiRequest.getStatus())
                    .setPassword(userApiRequest.getPassword())
                    .setRegisteredAt(userApiRequest.getRegisteredAt())
                    .setUnregisteredAt(userApiRequest.getUnregisteredAt()))
                .map(user -> baseRepository.save(user)) //update가 일어남
                .map(updatedUser -> response(updatedUser))            //userApiResponse 생성
                .map(userApiResponse -> Header.OK(userApiRequest))
                .orElseGet(() -> Header.ERROR("데이터 없음"));

        //4.userApiResponse

        return null;
    }

    @Override
    public Header delete(Long id) {
        // id -> respository -> user
        Optional<User> optionalUser = baseRepository.findById(id);

        // respository -> delete
        return optionalUser
                .map(user -> {
                    baseRepository.delete(user);

                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터가 없습니다."));
        //response return
    }

    private UserApiResponse response(User user) {
        //user  ->  userApiResponse

        UserApiResponse userApiResponse = UserApiResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword()) //todo 암호화 , 길이
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .registeredAt(user.getRegisteredAt())
                .unregisteredAt(user.getUnregisteredAt())
                .status(user.getStatus())
                .build();

        // Header + data return

        return userApiResponse;
    }

    public Header<List<UserApiResponse>> search(Pageable pageable) {
        Page<User> users = baseRepository.findAll(pageable);
        List<UserApiResponse> userApiResponseList = users.stream()
                .map(user -> response(user))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(users.getTotalPages())
                .totalElements(users.getTotalElements())
                .currentPage(users.getNumber())
                .currentElements(users.getNumberOfElements())
                .build();

        return Header.OK(userApiResponseList,pagination);
    }

    public Header<UserOrderInfoApiResponse> orderInfo(Long id) {

        //user
        User user = baseRepository.getOne(id);
        UserApiResponse userApiResponse = response(user);


        //orderGroup
        List<OrderGroup> orderGroupList = user.getOrderGroupList();
        List<OrderGroupApiReponse> orderGroupApiReponseList = orderGroupList.stream()
                .map(orderGroup -> {
                    OrderGroupApiReponse orderGroupApiReponse = orderGroupApiLogicService.response(orderGroup).getData();

                    // item api response
                    List<ItemApiResponse> itemApiResponseList = orderGroup.getOrderDetailList().stream()
                            .map(orderDetail -> orderDetail.getItem())
                            .map(item -> itemApiLogicService.response(item).getData())
                            .collect(Collectors.toList());

                    orderGroupApiReponse.setItemApiResponseList(itemApiResponseList);
                    return orderGroupApiReponse;
                })
                .collect(Collectors.toList());
        userApiResponse.setOrderGroupApiReponseList(orderGroupApiReponseList);

        UserOrderInfoApiResponse userOrderInfoApiResponse = UserOrderInfoApiResponse.builder()
                .userApiResponse(userApiResponse)
                .build();

        return Header.OK(userOrderInfoApiResponse);

    }
}
