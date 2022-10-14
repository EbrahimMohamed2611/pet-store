package eg.gov.iti.jets.petstore.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Species {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
}
