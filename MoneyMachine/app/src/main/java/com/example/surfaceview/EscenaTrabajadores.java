package com.example.surfaceview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.HashMap;

public class EscenaTrabajadores extends Escenas {

    Rect indicador1, indicador2, indicador3, btnVolver;
    Rect btnMas, btnMenos;
    int numPosX, numPosY, enerPosX, enerPosY, vitPosX, vitPosY;

    //Constructor
    public EscenaTrabajadores(int numEscena, Context context, int altoPantalla, int anchoPantalla) {
        super(numEscena, context, altoPantalla, anchoPantalla);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.back);
        bitmapFondo = aux.createScaledBitmap(aux,anchoPantalla, altoPantalla,true);
        indicador1 = new Rect(anchoPantalla/10, altoPantalla - 400, anchoPantalla/10 * 3, altoPantalla -100);
        indicador2 = new Rect(anchoPantalla/10 * 4, altoPantalla - 400, anchoPantalla/10 * 6, altoPantalla -100);
        indicador3 = new Rect(anchoPantalla - anchoPantalla/10 * 3, altoPantalla - 400, anchoPantalla- anchoPantalla/10, altoPantalla -100);
        numPosX = indicador1.centerX();
        numPosY = indicador1.centerY();
        enerPosX = indicador2.centerX();
        enerPosY = indicador2.centerY();
        vitPosX = indicador3.centerX();
        vitPosY = indicador3.centerY();
        btnVolver = new Rect(0, 0, 100, 100);
        btnMenos = new Rect(70, 300, 270, 500);
        btnMas = new Rect(800, 300, 1000, 500);

    }//end constructor

    @Override
    public void dibujar(Canvas c) {

        c.drawBitmap(bitmapFondo, 0 ,0, pincelRec);
        c.drawRect(indicador1, pincelRec);
        c.drawRect(indicador2, pincelRec);
        c.drawRect(indicador3, pincelRec);
        c.drawRect(btnVolver, pincelRec);
        c.drawRect(btnMas, pincelRec);
        c.drawRect(btnMenos, pincelRec);
        //c.drawText("" + numeroTbj, anchoPantalla/ 10 * 2, altoPantalla - 200, pincelTxt);
        c.drawText("" + trabajadores.numero, numPosX, numPosY + 20, pincelTxt);
        c.drawText(trabajadores.energia + "%", enerPosX, enerPosY + 20, pincelTxt);
        c.drawText(trabajadores.salud + "%", vitPosX, vitPosY + 20, pincelTxt);
        c.drawText(trabajadores.salario + "",550, 425, pincelTxt);
        super.dibujar(c);
    }

    @Override
    public int onTouchEvent(MotionEvent event)
    {
        int x = (int)event.getX();
        int y = (int)event.getY();
        if(btnVolver.contains(x, y)) {
            return 1;
        }//end if
        if(btnMas.contains(x, y)) {
            trabajadores.salario += 100;
            editor.putInt("salarioTbj", trabajadores.salario).commit();
        }//end if
        if(btnMenos.contains(x, y)) {
            trabajadores.salario -= 100;
            editor.putInt("salarioTbj", trabajadores.salario).commit();
        }//end if
        return numEscena;
    }//end onTouchEvent



}//end class EscenaTrabajadores
