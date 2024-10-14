package lk.ijse.gdse68.webposspring.util;

import lk.ijse.gdse68.webposspring.dto.CustomerDTO;
import lk.ijse.gdse68.webposspring.entity.Customer;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapping {

    private ModelMapper modelMapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Customer convertToCustomerEntity(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }

    public CustomerDTO convertToCustomerDTO(Customer ent) {
        return modelMapper.map(ent, CustomerDTO.class);
    }

    public List<CustomerDTO> convertToCustomerDTOList(List<Customer> customers) {
        return modelMapper.map(customers, new TypeToken<List<CustomerDTO>>() {
        }.getType());
    }
}

