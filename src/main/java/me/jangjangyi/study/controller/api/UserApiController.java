package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jangjangyi.study.controller.CrudController;
import me.jangjangyi.study.model.entity.User;
import me.jangjangyi.study.model.network.request.UserApiRequest;
import me.jangjangyi.study.model.network.response.UserApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController extends CrudController<UserApiRequest,UserApiResponse, User> {



}
