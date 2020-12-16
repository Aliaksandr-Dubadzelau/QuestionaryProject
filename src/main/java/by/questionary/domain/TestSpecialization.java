package by.questionary.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "test_specialization")
@Data(staticConstructor = "of")
@ToString(includeFieldNames = false)
@NoArgsConstructor
public class TestSpecialization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "test_id")
    @Column(nullable = false)
    private Test test;

    @Column(nullable = false)
    private boolean numeratedPage;

    @Column(nullable = false)
    boolean numeratedQuestion;

    @Column(nullable = false)
    private boolean randomQuestionSequence;

    @Column(nullable = false)
    private boolean indicator;

    @Column(nullable = false)
    private boolean obligatoryAnswer;

    @Column(nullable = false)
    private boolean anonymous;

}
