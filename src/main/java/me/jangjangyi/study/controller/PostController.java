package me.jangjangyi.study.controller;

import me.jangjangyi.study.model.SearchParam;
import org.springframework.web.bind.annotation.*;

/**
 * HTML < Form >
 *     ajax 검색
 *     http post body -> data이기 때문에 파라미터에 @RequestBody 애너테이션을 사용했다.
 */
@RestController
@RequestMapping("/api")
public class PostController {

//    아래와 동일하다
//    @RequestMapping(method = RequestMethod.POST, path = "/postMethod")
    @PostMapping("/postMethod")
    public SearchParam postMethod(@RequestBody SearchParam searchParam) {

        return searchParam;
    }

    @PutMapping
    public void put() {

    }

    @PatchMapping
    public void patch() {

    }
}
