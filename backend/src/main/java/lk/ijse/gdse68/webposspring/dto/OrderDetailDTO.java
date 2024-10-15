package lk.ijse.gdse68.webposspring.dto;

import lombok.*;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class OrderDetailDTO implements Serializable {
    private String itemId;
    private int quantity;
    private double price;
}

