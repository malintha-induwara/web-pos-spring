package lk.ijse.gdse68.webposspring.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Item {
    @Id
    private String itemId;
    private String itemName;
    private double price;
    private int quantity;
    private String category;
    private String image;

    @OneToMany(mappedBy = "items")
    private Set<OrderDetail> orderDetails;
}

