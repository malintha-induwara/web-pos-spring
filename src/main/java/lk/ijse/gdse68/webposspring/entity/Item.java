package lk.ijse.gdse68.webposspring.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Item {
    @Id
    private String itemId;
    private String itemName;
    private double price;
    private int quantity;
    private String category;
    private String imgName;

    @OneToMany(mappedBy = "items")
    private Set<OrderDetail> orderDetails;
}

