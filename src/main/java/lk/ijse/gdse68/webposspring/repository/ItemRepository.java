package lk.ijse.gdse68.webposspring.repository;

import lk.ijse.gdse68.webposspring.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, String> {
}
