package ru.mera.hibernate;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.util.List;

public class HibernateApp {

    public static void main(String[] args) {
        //HibernateSessionFactory sessionFactory = new HibernateSessionFactory();
        //Session session = HibernateSessionFactory.getSessionFactory().openSession();

        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");//populates the data of the configuration file

        List<Student> students;
        List<Answer> answers;

        //creating seession factory object
        SessionFactory factory = cfg.buildSessionFactory();

        //creating session object
        Session session = factory.openSession();

        Query query = session.createQuery("from Answer");
        //query.setParameter(0, 3);
        answers = query.list();
        int i = answers.size();
        System.out.println(i + "-------------------------");

        for (Answer answer : answers){
            System.out.println(answer.getTextOfAnswer() + " - " + answer.isCorrect());
        }
//        session beginTransaction();
//        student.setName("Ivanov");
//        student.setEmail("ivanov@mail.ru");
//        student.setPassword("1111");
//        Student student1 = new Student();
//        student1.setName("Petrov");
//        student1.setEmail("Petrov@mail.ru");
//        student1.setPassword("1111");
//        session.save(student);
//        session.save(student1);
//        //System.out.println("save");
//        session.getTransaction().commit();
//        //System.out.println("transaction");


        session.close();
        //System.out.println("close");
        factory.close();
    }
}
