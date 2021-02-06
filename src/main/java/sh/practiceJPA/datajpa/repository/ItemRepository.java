package sh.practiceJPA.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sh.practiceJPA.datajpa.entity.Item;

public interface ItemRepository extends JpaRepository<Item,Long> {
}
