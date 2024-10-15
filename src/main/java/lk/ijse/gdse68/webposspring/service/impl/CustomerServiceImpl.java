package lk.ijse.gdse68.webposspring.service.impl;

import lk.ijse.gdse68.webposspring.dto.CustomerDTO;
import lk.ijse.gdse68.webposspring.entity.Customer;
import lk.ijse.gdse68.webposspring.exception.CustomerAlreadyExistsException;
import lk.ijse.gdse68.webposspring.exception.CustomerNotFoundException;
import lk.ijse.gdse68.webposspring.exception.DataPersistFailedException;
import lk.ijse.gdse68.webposspring.repository.CustomerRepository;
import lk.ijse.gdse68.webposspring.service.CustomerService;
import lk.ijse.gdse68.webposspring.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final Mapping mapping;

    @Override
    public void saveCustomer(CustomerDTO customerDTO) {

        Optional<Customer> tempCustomer = customerRepository.findById(customerDTO.getCustomerId());
        if (tempCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already exists");
        } else {
            try {
                customerRepository.save(mapping.convertToCustomerEntity(customerDTO));
            } catch (Exception e) {
                throw new DataPersistFailedException("Failed to save the customer");
            }
        }
    }

    @Override
    public void updateCustomer(String customerId, CustomerDTO customerDTO) {
        Optional<Customer> tempCustomer = customerRepository.findById(customerId);
        if (tempCustomer.isPresent()) {
            tempCustomer.get().setFirstName(customerDTO.getFirstName());
            tempCustomer.get().setLastName(customerDTO.getLastName());
            tempCustomer.get().setAddress(customerDTO.getAddress());
            tempCustomer.get().setMobile(customerDTO.getMobile());
            tempCustomer.get().setDob(customerDTO.getDob());
        } else {
            throw new CustomerNotFoundException("Customer not found");
        }
    }


    @Override
    public void deleteCustomer(String customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            customerRepository.deleteById(customerId);
        } else {
            throw new CustomerNotFoundException("Customer not found");
        }
    }

    @Override
    public CustomerDTO searchCustomer(String customerId) {
        if (customerRepository.existsById(customerId)) {
            return mapping.convertToCustomerDTO(customerRepository.getReferenceById(customerId));
        } else {
            throw new CustomerNotFoundException("Customer not found");
        }
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return mapping.convertToCustomerDTOList(customers);
    }
}

