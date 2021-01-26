package me.jangjangyi.study.service;

import me.jangjangyi.study.ifs.CrudInterface;
import me.jangjangyi.study.model.entity.User;
import me.jangjangyi.study.model.enumclass.UserStatus;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.UserApiRequest;
import me.jangjangyi.study.model.network.response.UserApiResponse;
import me.jangjangyi.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserApiLogicService extends BaseService<UserApiRequest, UserApiResponse, User> {


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
        return response(newUser);
    }

    @Override
    public Header<UserApiResponse> read(Long id) {
        //id -> repository getOne , getById
        //user -> userApiResponse return
        return baseRepository.findById(id)
                .map(user -> response(user))
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

    private Header<UserApiResponse> response(User user) {
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

        return Header.OK(userApiResponse);
    }
}
