package lk.ijse.gdse68.webposspring.repository;

import lk.ijse.gdse68.webposspring.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
}
