package lk.ijse.gdse68.webposspring.service.impl;

import lk.ijse.gdse68.webposspring.dto.OrderDTO;
import lk.ijse.gdse68.webposspring.dto.OrderDetailDTO;
import lk.ijse.gdse68.webposspring.entity.*;
import lk.ijse.gdse68.webposspring.exception.CustomerNotFoundException;
import lk.ijse.gdse68.webposspring.exception.DataPersistFailedException;
import lk.ijse.gdse68.webposspring.exception.ItemNotFoundException;
import lk.ijse.gdse68.webposspring.repository.CustomerRepository;
import lk.ijse.gdse68.webposspring.repository.ItemRepository;
import lk.ijse.gdse68.webposspring.repository.OrderRepository;
import lk.ijse.gdse68.webposspring.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Transactional
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final ItemRepository itemRepository;


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
        orders.setSubTotal(orderDTO.getSubtotal());
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
        return null;
    }
}

