package ru.mera.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.mera.hibernate.entity.Student;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration {

    private String name;
    private String email;
    private String password;
    private BufferedReader reader;

    private void readName() throws IOException{

        //reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Введите ваше имя: ");
            name = reader.readLine();
            if (checkName(name)) break;
            else System.out.println("Не очень-то похоже на имя.");
        }
    }

    private boolean checkName(String  input){
        Pattern pattern = Pattern.compile("[a-zA-zа-яА-Я]+");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private void readEmail() throws IOException{

        while (true){
            System.out.println("Адрес электронной почты:");
            email = reader.readLine();
            if (checkEmail(email)) break;
            else System.out.println("Не очень-то похоже на адрес.");
        }
    }

    public boolean checkEmail(String input){
        Pattern pattern = Pattern.compile("\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*\\.\\w{2,4}");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private void readPassword() throws IOException{
        while (true){
            System.out.println("Введите ваш пароль(не менее 5 символов):");
            password = reader.readLine();
            if(checkPassword(password)) break;
            System.out.println("Плохой пароль");
        }
    }

    private boolean checkPassword(String password){
        Pattern regex = Pattern.compile("\\w+\\W+|\\W+\\w+");
        Matcher matcher = regex.matcher(password);
        return matcher.matches() && password.length() >= 5;
    }

    private void saveToDBase(){

        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");//populates the data of the configuration file

        //creating session factory object
        SessionFactory factory = cfg.buildSessionFactory();

        //creating session object
        Session session = factory.openSession();
        session.beginTransaction();

        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setPassword(password);

        session.save(student);
        session.getTransaction().commit();
        session.close();
        factory.close();
    }


    public static void main(String[] args){

        Registration registration = new Registration();
        try {
            registration.reader = new BufferedReader(new InputStreamReader(System.in));
            registration.readName();
            registration.readEmail();
            registration.readPassword();
            registration.saveToDBase();

            registration.reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}