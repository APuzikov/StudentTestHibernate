package ru.mera.hibernate.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "questions")
public class Question {

    private int id;
    private String textOfQuestion;
    private int difficultyLevelId;
    private DifficultyLevel difficultyLevel;
    private Set<Answer> answers = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "text_of_question", nullable = false)
    public String getTextOfQuestion() {
        return textOfQuestion;
}

    public void setTextOfQuestion(String textOfQuestion) {
        this.textOfQuestion = textOfQuestion;
    }

    @Column(name = "difficulty_level_id", nullable = false)
    public int getDifficultyLevelId() {
        return difficultyLevelId;
    }

    public void setDifficultyLevelId(int difficultyLevelId) {
        this.difficultyLevelId = difficultyLevelId;
    }

    @OneToMany(mappedBy = "question")
    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    @ManyToOne
    @JoinColumn(name = "difficulty_level_id", insertable = false, updatable = false)
    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}
