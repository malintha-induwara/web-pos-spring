package lk.ijse.gdse68.webposspring.controller;


import lk.ijse.gdse68.webposspring.dto.ItemDTO;
import lk.ijse.gdse68.webposspring.exception.DataPersistFailedException;
import lk.ijse.gdse68.webposspring.exception.ItemAlreadyExistsException;
import lk.ijse.gdse68.webposspring.exception.ItemNotFoundException;
import lk.ijse.gdse68.webposspring.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveItem(
            @RequestPart("itemId") String item,
            @RequestPart("itemName") String itemName,
            @RequestPart("price") String price,
            @RequestPart("quantity") String quantity,
            @RequestPart("category") String category,
            @RequestPart("image") MultipartFile image
    ) {
        logger.info("Received request to save item: {}", item);
        try {
            ItemDTO<MultipartFile> itemDTO = new ItemDTO<>(item, itemName, Double.parseDouble(price), Integer.parseInt(quantity), category, image);
            itemService.saveItem(itemDTO);
            logger.info("Item saved successfully: {}", item);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ItemAlreadyExistsException e) {
            logger.warn("Item already exists: {}", item);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (DataPersistFailedException e) {
            logger.error("Failed to persist item data: {}", item, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Unexpected error while saving item: {}", item, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(path = "/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateItem(@PathVariable("itemId") String itemId,
                                           @RequestPart("itemName") String itemName,
                                           @RequestPart("price") String price,
                                           @RequestPart("quantity") String quantity,
                                           @RequestPart("category") String category,
                                           @RequestPart("image") MultipartFile image
    ) {
        logger.info("Received request to update item: {}", itemId);
        try {
            ItemDTO<MultipartFile> itemDTO = new ItemDTO<>(itemId, itemName, Double.parseDouble(price), Integer.parseInt(quantity), category, image);
            itemService.updateItem(itemId, itemDTO);
            logger.info("Item updated successfully: {}", itemId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ItemNotFoundException e) {
            logger.warn("Item not found for update : {}", itemId);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (DataPersistFailedException e) {
            logger.error("Failed to persist updated item data: {}", itemId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Unexpected error while updating item: {}", itemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(path = "/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable("itemId") String itemId) {
        logger.info("Received request to delete item: {}", itemId);
        if (itemId == null) {
            logger.warn("Received null itemId for deletion");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            try {
                itemService.deleteItem(itemId);
                logger.info("Item deleted successfully: {}", itemId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } catch (ItemNotFoundException e) {
                logger.warn("Item not found for deletion: {}", itemId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (Exception e) {
                logger.error("Unexpected error while deleting item: {}", itemId, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @GetMapping(path = "/{itemId}")
    public ResponseEntity<ItemDTO<String>> searchItem(@PathVariable("itemId") String itemId) {
        logger.info("Received request to search item: {}", itemId);
        try {
            ItemDTO<String> itemDTO = itemService.searchItem(itemId);
            logger.info("Item found: {}", itemId);
            return ResponseEntity.ok(itemDTO);
        } catch (ItemNotFoundException e) {
            logger.warn("Item not found: {}", itemId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Unexpected error while searching for item: {}", itemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDTO<String>>> getAllItems() {
        logger.info("Received request to get all items");
        try {
            List<ItemDTO<String>> items = itemService.getAllItems();
            logger.info("Retrieved {} items", items.size());
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving all items", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

