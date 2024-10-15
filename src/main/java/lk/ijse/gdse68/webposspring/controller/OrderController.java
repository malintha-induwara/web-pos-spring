package lk.ijse.gdse68.webposspring.controller;

import lk.ijse.gdse68.webposspring.dto.OrderDTO;
import lk.ijse.gdse68.webposspring.exception.DataPersistFailedException;
import lk.ijse.gdse68.webposspring.exception.OrderNotFoundException;
import lk.ijse.gdse68.webposspring.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@CrossOrigin("*")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveOrder(@RequestBody OrderDTO orderDTO) {
        logger.info("Received request to save order");
        try {
            if (orderDTO == null) {
                logger.warn("Received null OrderDTO");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                orderService.placeOrder(orderDTO);
                logger.info("Order placed successfully: {}", orderDTO.getOrderId());
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
        } catch (DataPersistFailedException e) {
            logger.error("Failed to persist order data", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Unexpected error while saving order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> getOrderId() {
        logger.info("Received request to get order ID");
        try {
            Map<String, String> orderId = orderService.getOrderId();
            logger.info("Order ID retrieved successfully");
            return ResponseEntity.status(HttpStatus.OK).body(orderId);
        } catch (Exception e) {
            logger.error("Unexpected error while getting order ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(path = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> searchOrder(@PathVariable("orderId") String orderId) {
        logger.info("Received request to search order: {}", orderId);
        try {
            OrderDTO orderDTO = orderService.searchOrder(orderId);
            logger.info("Order found: {}", orderId);
            return ResponseEntity.ok(orderDTO);
        } catch (OrderNotFoundException e) {
            logger.warn("Order not found: {}", orderId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Unexpected error while searching for order: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(path = "/allOrders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        logger.info("Received request to get all orders");
        try {
            List<OrderDTO> allOrders = orderService.getAllOrders();
            logger.info("Retrieved {} orders", allOrders.size());
            return ResponseEntity.ok(allOrders);
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving all orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}