package eus.ehu.ejemploandroidtta.ejemploandroidtta;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ExerciseActivity extends AppCompatActivity {

    public static final int READ_REQUEST_CODE = 0;
    public static final int VIDEO_REQUEST_CODE = 1;
    public static final int AUDIO_REQUEST_CODE = 2;
    public static final int PICTURE_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
            return;
        switch(requestCode) {
            case READ_REQUEST_CODE:
            case VIDEO_REQUEST_CODE:
            case AUDIO_REQUEST_CODE:
                //sendFile(data.getData());//Enviar el archivo cuando se haya grabado
                break;
            case PICTURE_REQUEST_CODE:
                //sendFile(pictureURI)//Enviar uri
                break;

        }
    }

    public void sendFile(View view) {
        // De momento nada
    }

    public void takePhoto(View view) {
        // De momento nada
    }

    public void recordAudio(View view) {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            Toast.makeText(this, R.string.no_micro, Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            if (intent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(intent, AUDIO_REQUEST_CODE);
            else
                Toast.makeText(this,R.string.no_app, Toast.LENGTH_SHORT).show();
        }
    }

    public void recordVideo(View view) {
        // De momento nada
    }
}
