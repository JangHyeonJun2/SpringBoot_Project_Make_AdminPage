package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import me.jangjangyi.study.controller.CrudController;
import me.jangjangyi.study.ifs.CrudInterface;
import me.jangjangyi.study.model.entity.AdminUser;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.AdminUserApiRequest;
import me.jangjangyi.study.model.network.response.AdminUserApiResponse;
import me.jangjangyi.study.service.AdminUserApiService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/api/adminUser")
@RequiredArgsConstructor
public class AdminUserApiController extends CrudController<AdminUserApiRequest, AdminUserApiResponse, AdminUser> {


}
