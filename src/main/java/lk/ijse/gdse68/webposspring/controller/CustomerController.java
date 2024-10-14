package lk.ijse.gdse68.webposspring.controller;

import lk.ijse.gdse68.webposspring.dto.CustomerDTO;
import lk.ijse.gdse68.webposspring.exception.CustomerAlreadyExistsException;
import lk.ijse.gdse68.webposspring.exception.CustomerNotFoundException;
import lk.ijse.gdse68.webposspring.exception.DataPersistFailedException;
import lk.ijse.gdse68.webposspring.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        if (customerDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            try {
                customerService.saveCustomer(customerDTO);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }catch (CustomerAlreadyExistsException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            catch (DataPersistFailedException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @PutMapping(path = "/{customerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCustomer(@PathVariable("customerId")String customerId, @RequestBody CustomerDTO customerDTO) {
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            try {
                customerService.updateCustomer(customerId, customerDTO);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } catch (CustomerNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @GetMapping(value ="/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDTO> searchCustomer(@PathVariable("customerId")String customerId) {
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            try {
                CustomerDTO customerDTO = customerService.searchCustomer(customerId);
                return ResponseEntity.ok(customerDTO);
            } catch (CustomerNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @GetMapping( value = "/allCustomers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        try {
            List<CustomerDTO> allCustomers = customerService.getAllCustomers();
            return ResponseEntity.ok(allCustomers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @DeleteMapping(path = "/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId")String customerId) {
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            try {
                customerService.deleteCustomer(customerId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } catch (CustomerNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }



}

