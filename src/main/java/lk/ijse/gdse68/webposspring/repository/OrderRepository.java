package lk.ijse.gdse68.webposspring.repository;

import lk.ijse.gdse68.webposspring.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository  extends JpaRepository<Orders, String> {
}
