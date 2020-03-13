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




}//end class avisos
