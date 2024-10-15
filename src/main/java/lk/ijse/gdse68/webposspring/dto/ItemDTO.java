package lk.ijse.gdse68.webposspring.dto;


import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemDTO<T> implements Serializable {
    private String itemId;
    private String itemName;
    private double price;
    private int quantity;
    private String category;
    private T image;
}

