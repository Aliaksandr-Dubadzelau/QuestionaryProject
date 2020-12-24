package by.questionary.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@ToString(includeFieldNames = false)
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    private long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Please, fill the field name")
    @Length(min = 3, max = 50, message = "Name is wrong (3-50)")
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Please, fill the field email")
    @Length(min = 3, max = 70, message = "Email is wrong (3-70)")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Please, fill the field password")
    @Length(min = 6, max = 50, message = "Password is wrong (6-50)")
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Column(nullable = true)
    private int passwordCode;

    @Column(nullable = true, unique = true)
    private String activationCode;

    @Column(nullable = false)
    private boolean activated;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date registrationDate;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Test> tests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TestResult> results;

}
