package lk.ijse.gdse68.webposspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailDTO implements Serializable {
    private String itemId;
    private int quantity;
    private double price;
}

