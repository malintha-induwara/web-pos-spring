package lk.ijse.gdse68.webposspring.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO implements Serializable {
    private String orderId;
    private LocalDateTime orderTimeAndDate ;
    private String customerId;
    private double subTotal;
    private double discount;
    private double amountPayed;
    private List<OrderDetailDTO> orderDetails;
}

