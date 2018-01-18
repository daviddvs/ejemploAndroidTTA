package modelo;

/**
 * Created by tta on 1/16/18.
 */

public class Data {
    private static Data instance = null;
    private User user = null;
    private Test test = null;
    private Exercise exercise = null;

    private Data() {
    }

    public static synchronized Data getInstance() {
        if(instance == null)
            instance = new Data();
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}
