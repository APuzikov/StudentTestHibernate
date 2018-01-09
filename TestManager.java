package ru.mera.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class TestManager {

    private SessionFactory factory;
    private Session session;

    public TestManager(){
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        factory = cfg.buildSessionFactory();
        session = factory.openSession();
    }


    void startTest() throws IOException{

        int numberOfQuestion = 1;
        String input;
        int resultOfTest = 0;
        List<Question> questions;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Адрес электронной почты:");
        String studentEmail = reader.readLine();
        System.out.println("Пароль:");
        String studentPassword = reader.readLine();

        StudentLoaderFromDBase studentLoaderFromDBase = new StudentLoaderFromDBase(session);
        Student student = studentLoaderFromDBase.loadByEmail(studentEmail, studentPassword);


        if (student == null){
            System.out.println("Студент не найден, или неверный пароль");
        } else {
            System.out.println("Студент: " + student.getName());
            System.out.println("Уровень сложности - " + "NORMAL");
            System.out.println("Нужно ответить правильно на " + 65 + "% вопросов");

            StudentTest studentTest = new StudentTest();
            SaveResults saveResults = new SaveResults(student, session);
            saveResults.saveStudentTest(studentTest);

            QuestionsLoader questionsLoader = new QuestionsLoader(6, session);
            questions = questionsLoader.getQuestions();

            Test test = new Test(questions.size(), session);



            for(Question question : questions){

                StudentTestQuestion studentTestQuestion = new StudentTestQuestion();

                saveResults.saveStudentTestQuestions(studentTest.getId(), question.getId(), studentTestQuestion);

                test.outputQuestionsToConsole(question, numberOfQuestion);
                numberOfQuestion++;

                input = test.readFromConsole();

                resultOfTest += test.checkOfCorrect(input, question, studentTestQuestion.getId());

            }

            test.resultsToConsole(resultOfTest, student.getName());
            saveResults.updateStudentTest(studentTest, test.calculatePercent(resultOfTest));

        }

    }

    public static void main(String[] args) throws IOException {

        TestManager testManager = new TestManager();
        testManager.startTest();

        testManager.session.close();
        testManager.factory.close();

    }
}
