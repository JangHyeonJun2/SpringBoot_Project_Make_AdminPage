# SpringBoot_Project_Make_AdminPage
# 상품 관련 Admin페이지 제작 프로젝트입니다.
# 첫번째 문제점

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
