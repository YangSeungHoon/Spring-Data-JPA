package sh.practiceJPA.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sh.practiceJPA.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;


//앞서 했던 custom으로 해당 Repository를 확장시키기 보다는 그냥 이렇게 별개의 Repository를 따로 만들자.
@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager em;

    List<Member> findAllMembers() {
        return em.createQuery("Select m from Member m")
                .getResultList();
    }
}
