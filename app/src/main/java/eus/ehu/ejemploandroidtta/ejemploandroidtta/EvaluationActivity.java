package eus.ehu.ejemploandroidtta.ejemploandroidtta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import modelo.*;

public class EvaluationActivity extends AppCompatActivity {

    public static final String EXTRA_LOGIN = "login";
    public static final String EXTRA_TEST = "test";
    public School school = new School("http://u017633.ehu.eus:28080/ServidorTta/rest/tta");
    //public static Test test;
    //public static Exercise exercise;
    private Data data = Data.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        //Intent intent = getIntent();
        TextView textLogin = (TextView)findViewById(R.id.menu_login);
        //textLogin.setText("Bienvenido " + intent.getStringExtra(EXTRA_LOGIN));
        textLogin.setText("Bienvenido "+ data.getUser().getName());

        TextView textLesson = (TextView)findViewById(R.id.lesson_title);
        textLesson.setText("Lección "+data.getUser().getLessonNumber()+"\n"+data.getUser().getLessonTitle());

    }

    public void test(View view) {

        new ProgressTask<Test>(this){
            @Override
            protected Test work() throws Exception{
                return school.getTest();
            }
            @Override
            protected void onFinish(Test result){
                data.setTest(result);
                Intent intent = new Intent(this.context, TestActivity.class);
                startActivity(intent);
            }
        }.execute();

    }

    public void exercise(View view) {

        new ProgressTask<Exercise>(this){
            @Override
            protected Exercise work() throws Exception{
                return school.getExercise(1);
            }
            @Override
            protected void onFinish(Exercise result){
                data.setExercise(result);
                Intent intent = new Intent(this.context, ExerciseActivity.class);
                startActivity(intent);
            }
        }.execute();

    }

    public void monitoring(View view) {
        //No se si hará falta lanzar una actividad nueva o meter código dinámicamente en esta
    }

}
