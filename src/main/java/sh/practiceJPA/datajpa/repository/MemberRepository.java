package sh.practiceJPA.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import sh.practiceJPA.datajpa.dto.MemberDto;
import sh.practiceJPA.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username); //단건
    Optional<Member> findOptionalByUsername(String username); //단건 Optional

    //Page<Member> findByAge(int age, Pageable pageable);

    //Slice<Member> findByAge(int age, Pageable pageable);

    //Count Query 분리하기
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);


    @Modifying(clearAutomatically = true) //clearAutomatically속성을 정해주면 아래 벌크연산이후 자동으로 em.clear()가 실행된다.
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();


    @Override
    @EntityGraph(attributePaths = {"team"}) //이걸 쓰면 마치 fetch join 한 것과 같은 효과를 낸다.
    List<Member> findAll();

    //이렇게 jpql을 작성해놓고 fetch join의 효과를 노려볼 수도 있다.
    @EntityGraph(attributePaths = {"team"})
    @Query("select  m from Member m")
    List<Member> findMemberEntityGraph();

    //@EntityGraph("Member.all") // Member Entity 쪽에다가 Member.all이라는 이름을 정해주고
    // 그 다음 attributeNodes = @NamedAttributeNode("team") 이런 속성을 한 후에 이 어노테이션을 사용할 수 있다. 아래 @EntityGraph와 같은
    //효과를 지닌다.
    //회원 조회할때 보통 team데이터도 같이 쓴다면 그냥 아래처럼 해놓고 쓰면 회원조회하면서 team데이터도 fetch join으로 같이 가져온다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    //수정할 것이 아니라면, 이렇게 오로지 read만 하겠다라고 설정을 해서 최적화가 가능하다.
    //이러한 설정이 없다면 수정할 수 있다는것으로 간주하여 자동으로 snapshot을 찍는 행위를 하는데, 이것이 최적화에 걸림돌이 될 수 있다.
    @QueryHints(value = @QueryHint( name ="org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
