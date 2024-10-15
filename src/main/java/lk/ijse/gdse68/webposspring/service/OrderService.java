package lk.ijse.gdse68.webposspring.service;

import lk.ijse.gdse68.webposspring.dto.OrderDTO;

import java.util.List;
import java.util.Map;

public interface OrderService {

    void placeOrder(OrderDTO orderDTO);

    OrderDTO searchOrder(String orderId);

    Map<String, String> getOrderId();

    List<OrderDTO> getAllOrders();
}
