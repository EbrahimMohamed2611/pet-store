package eg.gov.iti.jets.petstore.entities;

import eg.gov.iti.jets.petstore.enums.Gender;
import eg.gov.iti.jets.petstore.enums.Roles;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USER_INFO")
@Data
@Inheritance
@DiscriminatorColumn(name = "role")
//@DiscriminatorValue("ROLE_USER")
@NoArgsConstructor
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(name = "phone_number", length = 11)
    private String phoneNumber;
    private Address address;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", insertable = false, updatable = false)
    private Roles role ;
    @Column(name = "user_name")
    private String userName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    //For Security
    @Column(name = "IS_ACTIVE")
    private boolean isActive;
    @Column(name = "IS_NOT_LOCKED")
    private boolean isNotLocked;
  
    public User(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.isActive = user.isActive();
        this.role = user.getRole();
        this.userName = user.getUserName();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.isNotLocked = user.isNotLocked();
        this.birthDate = user.getBirthDate();
        this.gender = user.getGender();
    }
}
