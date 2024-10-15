package lk.ijse.gdse68.webposspring.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class OrderDetailsId implements Serializable {
    private String orderId;
    private String itemId;
}

