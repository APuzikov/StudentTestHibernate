package ru.mera.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.mera.hibernate.entity.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutputResultsFromDBase {

    private SessionFactory factory;
    private Session session;
    private BufferedReader reader;
    private String name;

    public OutputResultsFromDBase(BufferedReader reader) {
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        factory = cfg.buildSessionFactory();
        session = factory.openSession();

        this.reader = reader;
    }

    public boolean isDigits(String input){
        Pattern regex = Pattern.compile("\\d+");
        Matcher matcher = regex.matcher(input);
        return matcher.matches();
    }

    public int digitInput() throws IOException {

        String input;
        int outPut;

        while (true) {
            input = reader.readLine();
            if (input.equals("exit")) return 0;
            else {
                if (isDigits(input)) {
                    outPut = Integer.parseInt(input);
                    break;
                }
                System.out.println("Неверный ввод.");
            }
        }
        return outPut;
    }

    public void readNameFromConsole() throws IOException {

        System.out.println("Введите ваше имя:");
        while (true){
            name = reader.readLine();
            if (checkName(name)) break;
            System.out.println("Не очень-то похоже на имя, попробуй еще раз.");
        }
    }

    private boolean checkName(String name){
        Pattern regex = Pattern.compile("[a-zA-Z]+|[а-яА-Я]+");
        Matcher matcher = regex.matcher(name);
        return matcher.matches();
    }

    void resultsToConsole(String name) throws IOException {

        List<StudentTest> studentTests;
        List<Question> questions;
        int studentTestId = - 1;
        int studentTestQuestionId = -1;

        int studentId = getStudentId(name);

        if (studentId != 0) {

            outputStudentTest(studentId);

            while(studentTestId != 0) {

                System.out.println("Для просмотра вопросов, выберите номер теста или наберите 'exit' для выхода");
                studentTestId = digitInput();

                if (studentTestId != 0) {
                    outputQuestions(studentTestId);

                    while (studentTestQuestionId != 0) {

                        System.out.println("Для просмотра ответов, выберите номер вопроса в тесте или 'exit' для выхода");
                        studentTestQuestionId = digitInput();

                        if (studentTestQuestionId != 0) {
                            outputAnswers(studentTestQuestionId);
                        }
                    }
                }
            }

        } else System.out.println("Студент не найден.");
    }

    int getStudentId(String name){

        int studentId = 0;
        List<Student> students ;

        //Query query1 = session.getCriteriaBuilder();
        Query query = session.createQuery("from Student");
        students = query.list();

        for (Student student : students){
            if (name.equals(student.getName())) studentId = student.getId();
        }
        return studentId;
    }

    private void outputStudentTest(int studentId){

        List<StudentTest> studentTests;
        Query query = session.createQuery("from StudentTest where studentId = ?");
        query.setParameter(0, studentId);
        studentTests = query.list();

        for(StudentTest studentTest : studentTests){
                System.out.println("Тест №: " + studentTest.getId() + ", студент: " + name );
                System.out.println("Начало теста: " + studentTest.getTestDate() + ", окончание: " + studentTest.getEndDate());
                System.out.println("Результат: " + studentTest.getTestResult() + " %");
                System.out.println("");
        }

    }

    private void outputQuestions(int studentTestId){

        List<StudentTestQuestion> studentTestQuestions;

        Query query = session.createQuery("from StudentTestQuestion where studentTestId = ?");
        query.setParameter(0, studentTestId);
        studentTestQuestions = query.list();

        for (StudentTestQuestion studentTestQuestion : studentTestQuestions){

            System.out.println("Номер вопроса в тесте: " + studentTestQuestion.getId());
            System.out.println(getQuestion(studentTestQuestion.getQuestionId()).getTextOfQuestion());
            System.out.println("");

        }
    }

    private void outputAnswers(int studentTestQuestionId){
        List<StudentTestAnswers> studentTestAnswers;

        Query query = session.createQuery("from StudentTestAnswers where studentTestQuestionId = ?");
        query.setParameter(0, studentTestQuestionId);
        studentTestAnswers = query.list();

        System.out.println("Вы выбрали ответы: ");
        System.out.println("");

        for (StudentTestAnswers studentTestAnswer : studentTestAnswers){

            System.out.println(getAnswer(studentTestAnswer.getAnswerId()).getTextOfAnswer() + " - "
                    + getAnswer(studentTestAnswer.getAnswerId()).isCorrect());
        }
    }

    private Question getQuestion (int questionId){

        Query query = session.createQuery("from Question where id = ?");
        query.setParameter(0, questionId);

        return (Question)query.getSingleResult();
    }

    private Answer getAnswer (int answerId){

        Query query = session.createQuery("from Answer where id = ?");
        query.setParameter(0, answerId);
        return (Answer)query.getSingleResult();
    }

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        OutputResultsFromDBase results = new OutputResultsFromDBase(reader);

        results.readNameFromConsole();
        results.resultsToConsole(results.name);

        reader.close();
        results.session.close();
        results.factory.close();

    }

}
