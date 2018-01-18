package eus.ehu.ejemploandroidtta.ejemploandroidtta;


import modelo.Data;
import modelo.Pregunta;
import modelo.ProgressTask;
import modelo.School;
import modelo.Test;
import modelo.AudioPlayer;
import modelo.User;

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

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{

    private int correct = -1;
    private RadioGroup group;
    private Test test;
    private String advise;
    private String adviseType;
    private ViewGroup layout;
    public final String baseUrl="http://u017633.ehu.eus:28080/ServidorTta/rest/tta";
    private School school = new School(baseUrl);
    private Data data = Data.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        try {
            fillTest();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillTest() throws IOException, JSONException {
        School school = new School(baseUrl);
        test = data.getTest();
        TextView textWording = (TextView) findViewById(R.id.test_wording);
        textWording.setText(test.getWording());
        group = (RadioGroup)findViewById(R.id.test_choices);
        int i = 0;
        for(Pregunta pregunta : test.getPreguntas()) {
            RadioButton radio = new RadioButton(this);
            radio.setText(pregunta.getEnunciado());
            radio.setOnClickListener(this);
            group.addView(radio);
            if (pregunta.isCorrecta())
                correct = i;
            i++;
        }
    }

    public void send(View view) {
        layout = (ViewGroup)view.getParent();
        int radioButtonId = group.getCheckedRadioButtonId();//Este id no es la posicion del radioButton
        View radioButton = group.findViewById(radioButtonId);
        int selected = group.indexOfChild(radioButton);
        int choices = group.getChildCount();
        for (int i=0; i<choices; i++)
            group.getChildAt(i).setEnabled(false);
        //findViewById(R.id.button_send_test).setEnabled(false);
        layout.removeView(findViewById(R.id.button_send_test));

        group.getChildAt(correct).setBackgroundColor(Color.GREEN);
        if(selected != correct){
            group.getChildAt(selected).setBackgroundColor(Color.RED);
            Toast.makeText(getApplicationContext(), "¡Has fallado!", Toast.LENGTH_SHORT).show();
            adviseType = test.getPreguntas().get(selected).getMime();
            advise = test.getPreguntas().get(selected).getAyuda();
            if(advise != null && !advise.isEmpty())
                findViewById(R.id.button_view_advise).setVisibility(View.VISIBLE);
        } else
            Toast.makeText(getApplicationContext(), "¡Correcto!", Toast.LENGTH_SHORT).show();

        new ProgressTask<Integer>(this){
            @Override
            protected Integer work() throws Exception{
                return school.uploadChoice(data.getUser().getId(), group.getCheckedRadioButtonId());
            }
            @Override
            protected void onFinish(Integer result){
                Toast.makeText(context, "Status code: "+result.toString(), Toast.LENGTH_SHORT).show();
            }
        }.execute();

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
        else if( adviseType.equals("video/mp4") ) {
            showVideo(advise);
        }
        else if( adviseType.equals("audio/mpeg") ) {
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
