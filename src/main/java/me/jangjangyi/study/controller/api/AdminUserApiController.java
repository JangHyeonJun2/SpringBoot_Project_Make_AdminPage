package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import me.jangjangyi.study.controller.CrudController;
import me.jangjangyi.study.model.entity.AdminUser;
import me.jangjangyi.study.model.network.request.AdminUserApiRequest;
import me.jangjangyi.study.model.network.response.AdminUserApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/adminUser")
@RequiredArgsConstructor
public class AdminUserApiController extends CrudController<AdminUserApiRequest, AdminUserApiResponse, AdminUser> {


}
