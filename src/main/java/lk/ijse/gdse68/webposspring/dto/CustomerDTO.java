package lk.ijse.gdse68.webposspring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO implements Serializable {
    private String customerId;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dob;
    private String address;
    private String mobile;
}

