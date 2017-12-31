package eus.ehu.ejemploandroidtta.ejemploandroidtta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EvaluationActivity extends AppCompatActivity {

    public static final String EXTRA_LOGIN = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        Intent intent = getIntent();
        TextView textLogin = (TextView)findViewById(R.id.menu_login);
        textLogin.setText(intent.getStringExtra(EXTRA_LOGIN));
    }

    public void test(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        //intent.putExtra(EvaluationActivity.EXTRA_LOGIN,login);
        startActivity(intent);
    }

    public void exercise(View view) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        //intent.putExtra(EvaluationActivity.EXTRA_LOGIN,login);
        startActivity(intent);
    }

    public void monitoring(View view) {
        //No se si hará falta lanzar una actividad nueva o meter código dinámicamente en esta
    }

}
