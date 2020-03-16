package com.example.surfaceview;

import android.content.Context;
import android.os.Environment;

public class avisos {


    private boolean hayAviso, respuesta;
    pantallaAvisos cuadroAviso, cuadroBotones;
    private int numAviso;


    public avisos(pantallaAvisos cuadroAviso, pantallaAvisos cuadroBotones) {
        this.cuadroAviso = cuadroAviso;
        this.cuadroBotones = cuadroBotones;
        this.hayAviso = false;
        this.respuesta = false;
        this.numAviso = 0;
    }//end contructor


    public boolean isHayAviso() {
        return hayAviso;
    }

    public void setHayAviso(boolean hayAviso) {
        this.hayAviso = hayAviso;
    }

    public boolean isRespuesta() {
        return respuesta;
    }

    public void setRespuesta(boolean respuesta) {
        this.respuesta = respuesta;
    }

    public int getNumAviso() {
        return numAviso;
    }

    public void setNumAviso(int numAviso) {
        this.numAviso = numAviso;
    }



    //Metodo que tendra unas respuestas dependiendo
    //del evento activado
    public String respuesta(boolean caso){
        switch (getNumAviso()){
            case 1:
                if(caso){
                    return "Tus trabajadores estan más contentos!!!";
                }//end if
                else{
                    return "Tus trabajadores se desaniman un poco!!!";
                }//end else



            default:
                return "Locooo";

        }//end switch
    }//end method respuesta


}//end class avisos
