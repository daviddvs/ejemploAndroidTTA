package eus.ehu.ejemploandroidtta.ejemploandroidtta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        fillTest();
    }

    private void fillTest() {
        //Test test = data.getTest();// obtener preguntas de Test de algun sitio, de un objeto Test
        List<String> choices = new ArrayList<String>();
        choices.add("Option0");choices.add("Option1");choices.add("Option2");choices.add("Option3");
        TextView textWording = (TextView) findViewById(R.id.test_wording);
        textWording.setText("Pregunta de prueba");
        RadioGroup group = (RadioGroup)findViewById(R.id.test_choices);
        int i = 0;
        for(String choice : choices) {
            RadioButton radio = new RadioButton(this);
            radio.setText(choice);
            radio.setOnClickListener(this);
            group.addView(radio);
            /* Esto es para cuando sepa como es el objeto Test
            if (choice.isCorrect())
                correct = i;
            i++;
            */
        }
    }

    public void onClick(View v) {
        findViewById(R.id.button_send_test).setVisibility(View.VISIBLE);
    }

}
