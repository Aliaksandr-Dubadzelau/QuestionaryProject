package by.questionary.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "test")
@Data(staticConstructor = "of")
@ToString(includeFieldNames = false)
@NoArgsConstructor
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateOfChange;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id")
    @Column(nullable = false)
    private User creator;

    @OneToOne(mappedBy = "test", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private TestSpecialization specialization;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Question> questions;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<TestResult> results;

}
