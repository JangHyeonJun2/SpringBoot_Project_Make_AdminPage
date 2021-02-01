package me.jangjangyi.study.controller;


import lombok.extern.slf4j.Slf4j;
import me.jangjangyi.study.ifs.CrudInterface;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Slf4j
public abstract class CrudController<Req,Res,Entity> implements CrudInterface<Req,Res> {

    @Autowired(required = false)
    protected BaseService<Req,Res,Entity> baseService;

    @Override
    @PostMapping("")
    public Header<Res> create(@RequestBody Header<Req> request) {
        return baseService.create(request);
    }

    @Override
    @GetMapping("{id}")
    public Header<Res> read(@PathVariable Long id) {
        return baseService.read(id);
    }

    @Override
    @PutMapping("")
    public Header<Res> update(@RequestBody Header<Req> request) {
        return baseService.update(request);
    }

    @Override
    @DeleteMapping("{id}")
    public Header delete(@PathVariable Long id) {
        return baseService.delete(id);
    }
}