package ru.mera.hibernate;

import org.hibernate.query.Query;
import org.hibernate.Session;
import ru.mera.hibernate.entity.Student;;import java.util.List;

public class StudentLoaderFromDBase {

    private Session session;

    StudentLoaderFromDBase(Session session) {
        this.session = session;
    }

    Student loadByEmail(String studentEmail, String studentPassword) {
        Student student = null;
        List<Student> students;

        Query query = session.createQuery("from Student");
        //query.setParameter(0, studentEmail);
        students = query.list();

        for (Student s : students){
            if (s.getEmail().equals(studentEmail) && s.getPassword().equals(studentPassword)){
                student = s;
            }
        }

        return student;
    }
}
