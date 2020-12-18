package by.questionary.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Table(name = "answer")
@Data
@ToString(includeFieldNames = false)
@NoArgsConstructor
//@RequiredArgsConstructor - для конструктора с файнал полями
//@AllArgsConstructor - для конструктора со всеми полями
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "test_result_id")
    private TestResult testResult;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "question_id")
    private Question question;

    @Length(min = 0, max = 2048, message = "Answer is much long (0-2048 symbols)")
    @Column(nullable = true)
    private String answer;

}
