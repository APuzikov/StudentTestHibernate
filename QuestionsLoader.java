package ru.mera.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.mera.hibernate.entity.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionsLoader {

    static final Logger loggerQL = LogManager.getLogger(QuestionsLoader.class);

    private int countOfQuestions;
    private Session session;
    private List<Question> questions = new ArrayList<Question>();

    public QuestionsLoader(int countOfQuestions, Session session) {
        this.countOfQuestions = countOfQuestions;
        this.session = session;
    }

    List<Question> getQuestions(){
        int[] numbersOfQuestions = new int[countOfQuestions];

        for (int i = 0; i < countOfQuestions; i++){
            while (true){
                int value = getRandomNumber();
                if (checkNumber(value, numbersOfQuestions)){
                    numbersOfQuestions[i] = value;
                    break;
                }
            }

        }

        for (int i = 0; i < countOfQuestions; i++){
            Query query = session.createQuery("from Question where id = ?");
            query.setParameter(0, numbersOfQuestions[i]);
            questions.add((Question)query.getSingleResult());
        }

        loggerQL.info("Список вопросов загружен из базы данных");

        return questions;
    }

    private int getRandomNumber() {
        return (int)Math.round(Math.random() * (getCountOfQuestions() - 1)) + 1;
    }

    private boolean checkNumber(int number, int[] numbersOfQuestion){
        boolean check = true;

        for (int i : numbersOfQuestion){
            if (i == number){
                check = false;
            }
        }
        return check;
    }

    private int getCountOfQuestions(){
        Query query = session.createQuery("from Question");
        return query.list().size();
    }

}
