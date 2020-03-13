package com.example.surfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.HashMap;

import javax.security.auth.login.LoginException;

public class EscenaOpciones extends Escenas {

    Rect btnVolver, btnBorrarDatos;

    Bitmap bitmapVolver;

    public EscenaOpciones(int numEscena, Context context, int altoPantalla, int anchoPantalla) {
        super(numEscena, context, altoPantalla, anchoPantalla);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.opciones);
        bitmapFondo = aux.createScaledBitmap(aux,anchoPantalla, altoPantalla,true);

        btnVolver = new Rect(anchoPantalla - anchoPantalla/9, 0, anchoPantalla, anchoPantalla/9);
        btnBorrarDatos = new Rect(100, altoPantalla - 300, 300, altoPantalla - 50);

        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.derecha);
        bitmapVolver = aux.createScaledBitmap(aux,btnVolver.width(), btnVolver.height(),true);
    }//end constructor


    @Override
    public void dibujar(Canvas c) {
        pincelRec.setColor(Color.RED);
        c.drawBitmap(bitmapFondo,0, 0, null);
        c.drawBitmap(bitmapVolver, anchoPantalla - btnVolver.width(), 0,null);
        c.drawRect(btnBorrarDatos, pincelRec);
    }//end dibujar


    @Override
    public int onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        if(btnVolver.contains(x, y)){
            return 1;
        }//end if
        if(btnBorrarDatos.contains(x, y)){
            //Aqui borramos datos, lo suyo seria preguntar si esta seguro con un fragment??
            editor.clear().commit();
            Log.i("DInero preference", "Dinero : " + preferences.getInt("money", 0));
            Log.i("DInero Variable local", "Dinero : " + money);
        }//end if
        return numEscena;
    }//end onTouchEvent
}//end class escena
