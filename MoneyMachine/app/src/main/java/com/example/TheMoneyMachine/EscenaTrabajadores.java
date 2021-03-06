package com.example.TheMoneyMachine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Clase que se encarga de dibujar la pantalla de trabajadores
 */
public class EscenaTrabajadores extends Escenas {
    /**
     * Cuadrado que representa el numero de trabajadores
     */
    Rect btnNumTrabajadores;
    /**
     * Cuadrado que representa la salud de los trabajadores
     */
    Rect btnSalud;
    /**
     * Cuadrado que representa la energía de los trabajadores
     */
    Rect btnEnergia;
    /**
     * Cuadrado que representa el botón de retroceso
     */
    Rect btnVolver;
    /**
     * Cuadrado que representa el contenedor
     */
    Rect btnContenedor;
    /**
     * Cuadrado que representa el botón de recargar energía
     */
    Rect btnSubirEnergia;
    /**
     * Cuadrado que representa el botón de ayuda
     */
    Rect btnAyuda;
    /**
     * Imagen del numero de trabajadores
     */
    Bitmap bitmapNumero;
    /**
     * Imagen de la salud de los trabajadores
     */
    Bitmap bitmapSalud;
    /**
     * Imagen de la energía de los trabajadores
     */
    Bitmap bitmapEnergia;
    /**
     * Imagen del botón de retroceso
     */
    Bitmap bitmapVolver;
    /**
     * Imagen del contenedor
     */
    Bitmap bitmapContenedor;
    /**
     * Imagen del botón de ayuda
     */
    Bitmap bitmapAyuda;
    /**
     * Imagen de la pantalla de ayuda
     */
    Bitmap bitmapBtnAyuda;
    /**
     * Indica si se debe mostrar la pantalla de ayuda
     */
    boolean ayuda;

    /**
     * Constructor de la clase
     * @param numEscena numero de escena actual
     * @param context contexto de la applicación
     * @param altoPantalla alto de la pantalla del dispositivo
     * @param anchoPantalla ancho de la pantalla del dispositivo
     */
    public EscenaTrabajadores(int numEscena, Context context, int altoPantalla, int anchoPantalla) {
        super(numEscena, context, altoPantalla, anchoPantalla);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.trabajadores);
        bitmapFondo = aux.createScaledBitmap(aux,anchoPantalla, altoPantalla,true);
        btnVolver = new Rect(0, 0, anchoPantalla/9,anchoPantalla/9);
        btnNumTrabajadores = new Rect(anchoPantalla/10, altoPantalla - altoPantalla/6, anchoPantalla/10 * 3, altoPantalla - altoPantalla/20);
        btnSalud = new Rect(anchoPantalla/10 * 4, altoPantalla - altoPantalla/6, anchoPantalla/10 * 6, altoPantalla - altoPantalla/20);
        btnEnergia = new Rect(anchoPantalla - anchoPantalla/10 * 3, altoPantalla - altoPantalla/6, anchoPantalla- anchoPantalla/10, altoPantalla - altoPantalla/20);
        btnContenedor = new Rect(anchoPantalla/13, altoPantalla - altoPantalla/5, anchoPantalla -anchoPantalla/13, altoPantalla - altoPantalla/25);
        btnSubirEnergia = new Rect(anchoPantalla - anchoPantalla/10 * 3, altoPantalla - altoPantalla/5 - altoPantalla/17, anchoPantalla- anchoPantalla/10, altoPantalla - altoPantalla/5);
        btnAyuda = new Rect(anchoPantalla - anchoPantalla/9, 0, anchoPantalla, anchoPantalla/9);
        //Imagen de los botones
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.num);
        bitmapNumero = aux.createScaledBitmap(aux, btnNumTrabajadores.width(), btnNumTrabajadores.height(), true);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.energia);
        bitmapEnergia = aux.createScaledBitmap(aux, btnEnergia.width(), btnEnergia.height(), true);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.salud);
        bitmapSalud = aux.createScaledBitmap(aux, btnSalud.width(), btnSalud.height(), true);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.izquierda);
        bitmapVolver = aux.createScaledBitmap(aux, btnVolver.width(), btnVolver.height(), true);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.contenedor);
        bitmapContenedor = aux.createScaledBitmap(aux, btnContenedor.width(), btnContenedor.height(), true);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.ayuda);
        bitmapBtnAyuda = aux.createScaledBitmap(aux, btnAyuda.width(), btnAyuda.height(), true);
        ayuda = false;
        if(!ingles){
            aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.ayudatrabajadores);
            bitmapAyuda = aux.createScaledBitmap(aux, anchoPantalla, altoPantalla,true);
        }//end if
        else {
            aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.helpworkers);
            bitmapAyuda = aux.createScaledBitmap(aux, anchoPantalla, altoPantalla,true);
        }//end else
    }//end constructor

    /**
     * Metodo que se utiliza para dibujar en el canvas.
     * @param c  Canvas en el que se va a dibujar
     */
    @Override
    public void dibujar(Canvas c) {
        c.drawBitmap(bitmapFondo, 0 ,0, pincelRec);
        c.drawRect(btnSubirEnergia, pincelRec);
        //Imagenes
        c.drawBitmap(bitmapContenedor, anchoPantalla/13, altoPantalla - altoPantalla/5, null);
        c.drawBitmap(bitmapNumero, anchoPantalla/10, altoPantalla - altoPantalla/6, null);
        c.drawBitmap(bitmapSalud, anchoPantalla/10 * 4, altoPantalla - altoPantalla/6, null);
        c.drawBitmap(bitmapEnergia, anchoPantalla - anchoPantalla/10 * 3, altoPantalla - altoPantalla/6, null);
        c.drawBitmap(bitmapVolver, 0, 0, null);
        c.drawBitmap(bitmapBtnAyuda, anchoPantalla - anchoPantalla/9, 0, null);
        c.drawText("" + trabajadores.numero, anchoPantalla/10 * 2, altoPantalla - altoPantalla/6, pincelTxt);
        c.drawText(trabajadores.energia + "%", anchoPantalla - anchoPantalla/10 * 2, altoPantalla - altoPantalla/6, pincelTxt);
        c.drawText(trabajadores.salud + "%", anchoPantalla/10 * 5, altoPantalla - altoPantalla/6, pincelTxt);
        c.drawText("" + rellenarEnergia, btnSubirEnergia.centerX(),btnSubirEnergia.centerY(), pincelTxt);
        if(ayuda) {
            c.drawBitmap(bitmapAyuda, 0, 0, null);
        }//end if
        super.dibujar(c);
    }//end method dibujar

    @Override
    public void actualizarFisica() {
        super.actualizarFisica();
    }

    /**
     * Metodo que se lanza cuando se produce una pulsación en la pantalla
     * @param event evento de la pulsación
     * @return devuelve el número de escena que se debe dibujar
     */
    @Override
    public int onTouchEvent(MotionEvent event)
    {
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
        if(btnSubirEnergia.contains(x, y)){
            if(money >= rellenarEnergia){
                trabajadores.energia += 10;
                money -= rellenarEnergia;
                if(trabajadores.energia > 100){
                    trabajadores.energia = 100;
                }//end if
                rellenarEnergia *= 3;
                editor.putInt("rellenarEnergia", rellenarEnergia);
                editor.putInt("energiaTrabajadores", trabajadores.energia);
                editor.putInt("money", money);
                editor.commit();
            }//end if
        }//end if
        return numEscena;
    }//end onTouchEvent
}//end class EscenaTrabajadores
