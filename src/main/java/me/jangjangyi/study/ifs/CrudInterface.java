package me.jangjangyi.study.ifs;

import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.response.UserApiResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrudInterface<Req,Res> {

//    Header<List<UserApiResponse>> search(Pageable pageable);

    Header<Res> create(Header<Req> request); //todo request Object 추가

    Header<Res> read(Long id);

    Header<Res> update(Header<Req> request);

    Header delete(Long id);
}
