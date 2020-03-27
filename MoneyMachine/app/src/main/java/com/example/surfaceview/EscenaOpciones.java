package com.example.surfaceview;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.HashMap;
import java.util.Locale;

import javax.security.auth.login.LoginException;

/**
 * Clase que se encarga de dibujar la pantalla de opciones
 */
public class EscenaOpciones extends Escenas {

    Rect btnVolver, btnBorrarDatos, btnCreditos, btnIdioma, btnMutear;

    Bitmap bitmapVolver, bitmapBorrar, bitmapBtnCreditos, bitmapCreditos, bitmapBandera;

    boolean creditos, muted;

    MediaPlayer mediaPlayer;

    /**
     * Constructor de la clase
     * @param numEscena numero de escena actual
     * @param context contexto de la applicación
     * @param altoPantalla alto de la pantalla del dispositivo
     * @param anchoPantalla ancho de la pantalla del dispositivo
     * @param mediaPlayer objeto que manipula la musica del juego
     */
    public EscenaOpciones(int numEscena, Context context, int altoPantalla, int anchoPantalla, MediaPlayer mediaPlayer) {
        super(numEscena, context, altoPantalla, anchoPantalla);
        this.mediaPlayer = mediaPlayer;
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.mejoras);
        bitmapFondo = aux.createScaledBitmap(aux,anchoPantalla, altoPantalla,true);

        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.creditos);
        bitmapCreditos = aux.createScaledBitmap(aux,anchoPantalla, altoPantalla,true);

        btnCreditos = new Rect(0, 0, anchoPantalla/4, anchoPantalla/9);
        btnVolver = new Rect(anchoPantalla - anchoPantalla/9, 0, anchoPantalla, anchoPantalla/9);
        btnBorrarDatos = new Rect(anchoPantalla/10, altoPantalla - altoPantalla/6, anchoPantalla/10 * 3, altoPantalla - altoPantalla/20);
        btnIdioma = new Rect(anchoPantalla/10 * 4, altoPantalla - altoPantalla/6, anchoPantalla/10 * 8, altoPantalla - altoPantalla/20);
        btnMutear = new Rect(anchoPantalla/10, altoPantalla - altoPantalla/3, anchoPantalla/10 * 3, altoPantalla - altoPantalla/5);

        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.derecha);
        bitmapVolver = aux.createScaledBitmap(aux,btnVolver.width(), btnVolver.height(),true);

        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.papelera);
        bitmapBorrar = aux.createScaledBitmap(aux, btnBorrarDatos.width(), btnBorrarDatos.height(),true);

        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.contenedor);
        bitmapBtnCreditos = aux.createScaledBitmap(aux, btnCreditos.width(), btnCreditos.height(),true);

        creditos = false;
        muted = false;
    }//end constructor

    /**
     * Metodo que se encarga de dibuja en el canvas
     * @param c  Canvas en el que se va a dibujar
     */
    @Override
    public void dibujar(Canvas c) {
        super.dibujar(c);
        pincelTxt.setTextSize(65);
        pincelRec.setColor(Color.RED);
        c.drawBitmap(bitmapFondo,0, 0, null);
        c.drawBitmap(bitmapBtnCreditos, 0, 0, null);
        c.drawBitmap(bitmapVolver, anchoPantalla - btnVolver.width(), 0,null);
        c.drawBitmap(bitmapBorrar, anchoPantalla/10, altoPantalla - altoPantalla/6, null);
        if(ingles){
            aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.espanhol);
            bitmapBandera = aux.createScaledBitmap(aux, btnIdioma.width(), btnIdioma.height(),true);
            aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.credits);
            bitmapCreditos = aux.createScaledBitmap(aux, anchoPantalla, altoPantalla,true);
        }//end if
        else {
            aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.ingles);
            bitmapBandera = aux.createScaledBitmap(aux, btnIdioma.width(), btnIdioma.height(),true);
            aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.creditos);
            bitmapCreditos = aux.createScaledBitmap(aux, anchoPantalla, altoPantalla,true);
        }//end else
        c.drawBitmap(bitmapBandera,anchoPantalla/10 * 4, altoPantalla - altoPantalla/6, null);
        c.drawText(context.getResources().getString(R.string.creditos), btnCreditos.centerX(), btnCreditos.centerY(), pincelTxt);
        if(creditos){
            c.drawBitmap(bitmapCreditos, 0 ,0, null);
        }//end if
    }//end dibujar

    /**
     * Metodo que se lanza cuando se produce una pulsación en la pantalla
     * @param event evento de la pulsación
     * @return devuelve el número de escena que se debe dibujar
     */
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
            editor.clear().commit();
        }//end if
        if(btnIdioma.contains(x, y)){
            if(ingles){
                setAppLocale("es_ES");
                ingles = false;
            }//end if
            else {
                setAppLocale("en");
                ingles = true;
            }//end else
            editor.putBoolean("ingles", ingles).commit();
        }//end if
        if(btnMutear.contains(x, 7)){
            if(muted){
                muted = false;
            }
            else {
                muted = true;
            }//end else
        }//end if
        return numEscena;
    }//end onTouchEvent


    /**
     * Metodo que silencia la musica o, en el caso contrario, le da volumen
     * @param mediaPlayer objecto que modifica la musica del juego
     * @return devuelve el objecto mediaPlayer
     */
    public MediaPlayer cambiarSonido(MediaPlayer mediaPlayer){
        if(muted){
            mediaPlayer.setVolume(0,0);
            return mediaPlayer;
        }
        else{
            mediaPlayer.setVolume(1,1);
            return mediaPlayer;
        }//end else
    }//end method void

    @Override
    public void actualizarFisica() {
        super.actualizarFisica();
    }


}//end class escena
