package com.example.surfaceview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
import android.util.Log;

//Clase que se utilizará para crear ventanas emergentes en la app
public class pantallaAvisos {

    private int altoPantalla, anchoPantalla;
    private String texto;
    private Context context;
    private Paint pincelFondo, pincelCuadro, pincelTexto;
    private Bitmap bitmapFondo, aux;
    private Rect cuadro;
    public Rect btnAceptar, btnCancelar;
    private String[] textos;


    public pantallaAvisos(int altoPantalla, int anchoPantalla, String texto, Context context, Paint pincelFondo, Paint pincelCuadro, Paint pincelTexto) {
        this.altoPantalla = altoPantalla;
        this.anchoPantalla = anchoPantalla;
        this.texto = texto;
        this.context = context;
        this.pincelFondo = pincelFondo;
        this.pincelCuadro = pincelCuadro;
        this.pincelTexto = pincelTexto;
        cuadro = new Rect(anchoPantalla/6, altoPantalla/3, anchoPantalla - anchoPantalla/6, altoPantalla/3 * 2);
        btnAceptar = new Rect(anchoPantalla/6, (altoPantalla/3 * 2) - altoPantalla/15, anchoPantalla/6 * 3, altoPantalla/3 * 2);
        btnCancelar = new Rect(anchoPantalla/6 * 3, (altoPantalla/3 * 2) - altoPantalla/15, anchoPantalla - anchoPantalla/6, altoPantalla/3 * 2);
    }//end constructor


    public int getAltoPantalla() {
        return altoPantalla;
    }

    public void setAltoPantalla(int altoPantalla) {
        this.altoPantalla = altoPantalla;
    }

    public int getAnchoPantalla() {
        return anchoPantalla;
    }

    public void setAnchoPantalla(int anchoPantalla) {
        this.anchoPantalla = anchoPantalla;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) { this.texto = texto; }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Paint getPincelFondo() {
        return pincelFondo;
    }

    public void setPincelFondo(Paint pincelFondo) {
        this.pincelFondo = pincelFondo;
    }

    public Paint getPincelCuadro() {
        return pincelCuadro;
    }

    public void setPincelCuadro(Paint pincelCuadro) {
        this.pincelCuadro = pincelCuadro;
    }

    public Paint getPincelTexto() {
        return pincelTexto;
    }

    public void setPincelTexto(Paint pincelTexto) {
        this.pincelTexto = pincelTexto;
    }

    public Bitmap getBitmapFondo() {
        return bitmapFondo;
    }

    public void setBitmapFondo(Bitmap bitmapFondo) {
        this.bitmapFondo = bitmapFondo;
    }


    //El bitmap fondo y auxiliar sirven para poder poner la imagen que va a aparecer con transpariencia
    //Necesario que el texto no se salga del cuadro(no creo que lo haga de forma correcta, probare cuantas letras
    //entran mas o menos cuando cambie la fuente, y añadire un salto de linea y pa diante)
    //Todos los colores se pueden cambiar con los pinceles

    //METODOS
    //Metodo que dibuja un cuadro de dialogo informativo, sin botones y sin ser interactuable
    public void cuadroEstandar(Canvas c){
        aux = BitmapFactory.decodeResource(context.getResources(), R.drawable.mejoras);
        bitmapFondo = aux.createScaledBitmap(aux,anchoPantalla, altoPantalla,true);

        //Zona de dibujado
        c.drawBitmap(bitmapFondo, 0, 0, pincelFondo);
        c.drawRect(cuadro, pincelCuadro);
        c.drawText(texto, anchoPantalla/2, altoPantalla/2, pincelTexto);
    }//end method cuadroEstandar


    //Metodo que dibuja un cuadro de dialogo informativo, con botones e interactuable
    public void cuadroBotones(Canvas c){
        aux = BitmapFactory.decodeResource(context.getResources(), R.drawable.mejoras);
        bitmapFondo = aux.createScaledBitmap(aux,anchoPantalla, altoPantalla,true);
        this.textos = texto.split("\\.");
        //Zona de dibujado
        c.drawBitmap(bitmapFondo, 0, 0, pincelFondo);
        c.drawRect(cuadro, pincelCuadro);
        int posY = altoPantalla / 3 ;
        for(int i = 0; i < textos.length; i++) {
            posY += altoPantalla/20;
            c.drawText(textos[i], cuadro.centerX(), posY , pincelTexto);
        }//end for
        //Botones
        pincelCuadro.setColor(Color.RED);
        c.drawRect(btnAceptar, pincelCuadro);
        c.drawRect(btnCancelar, pincelCuadro);
        int aceptCenterX = btnAceptar.centerX();
        int aceptCenterY = btnAceptar.centerY();
        int cancelCenterX = btnCancelar.centerX();
        int cancelCenterY = btnCancelar.centerY();
        c.drawText(context.getResources().getString(R.string.btnAceptar), aceptCenterX, aceptCenterY, pincelTexto);
        c.drawText(context.getResources().getString(R.string.btnCancelar), cancelCenterX, cancelCenterY, pincelTexto);

    }//end method cuadroBotones


}//end class PantallaAvisos
