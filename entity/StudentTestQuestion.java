package ru.mera.hibernate.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "student_test_question")
public class StudentTestQuestion {

    private int id;
    private int studentTestId;
    private int questionId;
    private StudentTest studentTest;
    private Set<StudentTestAnswers> studentTestAnswers = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "test_id")
    public int getStudentTestId() {
        return studentTestId;
    }

    public void setStudentTestId(int studentTestId) {
        this.studentTestId = studentTestId;
    }

    @Column(name = "question_id")
    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    @ManyToOne
    @JoinColumn(name = "test_id", insertable = false, updatable = false)
    public StudentTest getStudentTest() {
        return studentTest;
    }

    public void setStudentTest(StudentTest studentTest) {
        this.studentTest = studentTest;
    }

    @OneToMany(mappedBy = "studentTestQuestion")
    public Set<StudentTestAnswers> getStudentTestAnswers() {
        return studentTestAnswers;
    }

    public void setStudentTestAnswers(Set<StudentTestAnswers> studentTestAnswers) {
        this.studentTestAnswers = studentTestAnswers;
    }
}
