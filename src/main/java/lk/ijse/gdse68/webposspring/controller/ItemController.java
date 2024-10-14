package lk.ijse.gdse68.webposspring.controller;


import lk.ijse.gdse68.webposspring.dto.ItemDTO;
import lk.ijse.gdse68.webposspring.exception.DataPersistFailedException;
import lk.ijse.gdse68.webposspring.exception.ItemAlreadyExistsException;
import lk.ijse.gdse68.webposspring.exception.ItemNotFoundException;
import lk.ijse.gdse68.webposspring.service.ItemService;
import lombok.RequiredArgsConstructor;
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
        try {
            ItemDTO<MultipartFile> itemDTO = new ItemDTO<>(item, itemName, Double.parseDouble(price), Integer.parseInt(quantity), category, image);
            itemService.saveItem(itemDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ItemAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (DataPersistFailedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping( path = "/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateItem(@PathVariable("itemId") String itemId,
            @RequestPart("itemName") String itemName,
            @RequestPart("price") String price,
            @RequestPart("quantity") String quantity,
            @RequestPart("category") String category,
            @RequestPart("image") MultipartFile image
    ) {
        try {
            ItemDTO<MultipartFile> itemDTO= new ItemDTO<>(itemId, itemName, Double.parseDouble(price), Integer.parseInt(quantity), category, image);
            itemService.updateItem(itemId, itemDTO);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ItemAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (DataPersistFailedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(path = "/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable("itemId") String itemId) {
        if (itemId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else {
            try {
                itemService.deleteItem(itemId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } catch (ItemNotFoundException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @GetMapping(path = "/{itemId}")
    public ResponseEntity<ItemDTO<String>> searchItem(@PathVariable("itemId") String itemId) {
        try {
            ItemDTO<String> itemDTO = itemService.searchItem(itemId);
            return ResponseEntity.ok(itemDTO);
        } catch (ItemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDTO<String>>> getAllItems() {
        try {
            return ResponseEntity.ok(itemService.getAllItems());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}

