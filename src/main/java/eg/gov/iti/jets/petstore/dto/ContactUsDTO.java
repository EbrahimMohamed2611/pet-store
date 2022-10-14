package eg.gov.iti.jets.petstore.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ContactUsDTO {
    private String name;
    private String email;
    private String subject;
    private String Message;

}
