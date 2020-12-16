package by.questionary.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "answer")
@Data(staticConstructor = "of")
@ToString(includeFieldNames = false)
@NoArgsConstructor
//@RequiredArgsConstructor - для конструктора с файнал полями
//@AllArgsConstructor - для конструктора со всеми полями
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "test_result_id")
    @Column(nullable = false)
    private TestResult testResult;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "question_id")
    @Column(nullable = false)
    private Question question;

    @Column(nullable = true)
    private String answer;

}
