package me.jangjangyi.study.ifs;

import me.jangjangyi.study.model.network.Header;

public interface CrudInterface<Req,Res> {
    Header<Res> create(Header<Req> request); //todo request Object 추가

    Header<Res> read(Long id);

    Header<Res> update(Header<Req> request);

    Header delete(Long id);
}
