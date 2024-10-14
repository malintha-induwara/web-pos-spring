package lk.ijse.gdse68.webposspring.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDTO <T> {
    private String itemId;
    private String itemName;
    private double price;
    private int quantity;
    private String category;
    private T image;
}

