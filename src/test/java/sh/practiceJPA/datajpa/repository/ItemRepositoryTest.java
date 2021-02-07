package sh.practiceJPA.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import sh.practiceJPA.datajpa.entity.Item;
import sh.practiceJPA.datajpa.entity.Member;
import sh.practiceJPA.datajpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    @Test
    public void save() throws Exception {

        Item item = new Item("A");
        itemRepository.save(item);

    }



}
