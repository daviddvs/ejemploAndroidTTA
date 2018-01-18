package eus.ehu.ejemploandroidtta.ejemploandroidtta;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import modelo.Data;
import modelo.Exercise;
import modelo.ProgressTask;
import modelo.School;

public class ExerciseActivity extends AppCompatActivity {

    public static final int READ_REQUEST_CODE = 0;
    public static final int VIDEO_REQUEST_CODE = 1;
    public static final int AUDIO_REQUEST_CODE = 2;
    public static final int PICTURE_REQUEST_CODE = 3;

    private Uri pictureUri;
    private Exercise exercise;
    private School school = new School("http://u017633.ehu.eus:28080/ServidorTta/rest/tta");
    private InputStream is = null;
    private String fileName;
    private Data data = Data.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        exercise = data.getExercise();
        TextView wording = (TextView)findViewById(R.id.exercise_wording);
        wording.setText(exercise.getWording());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
            return;
        switch(requestCode) {
            case READ_REQUEST_CODE:
            case VIDEO_REQUEST_CODE:
            case AUDIO_REQUEST_CODE:
                sendFile(data.getData());//Enviar filename del archivo cuando se haya grabado
                break;
            case PICTURE_REQUEST_CODE:
                sendFile(pictureUri);//Enviar uri
                break;
        }
    }

    public void sendFile(Uri uri) {
        if(uri.toString().contains("file:")) {
            //Foto
            String[] splits = uri.toString().split("/");
            fileName =  splits[splits.length-1];
            try {
                is = new FileInputStream(uri.toString().substring(7));//se quita el "file:"
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                is = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);
            if(cursor != null && cursor.moveToFirst()){
                fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
            if(cursor != null){
                cursor.close();
            }
        }

        new ProgressTask<Integer>(this){
            @Override
            protected Integer work() throws Exception{
                return school.uploadFile(data.getUser().getId(), exercise.getId(), is, fileName);
            }
            @Override
            protected void onFinish(Integer result){
                Toast.makeText(context, "Status code: "+result.toString(), Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    public void uploadFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");//Mime type
        startActivityForResult(intent,READ_REQUEST_CODE);
    }

    public void sendPicture(View view) {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
            Toast.makeText(this, R.string.no_camera, Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager()) != null) {
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                try {
                    File file = File.createTempFile("tta", ".jpg", dir);
                    pictureUri = Uri.fromFile(file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                    startActivityForResult(intent, PICTURE_REQUEST_CODE);
                } catch (IOException e) {
                }
            }
            else
                Toast.makeText(this, R.string.no_app, Toast.LENGTH_SHORT).show();
        }
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
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
            Toast.makeText(this, R.string.no_camera, Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if(intent.resolveActivity(getPackageManager()) != null)
               startActivityForResult(intent, VIDEO_REQUEST_CODE);
            else
                Toast.makeText(this, R.string.no_app, Toast.LENGTH_SHORT).show();
        }
    }
}
