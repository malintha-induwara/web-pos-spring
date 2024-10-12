package lk.ijse.gdse68.webposspring.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "orders")
@Entity
public class Orders {
    @Id
    private String orderId;
    private LocalDateTime localDateTime;
    private double subTotal;
    private double discount;
    private double amountPayed;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @OneToMany(mappedBy = "orders")
    private Set<OrderDetail> orderDetails;
}

