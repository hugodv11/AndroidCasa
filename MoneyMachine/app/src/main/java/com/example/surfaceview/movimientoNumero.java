package com.example.surfaceview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Calendar;

public class movimientoNumero {

    private int numero;
    private int alpha;
    private Point posicion;
    Paint pincel;
    long tMover=System.currentTimeMillis();
    int tickMover=50;
    long tAlfa=System.currentTimeMillis();
    int tickAlfa=100;
    boolean activo;


    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public Point getPosicion() {
        return posicion;
    }

    public void setPosicion(Point posicion) {
        this.posicion = posicion;
    }


    public movimientoNumero(int numero, int alpha, Point posicion) {
        this.numero = numero;
        this.alpha = alpha;
        this.posicion = posicion;
        this.pincel=new Paint();
        pincel.setColor(Color.RED);
        pincel.setTextSize(100);
        activo = true;
    }//end constructor


    public void movimiento() {
        while(activo) {
            if (System.currentTimeMillis() - tAlfa > tickAlfa) {
                if (alpha != 0) alpha -=5;
                else activo = false;

                tAlfa = System.currentTimeMillis();
            }//end if
            if (System.currentTimeMillis() - tMover > tickMover) {
                posicion.y -= 10;

                tMover = System.currentTimeMillis();
            }//end if
        }//end while
    }//end method movimiento

    public void dibuja(Canvas c0) {

        while(alpha > 0){
            c0.drawText(""+numero,posicion.x, posicion.y, pincel);
        }//end while


    }//end method dibujar



}//end class movimientoNumero
