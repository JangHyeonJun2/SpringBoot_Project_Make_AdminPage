package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import me.jangjangyi.study.controller.CrudController;
import me.jangjangyi.study.model.entity.Partner;
import me.jangjangyi.study.model.network.request.PartnerApiRequest;
import me.jangjangyi.study.model.network.response.PartnerApiReponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/partner")
@RequiredArgsConstructor
public class PartnerApiController extends CrudController<PartnerApiRequest, PartnerApiReponse, Partner> {


}
