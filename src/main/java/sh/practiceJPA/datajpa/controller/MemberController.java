package sh.practiceJPA.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sh.practiceJPA.datajpa.entity.Member;
import sh.practiceJPA.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}") //기본키인 id를 이용해서..
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    //이렇게 페이지 인터페이스만 파라미터로 받게하면 알아서 페이징 처리해준다.
    //8080/members?page=0 => 0페이지에서 id를 20개 꺼낸다.
    //8080/members?page=0&size=3 => 0페이지에서 3개만 꺼낸다.
    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    @GetMapping("/members2/{id}") //도메인 컨버터가 객체를 @PathVariable로 받아와도 알아서 해결해준다.
    public String findMember(@PathVariable("id") Member member){
        return member.getUsername();
    }

//    @PostConstruct
//    public void init() {
//        memberRepository.save(new Member("userA"));
//    }

    @PostConstruct//page 실험용 데이터터
   public void init() {
        for(int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user"+ i,i));
        }
    }
}
