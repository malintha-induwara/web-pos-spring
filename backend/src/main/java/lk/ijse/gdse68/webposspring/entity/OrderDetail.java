package lk.ijse.gdse68.webposspring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "orderDetail")
public class OrderDetail {
    @EmbeddedId
    private OrderDetailsId orderDetailsId;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "orders_Id")
    private Orders orders;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_Id")
    private Item items;

    private int quantity;
    private double unitPrice;

}

