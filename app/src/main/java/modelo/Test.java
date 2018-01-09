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

        Pregunta preg0=new Pregunta("Option0",true,"","");//Ayuda solo las que estan mal
        Pregunta preg1=new Pregunta("Option1",false,"text/html","Deberias estudiar mas");
        Pregunta preg2=new Pregunta("Option2",false,"text/html","Deberias estudiar mas");
        Pregunta preg3=new Pregunta("Option3",false,"text/html","Deberias estudiar mas");
        preguntas.add(preg0);
        preguntas.add(preg1);
        preguntas.add(preg2);
        preguntas.add(preg3);

        return preguntas;

    }
    public String getEnunciado(){
        enunciado = "Elige una opción";
        return enunciado;
    }

}
