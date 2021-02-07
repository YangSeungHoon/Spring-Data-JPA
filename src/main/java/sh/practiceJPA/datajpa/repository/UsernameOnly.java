package sh.practiceJPA.datajpa.repository;


import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    //다음과 같이 설정하여 Open Projection을 하면 먼저 해당 객체를 가져오고 그다음 @Value어노테이션의 조건을 맞춰서 다시 원하는 데이터를
    //찍어서 계산한다.
    @Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
