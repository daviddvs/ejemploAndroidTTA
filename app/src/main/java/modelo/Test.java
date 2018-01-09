package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Franco on 1/9/18.
 */

public class Test {
    List<Pregunta> preguntas;
    String enunciado;

    public Test(){
        preguntas = new ArrayList<Pregunta>();
    }

    public List<Pregunta> getTest(){

        Pregunta preg0 = new Pregunta("Option0",true,"","");//Ayuda solo las que estan mal
        Pregunta preg1 = new Pregunta("Option1",false,"text/html","Deberias estudiar mas");
        Pregunta preg2 = new Pregunta("Option2",false,"text/html","https://egela1718.ehu.eus");
        Pregunta preg3 = new Pregunta("Option3",false,"video","http://u017633.ehu.eus:28080/static/ServidorTta/AndroidManifest.mp4");
        Pregunta preg4 = new Pregunta("Option4",false,"audio","http://u017633.ehu.eus:28080/static/ServidorTta/AndroidManifest.mp4");
        preguntas.add(preg0);
        preguntas.add(preg1);
        preguntas.add(preg2);
        preguntas.add(preg3);
        preguntas.add(preg4);

        return preguntas;

    }
    public String getEnunciado(){
        enunciado = "Elige una opción";
        return enunciado;
    }

}
