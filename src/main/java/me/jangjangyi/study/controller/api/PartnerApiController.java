package me.jangjangyi.study.controller.api;

import lombok.RequiredArgsConstructor;
import me.jangjangyi.study.controller.CrudController;
import me.jangjangyi.study.ifs.CrudInterface;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.PartnerApiRequest;
import me.jangjangyi.study.model.network.response.PartnerApiReponse;
import me.jangjangyi.study.repository.PartnerRepository;
import me.jangjangyi.study.service.PartnerApiLogicService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/api/partner")
@RequiredArgsConstructor
public class PartnerApiController extends CrudController<PartnerApiRequest, PartnerApiReponse> {

    private final PartnerApiLogicService partnerApiLogicService;

    @PostConstruct
    public void init() {
        this.baseService = partnerApiLogicService;
    }
}
