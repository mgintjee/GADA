package com.happybananastudio.gada;

/**
 * Created by mgint on 7/12/2018.
 */

public class ClassQuestion {
    public String CorrectAnswer, Question, FalseAnswers;

    ClassQuestion() {
    }

    ClassQuestion(String correctAnswer, String question, String falseAnswers) {
        super();
        CorrectAnswer = correctAnswer;
        Question = question;
        FalseAnswers = falseAnswers;
    }
}
