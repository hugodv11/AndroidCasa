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



    public EscenaOpciones(int numEscena, Context context, int altoPantalla, int anchoPantalla) {
        super(numEscena, context, altoPantalla, anchoPantalla);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.opciones);
        bitmapFondo = aux.createScaledBitmap(aux,anchoPantalla, altoPantalla,true);
        btnVolver = new Rect(anchoPantalla - 100, 0, anchoPantalla, 100);
        btnBorrarDatos = new Rect(100, altoPantalla - 300, 300, altoPantalla - 50);
    }//end constructor


    @Override
    public void dibujar(Canvas c) {
        pincelRec.setColor(Color.RED);
        c.drawBitmap(bitmapFondo,0, 0, null);
        c.drawRect(btnVolver, pincelRec);
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
