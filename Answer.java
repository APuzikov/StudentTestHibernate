package ru.mera.hibernate;

import javax.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {

    private int id;
    private String textOfAnswer;
    private boolean correct;
    private int questionId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "text_answer", nullable = false)
    public String getTextOfAnswer() {
        return textOfAnswer;
    }
    public void setTextOfAnswer(String textOfAnswer) {
        this.textOfAnswer = textOfAnswer;
    }

    @Column(name = "correct", nullable = false)
    public boolean isCorrect() {
        return correct;
    }
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @Column(name = "question_id", nullable = false)
    public int getQuestionId() {
        return questionId;
    }
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
}
