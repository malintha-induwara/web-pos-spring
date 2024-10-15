package lk.ijse.gdse68.webposspring.controller;

import lk.ijse.gdse68.webposspring.dto.OrderDTO;
import lk.ijse.gdse68.webposspring.exception.DataPersistFailedException;
import lk.ijse.gdse68.webposspring.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveOrder(@RequestBody OrderDTO orderDTO) {
        try {
            if (orderDTO == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                orderService.placeOrder(orderDTO);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
        } catch (DataPersistFailedException e) {
            System.out.println("error is here");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

