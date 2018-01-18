package modelo;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.Format;

import eus.ehu.ejemploandroidtta.ejemploandroidtta.TestActivity;

/**
 * Created by tta on 1/14/18.
 */

public class School {

    private RestClient rest;
    private Data data = Data.getInstance();

    public School(String baseUrl){
        rest = new RestClient(baseUrl);

    }

    public Test getTest() throws JSONException, IOException {
        Test test = new Test();
        //RestClient rest = new RestClient("http://u017633.ehu.eus:28080/ServidorTta/rest/tta");
        rest.setHttpBsicAuth(data.getUser().getDni(),data.getUser().getPasswd());
        JSONObject json = rest.getJson("getTest?id=1");
        test.setWording(json.getString("wording"));
        JSONArray array = json.getJSONArray("choices");

        for( int i = 0; i < array.length(); i++ ) {
            JSONObject item = array.getJSONObject(i);
            Pregunta pregunta = new Pregunta();
            pregunta.setId(item.getInt("id"));
            pregunta.setEnunciado(item.getString("answer"));
            pregunta.setCorrecta(item.getBoolean("correct"));
            pregunta.setAyuda(item.optString("advise",null));
            if(item.optJSONObject("resourceType") != null)
                pregunta.setMime(item.optJSONObject("resourceType").getString("mime"));
            test.getPreguntas().add(pregunta);
        }
        return test;
    }

    public void putTest(Test test) throws JSONException { // Este método creo que no se usa
        JSONObject json = new JSONObject();
        json.put("wording", test.getWording());
        JSONArray array = new JSONArray();
        for( Pregunta pregunta : test.getPreguntas()) {
            JSONObject item = new JSONObject();
            item.put("id",pregunta.getId());
            item.put("wording",pregunta.getEnunciado());
            item.put("correct",pregunta.isCorrecta());
            item.put("advise", pregunta.getAyuda());
            item.put("mime",pregunta.getMime());
            array.put(item);
        }
        json.put("choices",array);
        //RestClient.putString():  no hay
    }

    public Exercise getExercise(int id) throws IOException, JSONException {
        rest.setHttpBsicAuth(data.getUser().getDni(),data.getUser().getPasswd());
        JSONObject json = rest.getJson(String.format("getExercise?id=%d",id));
        Exercise exercise = new Exercise();
        exercise.setId(json.getInt("id"));
        exercise.setWording(json.getString("wording"));
        return exercise;
    }

    public int uploadChoice(int userId, int choiceId) throws JSONException, IOException {
        JSONObject json = new JSONObject();
        json.put("userId",userId);
        json.put("choiceId", choiceId);
        rest.setHttpBsicAuth(data.getUser().getDni(),data.getUser().getPasswd());
        return rest.postJson(json,"postChoice");
    }

    public User getUser(String dni, String passwd) {
        rest.setHttpBsicAuth(dni,passwd);
        JSONObject json;
        try {
            json = rest.getJson(String.format("getStatus?dni=%s",dni));
            User user = new User();
            user.setId(json.getInt("id"));
            user.setName(json.getString("user"));
            user.setLessonNumber(json.getInt("lessonNumber"));
            user.setLessonTitle(json.getString("lessonTitle"));
            user.setNextTest(json.getInt("nextTest"));
            user.setNextExercise(json.getInt("nextExercise"));
            return user;
        } catch (IOException e) {
            return null; //Para que devuelva null y se autentique
        } catch (JSONException e) {
            return null; //Para que devuelva null y se autentique
        }


    }

    public int uploadFile(int userId, int exerciseId, InputStream is, String fileName) throws IOException {
        rest.setHttpBsicAuth(data.getUser().getDni(),data.getUser().getPasswd());
        String path = String.format("postExercise?user=%d&id=%d",userId,exerciseId);
        return rest.postFile(path, is, fileName);
    }
}
