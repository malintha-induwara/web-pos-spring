package lk.ijse.gdse68.webposspring.repository;

import lk.ijse.gdse68.webposspring.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository  extends JpaRepository<Orders, String> {
    Optional<Orders> findTopByOrderByOrderIdDesc();
}
