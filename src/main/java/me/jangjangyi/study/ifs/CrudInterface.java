package me.jangjangyi.study.ifs;

import me.jangjangyi.study.model.network.Header;

public interface CrudInterface {
    Header create(); //todo request Object 추가

    Header read(Long id);

    Header update();

    Header delete(Long id);
}
