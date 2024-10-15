package lk.ijse.gdse68.webposspring.service;

import lk.ijse.gdse68.webposspring.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {

    void saveCustomer(CustomerDTO customerDTO);

    void updateCustomer(String customerId, CustomerDTO customerDTO);

    void deleteCustomer(String customerId);

    CustomerDTO searchCustomer(String customerId);

    List<CustomerDTO> getAllCustomers();
}
