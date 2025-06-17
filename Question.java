package quiz;

public class Question {
    private String question;
    private String[] options;
    private int correctOption;

    public Question(String question, String option1, String option2, String option3, String option4, int correctOption) {
        this.question = question;
        this.options = new String[]{option1, option2, option3, option4};
        this.correctOption = correctOption;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectOption() {
        return correctOption;
    }
}
