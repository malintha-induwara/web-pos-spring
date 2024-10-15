package lk.ijse.gdse68.webposspring.service;

import lk.ijse.gdse68.webposspring.dto.OrderDTO;

public interface OrderService {
    void placeOrder(OrderDTO orderDTO);
    OrderDTO searchOrder(String orderId);
}
