package sh.practiceJPA.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sh.practiceJPA.datajpa.entity.Member;

import java.util.List;

//JpaRepository<Entity,Entity에 맵핑된 pk>
public interface MemberRepository  extends JpaRepository<Member,Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username,int age);

}
