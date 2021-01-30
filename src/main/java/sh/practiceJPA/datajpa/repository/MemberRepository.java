package sh.practiceJPA.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sh.practiceJPA.datajpa.entity.Member;

public interface MemberRepository  extends JpaRepository<Member,Long> {
}
