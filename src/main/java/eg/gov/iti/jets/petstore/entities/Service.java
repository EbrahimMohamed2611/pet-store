package eg.gov.iti.jets.petstore.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Float price;
    @Lob
    private String description;
    private Float discount;
    private LocalTime startTime;
    private LocalTime endTime;
    private Duration timePerService;
    private Boolean deliverable;
    @Column(name = "IMAGE_URL", nullable = false)
    private String imageUrl;
    @ManyToOne(optional = false)
    private ServiceProvider provider;
    @ManyToOne(optional = false)
    private ServiceType type;
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ServiceRate> rates;
}
