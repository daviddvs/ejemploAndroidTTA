package eus.ehu.ejemploandroidtta.ejemploandroidtta;


import modelo.Pregunta;
import modelo.Test;
import modelo.AudioPlayer;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    int correct = -1;
    RadioGroup group;
    Test test;
    String advise;
    String adviseType;
    ViewGroup layout;

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
        test = new Test();
        TextView textWording = (TextView) findViewById(R.id.test_wording);
        textWording.setText(test.getEnunciado());
        group = (RadioGroup)findViewById(R.id.test_choices);
        int i = 0;
        for(Pregunta pregunta : test.getTest()) {
            RadioButton radio = new RadioButton(this);
            radio.setText(pregunta.enunciado);
            radio.setOnClickListener(this);
            group.addView(radio);
            if (pregunta.correcta)
                correct = i;
            i++;

        }
    }

    public void send(View view) {
        layout = (ViewGroup)view.getParent();
        int selected = (group.getCheckedRadioButtonId()-1);
        int choices = group.getChildCount();
        for (int i=0; i<choices; i++)
            group.getChildAt(i).setEnabled(false);
        //findViewById(R.id.button_send_test).setEnabled(false);
        layout.removeView(findViewById(R.id.button_send_test));

        group.getChildAt(correct).setBackgroundColor(Color.GREEN);
        if(selected != correct){
            group.getChildAt(selected).setBackgroundColor(Color.RED);
            Toast.makeText(getApplicationContext(), "¡Has fallado!", Toast.LENGTH_SHORT).show();
            adviseType = test.getTest().get(selected).tipoMIMEAyuda;
            advise = test.getTest().get(selected).recursoAyuda;
            if(advise != null && !advise.isEmpty())
                findViewById(R.id.button_view_advise).setVisibility(View.VISIBLE);
        } else
            Toast.makeText(getApplicationContext(), "¡Correcto!", Toast.LENGTH_SHORT).show();
    }

    public void help(View view) {

        if ( adviseType.equals("text/html") ) {
            if(advise.substring(0,10).contains("://")) {
                Uri uri = Uri.parse(advise);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
            else {
                WebView web = new WebView(this);
                //web.loadUrl(advise);
                web.loadData(advise, "text/html", null);
                web.setBackgroundColor(Color.TRANSPARENT);
                web.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
                layout.addView(web);
            }
        }
        else if( adviseType.equals("video") ) {
            showVideo(advise);
        }
        else if( adviseType.equals("audio") ) {
            playAudio(advise, (View) view.getParent());
        }
    }

    private void showVideo(String advise){
        VideoView video = new VideoView(this);
        video.setVideoURI(Uri.parse(advise));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        video.setLayoutParams(params);

        MediaController controller = new MediaController(this) {
            @Override
            public void hide() {
            }

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                    finish();//Finaliza la reproducción de video
                return super.dispatchKeyEvent(event);
            }
        };
        controller.setAnchorView(video);
        video.setMediaController(controller);

        layout.addView(video);
        video.start();
    }

    private void playAudio(String advise, View view) {

        Runnable run = new Runnable() {
            @Override
            public void run() {
                finish();
            }
        };
        AudioPlayer audioPlayer = new AudioPlayer(view, run);
        try {
            audioPlayer.setAudioUri(Uri.parse(advise));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        findViewById(R.id.button_send_test).setVisibility(View.VISIBLE);
    }

}
