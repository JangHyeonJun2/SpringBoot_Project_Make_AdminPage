package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jangjangyi.study.controller.CrudController;
import me.jangjangyi.study.ifs.CrudInterface;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.UserApiRequest;
import me.jangjangyi.study.model.network.response.UserApiResponse;
import me.jangjangyi.study.service.UserApiLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController extends CrudController<UserApiRequest,UserApiResponse> {


    private final UserApiLogicService userApiLogicService;

    @PostConstruct
    public void init() {
        this.baseService = userApiLogicService;
    }
}
