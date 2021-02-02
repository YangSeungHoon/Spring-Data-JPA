package sh.practiceJPA.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sh.practiceJPA.datajpa.dto.MemberDto;
import sh.practiceJPA.datajpa.entity.Member;

import java.util.List;

//JpaRepository<Entity,Entity에 맵핑된 pk>
public interface MemberRepository  extends JpaRepository<Member,Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username,int age);

    //실무에서 많이 씀
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //dto로 반환하기.
    @Query("select new sh.practiceJPA.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();
}
