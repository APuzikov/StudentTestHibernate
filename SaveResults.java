package ru.mera.hibernate;

import org.hibernate.Session;
import ru.mera.hibernate.entity.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class SaveResults {

    private Student student;
    private List<Question> questions;
    private int resultOfTest;
    private Session session;

    public SaveResults() {
    }

    SaveResults(Student student, Session session) {
        this.student = student;
        this.session = session;
    }

    public SaveResults(Session session) {
        this.session = session;
    }

    StudentTest saveStudentTest(StudentTest studentTest){

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        //StudentTest studentTest = new StudentTest();

        session.beginTransaction();
        studentTest.setStudentId(student.getId());
        studentTest.setCreateDate(timestamp);
        studentTest.setTestDate(timestamp);
        session.save(studentTest);
        session.getTransaction().commit();
        return studentTest;
    }

    void saveStudentTestQuestions(int studentTestId, int questionId, StudentTestQuestion studentTestQuestion){

        session.beginTransaction();
        studentTestQuestion.setStudentTestId(studentTestId);
        studentTestQuestion.setQuestionId(questionId);
        session.save(studentTestQuestion);
        session.getTransaction().commit();

    }

    void saveStudentTestAnswers(int studentTestQuestionId, int answerId, StudentTestAnswers studentTestAnswers){

        session.beginTransaction();
        studentTestAnswers.setStudentTestQuestionId(studentTestQuestionId);
        studentTestAnswers.setAnswerId(answerId);
        session.save(studentTestAnswers);
        session.getTransaction().commit();
    }

    void updateStudentTest(StudentTest studentTest, int result){

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        session.beginTransaction();
        studentTest.setEndDate(timestamp);
        studentTest.setTestResult(result);
        session.update(studentTest);
        session.getTransaction().commit();
    }
}
