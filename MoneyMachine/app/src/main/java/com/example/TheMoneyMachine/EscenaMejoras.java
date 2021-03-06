package com.example.TheMoneyMachine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Clase que se encarga de dibujar la escena de mejoras
 */
public class EscenaMejoras extends Escenas {

    Rect btnVolver, mejoraPulsación, mejoraAutoclick, mejoraTiempoAutoClick, btnAyuda;
    Bitmap bitmapPulsacion, bitmapAutoclick, bitmapCosteAutoClick, bitmapVolver, bitmapBtnAyuda, bitmapAyuda, bitmapFondo;
    int separacion;
    boolean ayuda;

    /**
     * Constructor de la clase
     * @param numEscena numero de escena actual
     * @param context contexto de la aplicación
     * @param altoPantalla alto de la pantalla del dispositivo
     * @param anchoPantalla ancho de la pantalla del dispositivo
     */
    public EscenaMejoras(int numEscena, Context context, int altoPantalla, int anchoPantalla) {
        super(numEscena, context, altoPantalla, anchoPantalla);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.mejoras);
        bitmapFondo = aux.createScaledBitmap(aux,anchoPantalla, altoPantalla,true);
        separacion = altoPantalla / 20;
        //Botones
        btnVolver = new Rect(0, 0, anchoPantalla/9,anchoPantalla/9);
        mejoraPulsación = new Rect(anchoPantalla/20, altoPantalla/10, anchoPantalla - anchoPantalla / 20    , (altoPantalla/10) * 2);
        mejoraTiempoAutoClick =new Rect(anchoPantalla/20, (altoPantalla / 10) * 3 - separacion, anchoPantalla - anchoPantalla / 20, (altoPantalla/10) * 4 - separacion);
        mejoraAutoclick = new Rect(anchoPantalla/20, (altoPantalla / 10) * 5 - (separacion * 2), anchoPantalla - anchoPantalla / 20, (altoPantalla/10) * 6 - (separacion * 2));
        btnAyuda = new Rect(anchoPantalla - anchoPantalla/9, 0, anchoPantalla, anchoPantalla/9);
        //Bitmaps
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.dollar);
        bitmapPulsacion = aux.createScaledBitmap(aux,mejoraPulsación.width()/4 , mejoraPulsación.height() - (mejoraPulsación.height() / 9),true);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.autoclick);
        bitmapAutoclick = aux.createScaledBitmap(aux,mejoraAutoclick.width()/4 , mejoraAutoclick.height() - (mejoraAutoclick.height() / 9),true);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.tiempo);
        bitmapCosteAutoClick = aux.createScaledBitmap(aux,mejoraTiempoAutoClick.width()/4 , mejoraTiempoAutoClick.height() - (mejoraTiempoAutoClick.height() / 9),true);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.izquierda);
        bitmapVolver = aux.createScaledBitmap(aux, btnVolver.width(),btnVolver.height(),true);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.ayuda);
        bitmapBtnAyuda = aux.createScaledBitmap(aux, btnAyuda.width(), btnAyuda.height(), true);
        ayuda = false;
        if(!ingles){
            aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.ayudamejoras);
            bitmapAyuda = aux.createScaledBitmap(aux, anchoPantalla, altoPantalla,true);
        }//end if
        else {
            aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.helpupgrades);
            bitmapAyuda = aux.createScaledBitmap(aux, anchoPantalla, altoPantalla,true);
        }//end else
    }//end constructor

    /**
     * Metodo que se utiliza para dibujar en el canvas.
     * @param c  Canvas en el que se va a dibujar
     */
    @Override
    public void dibujar(Canvas c) {
        c.drawBitmap(bitmapFondo, 0, 0, null);
        //dibujamos imagenes
        c.drawBitmap(bitmapVolver,0,    0,null);
        c.drawBitmap(bitmapPulsacion, anchoPantalla/20, altoPantalla/10, null);
        c.drawBitmap(bitmapAutoclick, anchoPantalla/20, (altoPantalla / 10) * 3 - separacion, null);
        c.drawBitmap(bitmapCosteAutoClick, anchoPantalla/20, (altoPantalla / 10) * 5 - (separacion * 2), null);
        c.drawBitmap(bitmapBtnAyuda, anchoPantalla - anchoPantalla/9, 0, null);
        pincelTxt.setColor(Color.WHITE);
        String s = context.getResources().getString(R.string.coste);
        c.drawText(s + " " + costoMejoraPulsacion, mejoraPulsación.centerX() + anchoPantalla / 8, mejoraPulsación.exactCenterY() + pincelTxt.getTextSize() / 4, pincelTxt);
        c.drawText(s + " " + costoMejoraAutoclick, mejoraAutoclick.centerX() + anchoPantalla / 8, mejoraAutoclick.exactCenterY() + pincelTxt.getTextSize() / 4, pincelTxt);
        c.drawText(s + " " + costoTiempoAutoclick, mejoraTiempoAutoClick.centerX() + anchoPantalla / 8, mejoraTiempoAutoClick.exactCenterY() + pincelTxt.getTextSize() / 4, pincelTxt);
        if(ayuda) {
            c.drawBitmap(bitmapAyuda, 0, 0, null);
        }//end if
        super.dibujar(c);
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
        if(btnVolver.contains(x, y)) {
            return 1;
        }//end if
        if(btnAyuda.contains(x, y)){
            ayuda = true;
        }//end if
        else {
            ayuda = false;
        }//end else
        if(mejoraPulsación.contains(x, y)) {
            if(money >= costoMejoraPulsacion){
                dineroPulsacion += 7;
                money -= costoMejoraPulsacion;
                costoMejoraPulsacion *= 2;
                editor.putInt("money", money);
                editor.putInt("dineroPulsacion", dineroPulsacion);
                editor.putInt("costoMejoraPulsacion", costoMejoraPulsacion);
                editor.commit();
            }//end if
        }//end if
        if(mejoraAutoclick.contains(x, y)) {
            if(money >= costoMejoraAutoclick){
                autoclick += 6;
                autoclick *= 3;
                money -= costoMejoraAutoclick;
                costoMejoraAutoclick *= 4;
                editor.putInt("money", money);
                editor.putInt("autoclick", autoclick);
                editor.putInt("costoMejoraAutoclick", costoMejoraAutoclick);
                editor.commit();
            }//end if
        }//end if
        if(mejoraTiempoAutoClick.contains(x, y)){
            if(money >= costoTiempoAutoclick) {
                tiempoAutoclick -= 250;
                if(tiempoAutoclick > 500){
                    tiempoAutoclick = 500;
                }//end if
                money -= costoTiempoAutoclick;
                costoTiempoAutoclick *= 5;
                editor.putInt("money", money);
                editor.putInt("tiempoAutoClick", tiempoAutoclick);
                editor.putInt("costoTiempoAutoClick", costoTiempoAutoclick);
                editor.commit();
            }//end if
        }//end if
        return numEscena;
    }//end onTouchEvent
}//end class EscenaMejoras
