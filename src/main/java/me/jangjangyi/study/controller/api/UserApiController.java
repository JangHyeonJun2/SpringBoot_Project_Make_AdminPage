package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jangjangyi.study.controller.CrudController;
import me.jangjangyi.study.model.entity.User;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.UserApiRequest;
import me.jangjangyi.study.model.network.response.UserApiResponse;
import me.jangjangyi.study.service.UserApiLogicService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController extends CrudController<UserApiRequest,UserApiResponse, User> {

    private final UserApiLogicService userApiLogicService;

    //페이징 처리
    @GetMapping("")
    public Header<List<UserApiResponse>> search(@PageableDefault(sort = "id",direction = Sort.Direction.ASC, size = 15) Pageable pageable) {
        log.info("{}",pageable);
        return userApiLogicService.search(pageable);
    }


}
