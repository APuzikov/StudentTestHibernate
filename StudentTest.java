package ru.mera.hibernate;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "student_test")
public class StudentTest {

    private int id;
    private int studentId;
    private Timestamp testDate;
    private Timestamp endDate;
    private float testResult;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "student_id")
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Column(name = "test_date")
    public Timestamp getTestDate() {
        return testDate;
    }

    public void setTestDate(Timestamp testDate) {
        this.testDate = testDate;
    }

    @Column(name = "end_date")
    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Column(name = "test_result")
    public float getTestResult() {
        return testResult;
    }

    public void setTestResult(float testResult) {
        this.testResult = testResult;
    }
}
