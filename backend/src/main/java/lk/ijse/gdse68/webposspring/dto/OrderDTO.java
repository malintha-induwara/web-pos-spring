package lk.ijse.gdse68.webposspring.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO implements Serializable {
    private String orderId;
    private LocalDateTime orderTimeAndDate;
    private String customerId;
    private double subTotal;
    private double discount;
    private double amountPayed;
    private List<OrderDetailDTO> orderDetails;
}

