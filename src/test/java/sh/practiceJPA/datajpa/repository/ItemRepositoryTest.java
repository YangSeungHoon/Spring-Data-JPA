package sh.practiceJPA.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sh.practiceJPA.datajpa.entity.Item;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void save() throws Exception {

        Item item = new Item("A");
        itemRepository.save(item);

    }

}
