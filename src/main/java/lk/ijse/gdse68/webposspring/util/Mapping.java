package lk.ijse.gdse68.webposspring.util;

import lk.ijse.gdse68.webposspring.dto.CustomerDTO;
import lk.ijse.gdse68.webposspring.dto.ItemDTO;
import lk.ijse.gdse68.webposspring.entity.Customer;
import lk.ijse.gdse68.webposspring.entity.Item;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class Mapping {

    private ModelMapper modelMapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        modelMapper.typeMap(Item.class, ItemDTO.class).addMappings(mapper -> mapper.skip(ItemDTO<String>::setImage));
        modelMapper.typeMap(ItemDTO.class, Item.class).addMappings(mapper -> mapper.skip(Item::setImage));
    }

    public Customer convertToCustomerEntity(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }

    public CustomerDTO convertToCustomerDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

    public List<CustomerDTO> convertToCustomerDTOList(List<Customer> customers) {
        return modelMapper.map(customers, new TypeToken<List<CustomerDTO>>() {
        }.getType());
    }

    public ItemDTO<String> convertToItemDTO(Item item) {
        return modelMapper.map(item, new TypeToken<ItemDTO<String>>(){}.getType());
    }

    public List<ItemDTO<String>> convertToItemDTOList(List<Item> items) {
        return modelMapper.map(items, new TypeToken<List<ItemDTO<String>>>() {}.getType());
    }

    public Item convertToItemEntity(ItemDTO<MultipartFile> itemDTO) {
        return modelMapper.map(itemDTO, Item.class);
    }
}

