# SpringBoot_Project_Make_AdminPage
# 상품 관련 Admin페이지 제작 프로젝트입니다.
# JPA 개발 순서

1. ERD 설계
2. Table 생성
3. Entity 생성
4. Repository 생성
5. Repository 테스트
6. 연관관계 설정
7. 필요한 Query Method 생성









# 코딩하면서 문제점



## 첫번째 문제점

```java
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void create() {
        User user = new User();
        user.setAccount("TestUser02");
        user.setEmail("TestUser02@naver.com");
        user.setPhoneNumber("010-2222-2222");
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy("admin");


        User newUser = userRepository.save(user);

        System.out.println("newUser : " + newUser);

    }
}
```

위에서와 같은 클래스와 메서드에서 실행을 하면 **User newUser = userRepository.save(user);**여기에서 NullPointerException이 나온다. 

@Autowired로 userRepository로 의존성을 주입해놨지만 의존성이 들어가지 않았다... 계속 해매었지만 방법을 찾았다.

### 스프링부트 단위 테스트

| annotation      | 의미                                                         | Default Auto-configuration                                   |
| --------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| @WebMvcTest     | MVC를 테스트 하기 위한 어노테이션, 간단한 Controller 에 대해서 테스트가 가능하다. | [Imported auto-configuration 참고](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-test-auto-configuration.html#test-auto-configuration) |
| @DataJpaTest    | JPA 테스트 하기 위한 어노테이션, JPA관련만 로드 된다.        | [Imported auto-configuration 참고](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-test-auto-configuration.html#test-auto-configuration) |
| @RestClientTest | REST Client 테스트 용도, RestTemplate과 같은 http client사용시 Mock Server를 만드는 용도 | [Imported auto-configuration 참고](https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-test-auto-configuration.html#test-auto-configuration) |

### 스프링부트 통합 테스트

| annotation      | 의미                                                         | 특이사항 |
| --------------- | ------------------------------------------------------------ | -------- |
| @SpringBootTest | 스프링의 실행 부터 모든 bean을 로드하여, 처음부터 끝까지 모두 테스트 가능 |          |

- 스프링에서 통합 테스트를 위해서는 다음의 Annotation을 사용한다.
  주로 controller 부터 service, repository, external library 등 모든 부분을 테스트하며, 스프링의 모든 Bean을 로드하여 사용하므로 테스트의 시간이 길다.

#### 찾아봐야하는것

- 단위 테스트 애너테이션에서 @DataJpaTest는 단위테스트에서 실행을 하게 되면 실제 DB에는 데이터가 들어오지 않던데 애너테이션 특성인지 찾아보기 만약에 그렇다면 설정 바꿀수 있는지 찾아보기

---------------------------

## 두번째 문제점

```java
package me.jangjangyi.study.repository;

import me.jangjangyi.study.model.entity.OrderDetail;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
class OrderDetailRepositoryTest {
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void create() {
        OrderDetail orderDetail = new OrderDetail();

        orderDetail.setStatus("WAITING");
        orderDetail.setArrivalDate(LocalDateTime.now().plusDays(2));
        orderDetail.setQuantity(1);
        orderDetail.setTotalPrice(BigDecimal.valueOf(1000000));

        orderDetail.setOrderGroupId(1L);
        orderDetail.setItemId(1L);

        orderDetail.setCreatedAt(LocalDateTime.now());
        orderDetail.setCreatedBy("AdminServer");

        OrderDetail newOrderDetail = orderDetailRepository.save(orderDetail);
        Assertions.assertThat(newOrderDetail).isNotNull();

    }
    @Test
    public void update() {

    }
}
```

- 테스트 구문에서 ItemRepository와 UserRepository를 사용할 필요가 있어서 처음에는 

```java
@Autowired
OrderDetailRepository orderDetailRepository;
ItemRepository itemRepository;
UserRepository userRepository;
```

​	이러한 식으로 @Autowired를 진행하였지만 계속해서 의존성 주입이 일어나지 않았고 NullPointerException이 일어났다. 그래서 각 객체에 의존성을 주입을 하기 위해서는 아래와같이 각 객체에 @Autowired를 해주어야 한다.

```
@Autowired
OrderDetailRepository orderDetailRepository;
@Autowired
ItemRepository itemRepository;
@Autowired
UserRepository userRepository;
```

#### mappedBy란?

- 양방향 연관관계에서 주인(?)을 정하는 방법의 속성이다.
- 주인이 아니면 mappedBy 속성을 사용해서 속성의 값으로 연관관계의 주인을 정할 수 있습니다.

현재 코드에서는 User엔티티와 OrderGroup엔티티를 예로 들면 User(1) : OrderGroup(N)의 관계입니다. 하나의 User가 여러개의 OrderGroup을 가지기 때문에 User가 주인입니다. 그래서 아래와 같이 코딩을 하게 됩니다.

```java
@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<OrderGroup> orderGroupList;
```

또한 OrderGroup엔티티에서도 아래와 같이 코딩을 합니다. 여기서 User객체의 user 변수는 위에서의 mappedBy의 "user"와 동일해야합니다.

```java
//OrderGroup N : 1 User
    @ManyToOne
    private User user;	
```

### 세번째 문제점

---

- 상품 Service에서 CRUD를 작성할 때(CREATE부분) request에서 상품의 정보를 받고 이 정보를 respone와 DB에 저장할 때 registeredAt에 대한 문제점.

```java
	@Service
public class ItemApiLogicService implements CrudInterface<ItemApiRequest,ItemApiResponse> {
    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ItemRepository itemRepository;
  
    @Override
    public Header<ItemApiResponse> create(Header<ItemApiRequest> request) {

        ItemApiRequest body = request.getData();

        Item item = Item.builder()
                .status(body.getStatus())
                .name(body.getName())
                .title(body.getTitle())
                .content(body.getContent())
                .price(body.getPrice())
                .brandName(body.getBrandName())
                .registeredAt(LocalDateTime.now()) //여기가 문제점 !!!! 
                .partner(partnerRepository.getOne(body.getPartnerId()))
                .build();

        Item newItem = itemRepository.save(item);
        return response(newItem);

    }
}
```

1. 원래 코드는 **.registeredAt(body.getRegisteredAt())** 으로 받았었지만 생각해보면 사용자가 상품을 생성하면서 날짜를 기입하지 않고 서버에서 자동적으로 만들어서 보여줘야한다. 그래서 **원래 코드**에서 **.registeredAt(LocalDateTime.now())**처럼 자동적으로 현재의 날짜와 시간을 넣어주고 item객체에 저장해야한다. 그러면 현재의 날짜를 가진 item객체가 생성되어  **Item newItem = itemRepository.save(item);**코드에서 DB에 정상적으로 저장이되며 response에도 정상적으로 보여준다.



### OrderGroupApiController에서의 문제점

#### Srping boot @RequestBody로 Json 데이터 받을 시 Json parse error

- 아래는 변경전 코드이다. 애너테이션을 보면 @RequiredArgsConstructor 애너테이션이다. 이 애너테이션은 DI관련 애너테이션인데 만약 필드에 빈을 주입 받을 객체가 하나 밖에 없다면 이 애너테이션을 사용하여 자동적으로 생성자 빈주입을 만들어준다. 하지만 규칙이 있는데 **필드에서 private final** 을 꼭꼭꼭 붙여줘야하는 것이다.!!!!!!!!!!!!!!!!!!!!!!!!!!

```java
@RestController
@RequestMapping("/api/orderGroup")
@RequiredArgsConstructor
public class OrderGroupApiController implements CrudInterface<OrderGroupApiRequest, OrderGroupApiReponse> {

    변경전
    //OrderGroupApiLogicService orderGroupApiLogicService;
      
    변경후
      private final OrderGroupApiLogicService orderGroupApiLogicService;

    @Override
    @PostMapping("")
    public Header<OrderGroupApiReponse> create(@RequestBody Header<OrderGroupApiRequest> request) {
        return orderGroupApiLogicService.create(request);
    }

    @Override
    @GetMapping("{id}")
    public Header<OrderGroupApiReponse> read(@PathVariable Long id) {
        return null;
    }

    @Override
    @PutMapping("")
    public Header<OrderGroupApiReponse> update(@RequestBody Header<OrderGroupApiRequest> request) {
        return null;
    }

    @Override
    @DeleteMapping("{id}")
    public Header delete(@PathVariable Long id) {
        return null;
    }
}
```



--------

# 프로젝트 진행

### JPA추가설정

![image-20210114135214929](/Users/janghyeonjun/Library/Application Support/typora-user-images/image-20210114135214929.png)

- 엔티티를 보면 created_at, created_by, updated_at, updated_by 이러한 column들을 볼 수 있다. 이러한 column들을 자동적으로 넣기 위한 설정을 추가로 하겠다.
- Spring Data는 누군가 entity를 생성 또는 수정할 때 변화를 감지하고 그에 대한 시간 또는 사용자를 감지할 수 있는 기능을 제공한다.

1. config package를 만들고 JpaConfig.class를 만든다.

```java
package me.jangjangyi.study.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

}
--JpaConfig.java
```

- @Configuration 이란??

  - 어떤 임의의 클래스를 만들어서 @Bean 어노테이션을 붙인다고 되는 것이 아니고, @Bean을 사용하는 클래스에는 반드시 @Configuration 어노테이션을 활용하여 해당 클래스에서 Bean을 등록하고자 함을 명시해주어야 한다.

    위의 예제에서도 클래스 이름 위에 @Configuration 어노테이션을 명시하여 해당 클래스에서 1개 이상의 Bean을 생성하고 있음을 명시하고 있다. 그렇기 때문에 @Bean 어노테이션을 사용하는 클래스의 경우 반드시 @Configuration과 함께 사용해주어야 한다.

- @EnableJpaAuditing

  - JPA Auditing을 활성화 하기 위한 애너테이션이다.

2. component 패키지를 만들고 패키지 안에 LoginUserAuditorAware.java클래스를 만듭니다.

```java
package me.jangjangyi.study.component;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginUserAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("AdminServer");
    }
}

```

- 유저정보를 꺼내올 수 있는 기능으로 구현합니다.

3. Spring Data에서 지원하는 annotation들은 어떤 사용자가 entity를 생성 또는 수정했는지 감지하는 `@CreatedBy`, `@LastModifiedBy`와 언제 생성 또는 수정되었는지 감지하는 `@CreatedDate`, `@LastModifiedDate`가 있다. 이러한 애너테이션을 사용하여 각 엔티티에서 필드값에 필요한 애너테이션을 작성합니다.                                                            또한 Auditing 를 사용할 클래스 상단에 `@EntityListeners` 설정합니다.

```java
	package me.jangjangyi.study.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class) -- @EntityListenters적용한 모습
public class AdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account;

    private String password;

    private String status;

    private String role;

    private LocalDateTime lastLoginAt;

    private LocalDateTime passwordUpdatedAt;

    private int loginFailCount;

    private LocalDateTime registeredAt;

    private LocalDateTime unregisteredAt;

    @CreatedDate -- 애너테이션적용
    private LocalDateTime createdAt;

    @CreatedBy -- 애너테이션적용
    private String createdBy;

    @LastModifiedDate -- 애너테이션적용
    private LocalDateTime updatedAt;

    @LastModifiedBy -- 애너테이션적용
    private String updatedBy;
}

```

4. 테스트에서 결과확인

```java
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void create() {
        String account = "Test02";
        String password = "Test02";
        String status = "REGISTERED";
        String email = "Test01@naver.com";
        String phoneNumber = "010-222-3333";
        LocalDateTime registeredAt = LocalDateTime.now();
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "AdminServer";

        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        user.setStatus(status);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setRegisteredAt(registeredAt);
      	user.setCreatedAt(LocalDateTime.now()); -- Auditing을 사용하지 않았을 때 사용한다.
        user.setCreatedBy("AdminServer"); -- Auditing을 사용하지 않았을 때 사용한다.

        User newUser = userRepository.save(user);

        Assertions.assertThat(newUser).isNotNull();

    }
 }
```

- Auditing을 사용했을 때는                                                                                                                                                                                                  user.setCreatedAt(LocalDateTime.now()); -- Auditing을 사용하지 않았을 때 사용한다.
  user.setCreatedBy("AdminServer"); -- Auditing을 사용하지 않았을 때 사용한다.                                                                                                        이 두줄이 필요가 없으며 결과적으로 DB에 자동적으로 들어가게 된다.

### @Builder란?

- Category.java

```java
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
@Builder
@Accessors(chain = true)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String title;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

    //Category 1 : N Partner
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<Partner> partnerList;
}

```

- 이러한 엔티티가 있다. 위에 애너테이션에서 @AllArgsConstructor, @NoArgsConstructor은 생성자에서 모든 생성자를 받는 생성자와 빈 생성자를 자동적으로 만들어주는 애너테이션이다. 하지만 테스트 코드에서 생성자를 호출할 때나 생성자에서 몇개의 값만 설정하고 생성자를 생성하는 경우에는 엔티티에 그에 맞는생성자를 다시 만들어줘야한다. 이러한 불편함이 있기 때문에 @Builder애너테이션을 사용해보았다.<br>Ex)만약에 테스트 코드에서 Category.java클래스에서 type,title만 필요한 생성자가 필요하게 되는 상황.

```java
 @Test
    public void create() {
        String type = "COMPUTER";
        String title = "컴퓨터";
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "AdminServer";

        Category category = new Category();

        category.setType(type);
        category.setTitle(title);
        category.setCreatedAt(createdAt);
        category.setCreatedBy(createdBy);

        Category newCategory = categoryRepository.save(category);

        assertThat(newCategory).isNotNull();
        assertThat(newCategory.getType()).isEqualTo(type);
        assertThat(newCategory.getTitle()).isEqualTo(title);
        
        category.setTitle("").setType(""); -- 이러한 식으로 필요한 값만 생성자에서 만들어서 객체를 만들어 사용하면 된다.
    }
```



------------------

# CRUD 인터페이스 정의와 ResponseBody 공통부 작성

![image-20210114162426432](/Users/janghyeonjun/Library/Application Support/typora-user-images/image-20210114162426432.png)

- 위의 사진과 같은 body 부분을 Front-end 개발자와 공유하기 위한 Json형태로 만들어서 보여주게된다. 이러한 ResponseBody에 어떤 data와 시간, 응답코드를 보여주기 위해서 다음과 같이 작성하였다.

```java
package me.jangjangyi.study.model.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Header<T> {
    //api 통신 시간
    private LocalDateTime transactionTime;

    //api 응답 코드
    private String resultCode;

    //api 부가 설명
    private String description;


    private T data;

    // OK
    public static <T> Header<T> OK() {
        return (Header<T>)Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .description("OK")
                .build();
    }

    //DATA OK
    public static <T> Header<T> OK(T data) {
        return (Header<T>)Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .description("OK")
                .data(data)
                .build();
    }

    //ERROR
    public static <T> Header<T> ERROR(String description) {
        return (Header<T>)Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("ERROR")
                .description(description)
                .build();
    }
}

```

- 3개의 부분으로 메서드를 나눠서 작성하였는데 데이터없는 부분, 데이터와 같이 있는 부분, 에러가 난 부분으로 작성하였다.
- 이렇게 작성한 Header.java 클래스를 바탕으로 CRUD 인터페이스를 만들고 반환값을 Header로 작성하였다.

```java
package me.jangjangyi.study.ifs;

import me.jangjangyi.study.model.network.Header;

public interface CrudInterface<Req,Res> {
    Header<Res> create(Req request); //todo request Object 추가

    Header<Res> read(Long id);

    Header<Res> update(Req request);

    Header delete(Long id);
}
```

- 이렇게 인터페이스를 작성하면 나중에 Controller에서 CRUD를 작성할 때 인터페이스를 implements를 하면 CRUD에 관한 메서드를 빠질일이 없다. 그리고 CRUD인터페이스에 제네릭 타입으로 Req,Res를 추가하여 사용자가 요청한 데이터를 받아서 처리하여 Response로 보내도록 하기 위해서 작성하였다. UserApiController.java, UserApiRequest.java, UserApiResponse.java의 코드는 아래와 같다.

```java
package me.jangjangyi.study.controller.api;

import me.jangjangyi.study.ifs.CrudInterface;
import me.jangjangyi.study.model.network.Header;
import me.jangjangyi.study.model.network.request.UserApiRequest;
import me.jangjangyi.study.model.network.response.UserApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserApiController implements CrudInterface<UserApiRequest,UserApiResponse> {

    @Override
    @PostMapping("")
    public Header<UserApiResponse> create(@RequestBody UserApiRequest userApiRequest) {
        return null;
    }

    @Override
    @GetMapping("{id}") // /api/user/{id}
    public Header<UserApiResponse> read(@PathVariable(name = "id") Long id) {
        return null;
    }

    @Override
    @PutMapping("") // /api/user
    public Header<UserApiResponse> update(@RequestBody UserApiRequest userApiRequest) {
        return null;
    }

    @Override
    @DeleteMapping("{id}") // /api/user/{id}
    public Header delete(@PathVariable Long id) {
        return null;
    }
}

```

```java
package me.jangjangyi.study.model.network.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserApiRequest {

    private Long id;
    private String account;
    private String password;
    private String status;
    private String email;
    private String phoneNumber;
}

```

```java
package me.jangjangyi.study.model.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserApiResponse {

    private Long id;

    private String account;

    private String password;

    private String status;

    private String email;

    private String phoneNumber;

    private LocalDateTime registeredAt;

    private LocalDateTime unregisteredAt;
}

```

--------------

# Controller와 Service에 사용자 API CRUD 만들기

1. 사용자가 인터넷 상에서 요청하는 url주소와 요청 데이터를 요청하면 이러한 데이터를 Controller에서 UserApiRequest로 객체로 받아서 Service에 넘겨줍니다. 아래 코드와 같습니다.

```java
@Slf4j //log를 보여주는 애너테이션입니다.
@RestController
@RequestMapping("/api/user")
public class UserApiController implements CrudInterface<UserApiRequest,UserApiResponse>  {

    @Autowired
    private UserApiLogicService userApiLogicService;

    @Override
    @PostMapping("")
    public Header<UserApiResponse> create(@RequestBody Header<UserApiRequest> request) {
        log.info("{}", request);
        return userApiLogicService.create(request);
    }

    @Override
    @GetMapping("{id}") // /api/user/{id}
    public Header<UserApiResponse> read(@PathVariable(name = "id") Long id) {
        log.info("read id: {}",id);
        return userApiLogicService.read(id);
    }

    @Override
    @PutMapping("") // /api/user
    public Header<UserApiResponse> update(@RequestBody Header<UserApiRequest> userApiRequest) {
        return userApiLogicService.update(userApiRequest);
    }

    @Override
    @DeleteMapping("{id}") // /api/user/{id}
    public Header delete(@PathVariable Long id) {
        log.info("delete id: {} ",id);
        return userApiLogicService.delete(id);
    }
}
```

2. Controller에서 각 url과 데이터를 받으면 이것을 처리해주는 Service에 넘겨주게 됩니다. Controller에 맞게 Service객체도 CRUD를 구현해줍니다. **코드는 아래와 같습니다.** 

```java
@Service
public class UserApiLogicService implements CrudInterface<UserApiRequest, UserApiResponse> {

    @Autowired
    private UserRepository userRepository;
    //1. request data
    //2. user 생성
    //3. 생성된 데이터 -> UserApiResponse return
    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request) {
        //1. request Data
        UserApiRequest userApiRequest = request.getData();

        //2. User 생성
        User user = User.builder()
                .account(userApiRequest.getAccount())
                .password(userApiRequest.getPassword())
                .status("REGISTERED")
                .phoneNumber(userApiRequest.getPhoneNumber())
                .email(userApiRequest.getEmail())
                .registeredAt(LocalDateTime.now())
                .build();

        User newUser = userRepository.save(user);

        //3. 생성된 데이터 -> userApiResponse return
        return response(newUser);
    }

    @Override
    public Header<UserApiResponse> read(Long id) {
        //id -> repository getOne , getById
        //user -> userApiResponse return
        return userRepository.findById(id)
                .map(user -> response(user))
                .orElseGet(() -> Header.ERROR("데이터 없음"));

    }

    @Override
   public Header<UserApiResponse> update(Header<UserApiRequest> request) {
        //1. data
        UserApiRequest userApiRequest = request.getData();

        //2.id -> user 데이터를 찾고
        Optional<User> optionalUser = userRepository.findById(userApiRequest.getId());

        //3.update
        optionalUser.map(user ->
                user.setAccount(userApiRequest.getAccount())
                    .setPhoneNumber(userApiRequest.getPhoneNumber())
                    .setEmail(userApiRequest.getEmail())
                    .setStatus(userApiRequest.getStatus())
                    .setPassword(userApiRequest.getPassword())
                    .setRegisteredAt(userApiRequest.getRegisteredAt())
                    .setUnregisteredAt(userApiRequest.getUnregisteredAt()))
                .map(user -> userRepository.save(user)) //update가 일어남
                .map(updatedUser -> response(updatedUser))            //userApiResponse 생성
                .orElseGet(() -> Header.ERROR("데이터 없음"));

        //4.userApiResponse

        return null;
    }

    @Override
    public Header delete(Long id) {
        // id -> respository -> user
        Optional<User> optionalUser = userRepository.findById(id);

        // respository -> delete
        return optionalUser
                .map(user -> {
                    userRepository.delete(user);

                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터가 없습니다."));
        //response return
    }

    private Header<UserApiResponse> response(User user) {
        //user  ->  userApiResponse

        UserApiResponse userApiResponse = UserApiResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword()) //todo 암호화 , 길이
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .registeredAt(user.getRegisteredAt())
                .unregisteredAt(user.getUnregisteredAt())
                .status(user.getStatus())
                .build();

        // Header + data return

        return Header.OK(userApiResponse);
    }
}
```

---

### 프로젝트 변경사항

- @Autowired필드

  - 변경전

    ```java
    @RestController
    @RequestMapping("/api/item")
    public class ItemApiController implements CrudInterface<ItemApiRequest, ItemApiResponse> {
    
       @Autowired
       private ItemApiLogicService itemApiLogicService;
        ...
        ...
    
    ```

    

- @Autowired 생성자

  - 변경후

    ```java
    @RestController
    @RequestMapping("/api/item")
    @RequiredArgsConstructor
    public class ItemApiController implements CrudInterface<ItemApiRequest, ItemApiResponse> {
    
    private final ItemApiLogicService itemApiLogicService;
    
    ```

    - 생성자가 하나이면 @RequiredArgsConstructor로 자동적으로 생성자를 만들어 Bean을 주입해준다.
