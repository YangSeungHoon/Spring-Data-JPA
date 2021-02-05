package sh.practiceJPA.datajpa.repository;

import sh.practiceJPA.datajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
