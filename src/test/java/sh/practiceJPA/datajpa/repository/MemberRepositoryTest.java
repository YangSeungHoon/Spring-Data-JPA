package sh.practiceJPA.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import sh.practiceJPA.datajpa.dto.MemberDto;
import sh.practiceJPA.datajpa.entity.Member;
import sh.practiceJPA.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

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

}