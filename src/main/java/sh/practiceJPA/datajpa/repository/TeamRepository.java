package sh.practiceJPA.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sh.practiceJPA.datajpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
