package ru.mera.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.mera.hibernate.entity.Answer;
import ru.mera.hibernate.entity.Question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoadFromFileToDBase {
    private String url = "jdbc:mysql://localhost:3306/hibernate";
    private String username = "root";
    private String password = "root";
    private int countOfFiles = getCountOfFiles("C:\\Users\\apuzik\\IdeaProjects\\StudentTest\\Questions");
    private SessionFactory factory;
    private Session session;

    public LoadFromFileToDBase() {
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        factory = cfg.buildSessionFactory();
        session = factory.openSession();
    }

    private int getCountOfFiles(String path) {
        int count = 0;
        File file = new File(path);
        File listFile[] = file.listFiles();
        for (File file1 : listFile) {
            if (file1.isFile()) count++;
        }
        return count;
    }

    private void readAndAdd(String fileName, int id) throws IOException, SQLException, ClassNotFoundException {

        StringBuffer buffer = new StringBuffer();
        List<String> answers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){

            boolean flag = false;

            while (reader.ready()){
                String line = reader.readLine();
                if (line.equals("----START ANSWERS----")) {
                    flag = true;

                } else if (!flag) {
                    //продолжаем читать вопрос
                    buffer.append(line).append("\n");

                } else if (flag) {
                    //читаем ответы
                    answers.add(line);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        addToQuestion(buffer.toString(), 2);

        addToAnswers(answers, id);

    }

    private void addToQuestion(String textOfQuestion, int difficultyLevel) throws ClassNotFoundException, SQLException {

        Question question = new Question();
        session = factory.openSession();
        session.beginTransaction();
        question.setTextOfQuestion(textOfQuestion);
        question.setDifficultyLevelId(difficultyLevel);
        session.save(question);
        session.getTransaction().commit();

    }

    private void addToAnswers(List<String> answers, int id) throws SQLException, ClassNotFoundException {

        for (String textOfAnswer : answers){

            String[] data = textOfAnswer.split(",", 2);
            Answer answer = new Answer();
            session = factory.openSession();
            session.beginTransaction();
            answer.setTextOfAnswer(data[1]);
            answer.setCorrect(data[0].equals("1"));
            answer.setQuestionId(id);
            session.save(answer);
            session.getTransaction().commit();

        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException{

        String path = "C:\\Users\\apuzik\\IdeaProjects\\StudentTest\\Questions";
        LoadFromFileToDBase load = new LoadFromFileToDBase();


        for (int i = 1; i <= load.countOfFiles ; i++) {
            String fileName = path + "\\test" + i + ".txt";
            load.readAndAdd(fileName, i);

        }

        load.session.close();
        load.factory.close();
    }
}
