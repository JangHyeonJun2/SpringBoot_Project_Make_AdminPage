package me.jangjangyi.study.service;

import me.jangjangyi.study.model.entity.Partner;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.PartnerApiRequest;
import me.jangjangyi.study.model.network.response.PartnerApiReponse;
import me.jangjangyi.study.model.network.response.UserApiResponse;
import me.jangjangyi.study.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PartnerApiLogicService extends BaseService<PartnerApiRequest, PartnerApiReponse,Partner> {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Header<PartnerApiReponse> create(Header<PartnerApiRequest> request) {
        PartnerApiRequest body = request.getData();
        Partner partner = Partner.builder()
                .name(body.getName())
                .status(body.getStatus())
                .address(body.getAddress())
                .callCenter(body.getCallCenter())
                .partnerNumber(body.getPartnerNumber())
                .businessNumber(body.getBusinessNumber())
                .ceoName(body.getCeoName())
                .registeredAt(LocalDateTime.now())
                .category(categoryRepository.getOne(body.getCategoryId()))
                .build();

        Partner newPartner = baseRepository.save(partner);

        return response(newPartner);
    }

    @Override
    public Header<PartnerApiReponse> read(Long id) {
        return baseRepository.findById(id).map(this::response).orElseGet(() -> Header.ERROR("데이터가 없습니다."));
    }

    @Override
    public Header<PartnerApiReponse> update(Header<PartnerApiRequest> request) {
        PartnerApiRequest body = request.getData();
        return baseRepository.findById(body.getId()).map(partner -> {
            partner
                    .setName(body.getName())
                    .setStatus(body.getStatus())
                    .setAddress(body.getAddress())
                    .setCallCenter(body.getCallCenter())
                    .setPartnerNumber(body.getPartnerNumber())
                    .setBusinessNumber(body.getBusinessNumber())
                    .setRegisteredAt(LocalDateTime.now())
                    .setUnregisteredAt(body.getUnregisteredAt())
                    .setCeoName(body.getCeoName())
                    .setCategory(categoryRepository.getOne(body.getCategoryId()));
            return partner;
        }).map(newPartner -> baseRepository.save(newPartner)).map(partner -> response(partner)).orElseGet(() -> Header.ERROR("데이터가 없습니다."));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id).map(partner -> {
            baseRepository.delete(partner);
            return Header.OK();
        }).orElseGet(() -> Header.ERROR("데이터가 없습니다."));
    }

    private Header<PartnerApiReponse> response(Partner partner) {
        PartnerApiReponse body = PartnerApiReponse.builder()
                .id(partner.getId())
                .name(partner.getName())
                .status(partner.getStatus())
                .address(partner.getAddress())
                .callCenter(partner.getCallCenter())
                .partnerNumber(partner.getPartnerNumber())
                .businessNumber(partner.getBusinessNumber())
                .ceoName(partner.getCeoName())
                .registeredAt(partner.getRegisteredAt())
                .categoryId(partner.getCategory().getId())
                .build();
        return Header.OK(body);
    }


}
