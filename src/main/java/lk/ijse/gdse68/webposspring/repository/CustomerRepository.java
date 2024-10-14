package lk.ijse.gdse68.webposspring.repository;

import lk.ijse.gdse68.webposspring.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,String> {

}
