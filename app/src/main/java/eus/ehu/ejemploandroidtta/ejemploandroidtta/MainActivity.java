package eus.ehu.ejemploandroidtta.ejemploandroidtta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

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
            intent.putExtra(EvaluationActivity.EXTRA_LOGIN,login);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Error de acceso", Toast.LENGTH_SHORT).show();
        }

    }
}
