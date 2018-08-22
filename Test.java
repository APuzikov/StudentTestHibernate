package ru.mera.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.mera.hibernate.entity.Answer;
import ru.mera.hibernate.entity.Question;
import ru.mera.hibernate.entity.StudentTestAnswers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    static final Logger testLogger = LogManager.getLogger(TestManager.class);

    private int countOfQuestions;
    private Session session;

    public Test(int countOfQuestions, Session session) {
        this.countOfQuestions = countOfQuestions;
        this.session = session;
    }

    String readFromConsole() throws IOException{
        String inputFromConsole = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {

            try {
                inputFromConsole = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                testLogger.info("Ошибка ввода");
            }
            Pattern regex1 = Pattern.compile("^\\d(,\\d)*");
            Matcher m1 = regex1.matcher(inputFromConsole);

            if (!m1.matches()) {
                System.out.println("Неверный ввод");
            } else {
                //System.out.println("Yes");
                break;
            }
        }

        testLogger.info("Ответы считаны из консоли");

        return inputFromConsole;
    }

    void outputQuestionsToConsole(Question question, int numberOfQuestion) {
        int countOfcorrect = 0;
        List<Answer> answers;
        System.out.println("Вопрос N " + numberOfQuestion + " из " + countOfQuestions);

        System.out.println(question.getTextOfQuestion());

        Query query = session.createQuery("from Answer where questionId = ?");
        query.setParameter(0, question.getId());
        answers = query.list();

        int index = 1;

        //answers.stream().forEach(a -> System.out.println(a.getTextOfAnswer()));

        for (Answer answer : answers) {                     //вывод вариантов ответов в консоль
            System.out.println(index + " " + answer.getTextOfAnswer() + " " + answer.isCorrect());
            index++;
            if (answer.isCorrect()) countOfcorrect++;
        }
        System.out.println("Выберите " + countOfcorrect + " ответов");

        testLogger.info("Вопрос и список ответов выведены в консоль");
    }


    int checkOfCorrect(String input, Question question, int studentTestQuestionId){

        List<Answer> answers;
        boolean correct = true;

        String[] str = input.split(",");
        int[] digits = new int[str.length];

        Query query = session.createQuery("from Answer where questionId = ?");
        query.setParameter(0, question.getId());
        answers = query.list();

        SaveResults saveResults = new SaveResults(session);
        for(String s : str){
            int number = Integer.parseInt(s);

            if (number <= answers.size() && number != 0){
                StudentTestAnswers studentTestAnswers = new StudentTestAnswers();
                saveResults.saveStudentTestAnswers(studentTestQuestionId, answers.get(number - 1).getId(), studentTestAnswers);
            } else System.out.println("Такого ответа нет" + "\n");
        }


        if (digits.length != getCountOfCorrect(answers)){
            correct = false;
        } else {

            for (int i = 0; i < digits.length; i++){

                digits[i] = Integer.parseInt(str[i]);
//                StudentTestAnswers studentTestAnswers = new StudentTestAnswers();
//                saveResults.saveStudentTestAnswers(studentTestQuestionId, answers.get(digits[i] - 1).getId(), studentTestAnswers);

                   correct = digits[i] <= answers.size() && digits[i] != 0 && correct && answers.get(digits[i] - 1).isCorrect();
            }

        }
        return correct ? 1 : 0;
    }

    void resultsToConsole(int resultOfTest, String studentName) {

        int percent = calculatePercent(resultOfTest);

        System.out.println("Студент " + studentName + " " + percent + "%");

        if (percent >= 65) {
            System.out.println("Тест успешно пройден ");
        } else System.out.println("Тест не пройден");
    }

    private long getCountOfCorrect(List<Answer> answers){
        //int countOfCorrect = 0;
//        for (Answer answer : answers){
//
//            if (answer.isCorrect()) countOfCorrect++;
//        }
      return answers.stream().filter(answer -> answer.isCorrect()).count();
    }

    int calculatePercent(int resultOfTest) {

        return Math.round((float) resultOfTest/countOfQuestions*100);
    }

}

