package eus.ehu.ejemploandroidtta.ejemploandroidtta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import modelo.*;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public School school = new School("http://u017633.ehu.eus:28080/ServidorTta/rest/tta");
    private Data data = Data.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void authenticate(final String login, final String passwd) {

        new ProgressTask<User>(this){
            @Override
            protected User work() throws Exception{
                return school.getUser(login, passwd);
            }
            @Override
            protected void onFinish(User result){
                data.setUser(result);
                if(data.getUser() != null) {
                    data.getUser().setDni(login);
                    data.getUser().setPasswd(passwd);
                    shiftActivity(true);
                } else if (data.getUser() == null)
                    shiftActivity(false);
            }
        }.execute();
        //Log.d("W",data.getUser().getName());
    }

    public void login(View view) {
        String login = ((EditText)findViewById(R.id.login)).getText().toString();
        String passwd = ((EditText)findViewById(R.id.passwd)).getText().toString();
        authenticate(login, passwd);
    }

    private void shiftActivity(boolean auth) {
        if( auth ) {
            Intent intent = new Intent(this, EvaluationActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Error de acceso", Toast.LENGTH_SHORT).show();
        }
    }
}
