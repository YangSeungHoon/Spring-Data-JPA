package sh.practiceJPA.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import sh.practiceJPA.datajpa.dto.MemberDto;
import sh.practiceJPA.datajpa.entity.Member;
import sh.practiceJPA.datajpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberQueryRepository memberQueryRepository;

    @Autowired
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);


        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA",15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA",10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameListTest() {
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUsernameList();
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }


    @Test
    public void findMemberDtoTest() {

        Team team = new Team("temaA");
        teamRepository.save(team);

        Member m1 = new Member("AAA",10);
        memberRepository.save(m1);
        m1.setTeam(team);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void findByNamesTest() {
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB")); //AAA와 BBB를 in 절로 가져온다.

        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnTypeTest() {
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("BBB",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        //컬렉션에서 만약에 없는 것을 찾으려고하면 null을 반환하는 것이 아니라, 사이즈가 0인 컬렉션을 반화한다.(empty 컬렉션 반환)
        List<Member> result = memberRepository.findListByUsername("AAA");

        //이렇게 단건 조회에서 없는 값을 찾으려고하면 결과가 null을 반환한다.
        Member findMember = memberRepository.findMemberByUsername("AAA");
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAA");
    }

    @Test
    public void paging() {
        memberRepository.save(new Member("memebr1",10));
        memberRepository.save(new Member("memebr2",10));
        memberRepository.save(new Member("memebr3",10));
        memberRepository.save(new Member("memebr4",10));
        memberRepository.save(new Member("memebr5",10));

        int age=  10;

        //0페이지에서 3개 가져오는데 username기준으로 내림차순 하겠다.
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //반환타입이 Page이면 total Page까지 알아서 가져온다.
        Page<Member> page = memberRepository.findByAge(age,pageRequest);

        List<Member> content = page.getContent();

        //그대로 반환하면 안된다. 이렇게 map으로해서 MemberDto로 바꿔서 반환해 줘야한다.
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5); //전체 수
        assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2);// 전체 페이지개수
        assertThat(page.isFirst()).isTrue(); //첫 번째 페이지인지 확인
        assertThat(page.hasNext()).isTrue(); //다음 페이지가 존재하는지 확인인
    }


//    //slice는 totalCount가 안날라간다.
//    @Test
//    public void slicePaging() {
//        memberRepository.save(new Member("memebr1",10));
//        memberRepository.save(new Member("memebr2",10));
//        memberRepository.save(new Member("memebr3",10));
//        memberRepository.save(new Member("memebr4",10));
//        memberRepository.save(new Member("memebr5",10));
//
//        int age=  10;
//
//        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
//
//        Slice<Member> page = memberRepository.findByAge(age,pageRequest);
//
//        List<Member> content = page.getContent();
//
//        assertThat(content.size()).isEqualTo(3);
//        assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
//        assertThat(page.isFirst()).isTrue(); //첫 번째 페이지인지 확인
//        assertThat(page.hasNext()).isTrue(); //다음 페이지가 존재하는지 확인인
//    }

    @Test
    public void bulkUpdate() {

        memberRepository.save(new Member("memebr1",10));
        memberRepository.save(new Member("memebr2",11));
        memberRepository.save(new Member("memebr3",12));
        memberRepository.save(new Member("memebr4",20));
        memberRepository.save(new Member("memebr5",21));
        memberRepository.save(new Member("memebr5",40));

        int resultCount = memberRepository.bulkAgePlus(20);
        //em.flush() 물론 위에 벌크연산이 jpql로 이루어져 있어서 flush는 자연스럽게 실행된다.
        //em.clear()// 어쨋거나 영속성 컨텍스트는 초기화를 시켜줘야해서 clear()가 필요하다. 근데 이것을 여기다 안하고, Repository쪽에서
        //설정이 가능하다.

        //이렇게만 실행하면 age가 바뀌지 않는다.
        //왜냐하면 이것은 벌크 연산때문인데, JPA에서는 영속성 컨텍스트가 존재하는데 이 벌크 연산을 실행하면 영속성 컨텍스트를 무시하고
        //그대로 DB에 쿼리를 날려버린다. 그러니까 아직 영속성 컨텍스트는 초기화가 되지 못하고, member5의 나이가 40인 것이다.
        //물론 이 때, DB에 member5의 값은 41로 업데이트 되어있다.
        //그래서 위의 상황을 막기위해 벌크연산 이후에는 무조건 em.flush()를 하고, em.clear()를 해주는것이 안전하다.
        //물론 벌크 연산 이후에 추가적인 조작이 없다면 상관없긴함.
        List<Member> result = memberRepository.findListByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",10,teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //List<Member> members =memberRepository.findAll();
        //List<Member> members = memberRepository.findMemberFetchJoin();

        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void QueryHint() throws Exception {

        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when

        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();
        // then

    }

    @Test
    public void lock() throws Exception {

        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();


        List<Member> result = memberRepository.findLockByUsername("member1");

    }

    @Test
    public void callCustom() throws Exception {
        List<Member> result = memberRepository.findMemberCustom();
    }

    //QueryByExample
    //동적 쿼리를 편리하게 처리하고 도메인 객체를 그대로 사용한다.
    //그래서 데이터 저장소를 RDB에서 NoSQL로 변경해도 코드 변경이 없게 추상화 되어 있음.
    // 스프링 데이터 JPA JpaRepository 인터페이스에 이미 포함되되어있음.

    // 그러나 명확하게 단점이 존재함.
    //일단, 조인은 가능하지만 내부조인(INNER JOIN)만 가능하고 외부조인(LEFT JOIN)은 안됨.
    //그리고 중첩 제약조건이 불가능하고 매칭 조건이 너무 단순함.

    //결과적으로 그냥 QueryDSL을 사용하는 것이 해결책임.
    @Test
    public void queryByExample() throws Exception {

        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1",0,teamA);
        Member m2 = new Member("m2",0,teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        Member member= new Member("m1");
        Team team = new Team("teamA"); //내부조인으로 teamA 가능
        member.setTeam(team);

        //ExampleMatcher 생성, age프로퍼티는 무시
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");

        Example<Member> example = Example.of(member,matcher);

        List<Member> result = memberRepository.findAll(example);

        assertThat(result.get(0).getUsername()).isEqualTo("m1");

    }

    //원하는 데이터만 딱 가져올 때 사용.
    @Test
    public void projectionsTest() throws Exception {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1",0,teamA);
        Member m2 = new Member("m2",0,teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        List<NestedClosedProjections> result = memberRepository.findProjectionsByUsername("m1",NestedClosedProjections.class);

        for (NestedClosedProjections NestedClosedProjections : result) {
            System.out.println("UsernameOnlyDto = " + NestedClosedProjections);
        }

        // then

    }

    @Test
    public void nativeQueryTest() throws Exception {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1",0,teamA);
        Member m2 = new Member("m2",0,teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when

       Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0,10));
       List<MemberProjection> content = result.getContent();

        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection.getUsername() = " + memberProjection.getUsername());
            System.out.println("memberProjection.getTeamName() = " + memberProjection.getTeamName());

        }
    }

}