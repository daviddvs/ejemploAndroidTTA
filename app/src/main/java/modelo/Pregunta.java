package modelo;

import java.util.List;

/**
 * Created by David Franco on 1/9/18.
 */

public class Pregunta {
    public String enunciado;
    public boolean correcta;
    public String tipoMIMEAyuda;
    public String recursoAyuda;

    public Pregunta(String enun, boolean corr, String tipo, String recurso){
        enunciado = enun;
        correcta = corr;
        tipoMIMEAyuda = tipo;
        recursoAyuda = recurso;
    }
}
