package eus.ehu.ejemploandroidtta.ejemploandroidtta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import modelo.*;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public School school = new School("http://u017633.ehu.eus:28080/ServidorTta/rest/tta");
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private boolean authenticate(String login, String passwd) {
        return login.equals(passwd);
    }

    public void login(View view) {
        Intent intent = new Intent(this, EvaluationActivity.class);
        String login = ((EditText)findViewById(R.id.login)).getText().toString();
        String passwd = ((EditText)findViewById(R.id.passwd)).getText().toString();

        if( authenticate(login, passwd) ) {

            new ProgressTask<User>(this){
                @Override
                protected User work() throws Exception{
                    return school.getUser("12345678A");
                }
                @Override
                protected void onFinish(User result){
                    MainActivity.user=result;
                    Intent intent = new Intent(this.context, EvaluationActivity.class);
                    //intent.putExtra(EvaluationActivity.EXTRA_LOGIN,login);
                    startActivity(intent);
                }
            }.execute();

        }
        else {
            Toast.makeText(this, "Error de acceso", Toast.LENGTH_SHORT).show();
        }

    }
}
