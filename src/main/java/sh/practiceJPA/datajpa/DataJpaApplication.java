package sh.practiceJPA.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "sh.practiceJPA.datajpa.repository")// 원래는 이렇게 위치를 잡아줘야 하는데,
//스프링 부트를 쓰면 필요없다.
public class DataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataJpaApplication.class, args);
    }

}
