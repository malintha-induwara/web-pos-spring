package lk.ijse.gdse68.webposspring.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "customer")
@Entity
public class Customer {
    @Id
    private String customerId;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String address;
    private String mobile;

    @OneToMany(mappedBy = "customer")
    private List<Orders> orders;

}

