package lk.ijse.gdse68.webposspring.service.impl;

import lk.ijse.gdse68.webposspring.dto.ItemDTO;
import lk.ijse.gdse68.webposspring.entity.Item;
import lk.ijse.gdse68.webposspring.exception.DataPersistFailedException;
import lk.ijse.gdse68.webposspring.exception.ItemAlreadyExistsException;
import lk.ijse.gdse68.webposspring.exception.ItemNotFoundException;
import lk.ijse.gdse68.webposspring.repository.ItemRepository;
import lk.ijse.gdse68.webposspring.service.ItemService;
import lk.ijse.gdse68.webposspring.util.ImageUtil;
import lk.ijse.gdse68.webposspring.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final Mapping mapping;

    private final ImageUtil imageUtil;

    @Override
    public void saveItem(ItemDTO<MultipartFile> itemDTO) {
        Optional<Item> tempItem = itemRepository.findById(itemDTO.getItemId());
        if (tempItem.isPresent()) {
            throw new ItemAlreadyExistsException("Item already exists");
        } else {
            try {
                String imageName = imageUtil.saveImage(itemDTO.getImage());
                Item item = mapping.convertToItemEntity(itemDTO);
                item.setImage(imageName);
                itemRepository.save(item);
            } catch (Exception e) {
                throw new DataPersistFailedException("Failed to save the item");
            }
        }
    }

    @Override
    public void updateItem(String itemId, ItemDTO<MultipartFile> itemDTO) {
        Optional<Item> tempItem = itemRepository.findById(itemId);
        if (tempItem.isPresent()) {
            String imageName = tempItem.get().getImage();
            if (!itemDTO.getImage().isEmpty()) {
                imageName = imageUtil.updateImage(tempItem.get().getImage(), itemDTO.getImage());
            }
            tempItem.get().setItemName(itemDTO.getItemName());
            tempItem.get().setPrice(itemDTO.getPrice());
            tempItem.get().setQuantity(itemDTO.getQuantity());
            tempItem.get().setCategory(itemDTO.getCategory());
            tempItem.get().setImage(imageName);
        } else {
            throw new ItemNotFoundException("Item not found");
        }
    }

    @Override
    public void deleteItem(String itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            imageUtil.deleteImage(item.get().getImage());
            itemRepository.deleteById(itemId);
        } else {
            throw new ItemNotFoundException("Item not found");
        }

    }

    @Override
    public ItemDTO<String> searchItem(String itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            ItemDTO<String> itemDTO = mapping.convertToItemDTO(item.get());
            itemDTO.setImage(imageUtil.getImage(item.get().getImage()));
            return itemDTO;
        } else {
            throw new ItemNotFoundException("Item not found");
        }
    }

    @Override
    public List<ItemDTO<String>> getAllItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemDTO<String>> itemDTOS = mapping.convertToItemDTOList(items);
        for (ItemDTO<String> itemDTO : itemDTOS) {
            items.stream().filter(item ->
                            item.getItemId().equals(itemDTO.getItemId()))
                    .findFirst()
                    .ifPresent(item ->
                            itemDTO.setImage(imageUtil.getImage(item.getImage())));
        }
        return itemDTOS;
    }
}

