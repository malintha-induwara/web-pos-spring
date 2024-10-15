package lk.ijse.gdse68.webposspring.service.impl;

import lk.ijse.gdse68.webposspring.dto.OrderDTO;
import lk.ijse.gdse68.webposspring.dto.OrderDetailDTO;
import lk.ijse.gdse68.webposspring.entity.*;
import lk.ijse.gdse68.webposspring.exception.CustomerNotFoundException;
import lk.ijse.gdse68.webposspring.exception.DataPersistFailedException;
import lk.ijse.gdse68.webposspring.exception.ItemNotFoundException;
import lk.ijse.gdse68.webposspring.exception.OrderNotFoundException;
import lk.ijse.gdse68.webposspring.repository.CustomerRepository;
import lk.ijse.gdse68.webposspring.repository.ItemRepository;
import lk.ijse.gdse68.webposspring.repository.OrderRepository;
import lk.ijse.gdse68.webposspring.service.OrderService;
import lk.ijse.gdse68.webposspring.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Transactional
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final ItemRepository itemRepository;

    private final Mapping mapping;

    @Override
    public void placeOrder(OrderDTO orderDTO) {
        Optional<Customer> customer = customerRepository.findById(orderDTO.getCustomerId());

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException("Customer not found");
        }

        //Create New Order
        Orders newOrder = createOrder(orderDTO, customer.get());

        //Update items and convert Order DTOs to Entities
        Set<OrderDetail> orderDetails = saveOrderDetailsAndUpdateItems(newOrder, orderDTO.getOrderDetails());
        newOrder.setOrderDetails(orderDetails);

        //Save Order
        saveOrder(newOrder);
    }

    private Orders createOrder(OrderDTO orderDTO, Customer customer) {
        Orders orders = new Orders();
        orders.setOrderId(orderDTO.getOrderId());
        orders.setOrderTimeAndDate(LocalDateTime.now());
        orders.setCustomer(customer);
        orders.setAmountPayed(orderDTO.getAmountPayed());
        orders.setSubTotal(orderDTO.getSubTotal());
        orders.setDiscount(orderDTO.getDiscount());
        return orders;
    }


    private void saveOrder(Orders orders) {
        try {
            orderRepository.save(orders);
        } catch (Exception e) {
            throw new DataPersistFailedException("Failed to save the order");
        }
    }

    private Set<OrderDetail> saveOrderDetailsAndUpdateItems(Orders orders, List<OrderDetailDTO> orderDetailDTOList) {
        List<OrderDetail> orderDetailList= orderDetailDTOList.stream().map(orderDetailDto -> {
            Optional<Item> item = itemRepository.findById(orderDetailDto.getItemId());

            if (item.isEmpty()) {
                throw new ItemNotFoundException("Item not found");
            }

            //Update the quantity
            item.get().setQuantity(item.get().getQuantity() - orderDetailDto.getQuantity());

            OrderDetailsId orderDetailsId = new OrderDetailsId(orders.getOrderId(), item.get().getItemId());
            return new OrderDetail(orderDetailsId, orders, item.get(), orderDetailDto.getQuantity(), orderDetailDto.getPrice());
        }).toList();
        return new HashSet<>(orderDetailList);
    }


    @Override
    public OrderDTO searchOrder(String orderId) {
        if (orderRepository.existsById(orderId)){
            return mapping.convertToOrderDTO(orderRepository.findById(orderId));
        }else {
            throw  new OrderNotFoundException("Order Not Found");
        }
    }

    @Override
    public Map<String,String> getOrderId() {
        Optional<Orders> lastOrder = orderRepository.findTopByOrderByOrderIdDesc();
        Map<String,String>  orderId = new HashMap<>();
        if (lastOrder.isPresent()){
            String lastOrderId = lastOrder.get().getOrderId();
            String prefix = lastOrderId.substring(0, 1);
            int number = Integer.parseInt(lastOrderId.substring(1));
            number++;
            String formattedNumber = String.format("%03d", number);
            orderId.put("orderId",prefix + formattedNumber);
            return orderId;
        }
        orderId.put("orderId","O001");
        return orderId;
    }
}

