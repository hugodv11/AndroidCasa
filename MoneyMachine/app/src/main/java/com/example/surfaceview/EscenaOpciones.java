package com.example.surfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.HashMap;

import javax.security.auth.login.LoginException;

public class EscenaOpciones extends Escenas {

    Rect btnVolver, btnBorrarDatos, btnCreditos;

    Bitmap bitmapVolver, bitmapBorrar, bitmapBtnCreditos, bitmapCreditos;

    boolean creditos;

    public EscenaOpciones(int numEscena, Context context, int altoPantalla, int anchoPantalla) {
        super(numEscena, context, altoPantalla, anchoPantalla);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.mejoras);
        bitmapFondo = aux.createScaledBitmap(aux,anchoPantalla, altoPantalla,true);

        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.creditos);
        bitmapCreditos = aux.createScaledBitmap(aux,anchoPantalla, altoPantalla,true);

        btnCreditos = new Rect(0, 0, anchoPantalla/4, anchoPantalla/9);
        btnVolver = new Rect(anchoPantalla - anchoPantalla/9, 0, anchoPantalla, anchoPantalla/9);
        btnBorrarDatos = new Rect(anchoPantalla/10, altoPantalla - altoPantalla/6, anchoPantalla/10 * 3, altoPantalla - altoPantalla/20);

        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.derecha);
        bitmapVolver = aux.createScaledBitmap(aux,btnVolver.width(), btnVolver.height(),true);

        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.papelera);
        bitmapBorrar = aux.createScaledBitmap(aux, btnBorrarDatos.width(), btnBorrarDatos.height(),true);

        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.contenedor);
        bitmapBtnCreditos = aux.createScaledBitmap(aux, btnCreditos.width(), btnCreditos.height(),true);


        creditos = false;
    }//end constructor


    @Override
    public void dibujar(Canvas c) {
        pincelRec.setColor(Color.RED);
        c.drawBitmap(bitmapFondo,0, 0, null);
        c.drawBitmap(bitmapBtnCreditos, 0, 0, null);
        c.drawBitmap(bitmapVolver, anchoPantalla - btnVolver.width(), 0,null);
        c.drawBitmap(bitmapBorrar, anchoPantalla/10, altoPantalla - altoPantalla/6, null);
        c.drawText("Créditos", btnCreditos.centerX(), btnCreditos.centerY(), pincelTxt);

        if(creditos){
            c.drawBitmap(bitmapCreditos, 0 ,0, null);
        }//end if
    }//end dibujar


    @Override
    public int onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        if(creditos = true){
            creditos = false;
        }//end if
        if(btnVolver.contains(x, y)){
            return 1;
        }//end if
        if(btnCreditos.contains(x, y)){
            creditos = true;
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
