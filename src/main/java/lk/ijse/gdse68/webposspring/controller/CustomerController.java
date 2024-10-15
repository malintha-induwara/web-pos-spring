package lk.ijse.gdse68.webposspring.controller;

import lk.ijse.gdse68.webposspring.dto.CustomerDTO;
import lk.ijse.gdse68.webposspring.exception.CustomerAlreadyExistsException;
import lk.ijse.gdse68.webposspring.exception.CustomerNotFoundException;
import lk.ijse.gdse68.webposspring.exception.DataPersistFailedException;
import lk.ijse.gdse68.webposspring.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        logger.info("Received request to save customer: {}", customerDTO);
        if (customerDTO == null) {
            logger.warn("Received null CustomerDTO");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            try {
                customerService.saveCustomer(customerDTO);
                logger.info("Customer saved successfully: {}", customerDTO.getCustomerId());
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } catch (CustomerAlreadyExistsException e) {
                logger.warn("Customer already exists: {}", customerDTO.getCustomerId());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } catch (DataPersistFailedException e) {
                logger.error("Failed to persist customer data: {}", customerDTO.getCustomerId(), e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } catch (Exception e) {
                logger.error("Unexpected error while saving customer: {}", customerDTO.getCustomerId(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @PutMapping(path = "/{customerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCustomer(@PathVariable("customerId") String customerId, @RequestBody CustomerDTO customerDTO) {
        logger.info("Received request to update customer: {}", customerId);
        if (customerId == null) {
            logger.warn("Received null customerId for update");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            try {
                customerService.updateCustomer(customerId, customerDTO);
                logger.info("Customer updated successfully: {}", customerId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } catch (CustomerNotFoundException e) {
                logger.warn("Customer not found for update: {}", customerId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (Exception e) {
                logger.error("Unexpected error while updating customer: {}", customerId, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @GetMapping(value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDTO> searchCustomer(@PathVariable("customerId") String customerId) {
        logger.info("Received request to search customer: {}", customerId);
        if (customerId == null) {
            logger.warn("Received null customerId for search");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            try {
                CustomerDTO customerDTO = customerService.searchCustomer(customerId);
                logger.info("Customer found: {}", customerId);
                return ResponseEntity.ok(customerDTO);
            } catch (CustomerNotFoundException e) {
                logger.warn("Customer not found: {}", customerId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (Exception e) {
                logger.error("Unexpected error while searching for customer: {}", customerId, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        logger.info("Received request to get all customers");
        try {
            List<CustomerDTO> allCustomers = customerService.getAllCustomers();
            logger.info("Retrieved {} customers", allCustomers.size());
            return ResponseEntity.ok(allCustomers);
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving all customers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(path = "/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") String customerId) {
        logger.info("Received request to delete customer: {}", customerId);
        if (customerId == null) {
            logger.warn("Received null customerId for deletion");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            try {
                customerService.deleteCustomer(customerId);
                logger.info("Customer deleted successfully: {}", customerId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } catch (CustomerNotFoundException e) {
                logger.warn("Customer not found for deletion: {}", customerId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (Exception e) {
                logger.error("Unexpected error while deleting customer: {}", customerId, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
}

