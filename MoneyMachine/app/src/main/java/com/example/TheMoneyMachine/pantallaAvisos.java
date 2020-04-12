package com.example.TheMoneyMachine;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Clase que se utiliza para crear ventanas emergentes en la app
 */
public class pantallaAvisos {
    /**
     * Alto de pantalla del dispositivo
     */
    private int altoPantalla;
    /**
     * Ancho de pantalla del dispositivo
     */
    private int anchoPantalla;
    /**
     * Texto a mostrar
     */
    private String texto;
    /**
     * Contexto de la applicación
     */
    private Context context;
    /**
     * Pincel de la imagen de fondo
     */
    private Paint pincelFondo;
    /**
     * Pincel del cuadro
     */
    private Paint pincelCuadro;
    /**
     * Pincel del texto
     */
    private Paint pincelTexto;
    /**
     * Imagen de fondo
     */
    private Bitmap bitmapFondo;
    /**
     * Bitmap auxiliar utilizado para reescalar imagenes
     */
    private Bitmap aux;
    /**
     * Cuadrado que representa el cuadro
     */
    private Rect cuadro;
    /**
     * Cuadrado que representa el botón aceptar
     */
    public Rect btnAceptar;
    /**
     * Cuadrado que representa el botón cancelar
     */
    public  Rect btnCancelar;
    /**
     * Se utiliza para divir en textos mas pequeños la propiedad texto
     */
    private String[] textos;

    /**
     * set del texto
     * @param texto nuevo texto
     */
    public void setTexto(String texto) { this.texto = texto; }

    /**
     * Constuctor de la clase
     * @param altoPantalla alto de la pantalla del dispositivo
     * @param anchoPantalla ancho de la pantalla del dispositivo
     * @param texto //texto que se dibujara en la ventana
     * @param context contexto de la aplicación
     * @param pincelFondo pincel para el fondo
     * @param pincelCuadro pincel para el cuadro
     * @param pincelTexto pincel para el texto
     */
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


    //METODOS
    /**
     * Metodo que dibuja un cuadro de dialogo informativo, sin botones y sin ser interactuable
     * @param c canvas en el que se va a dibujar
     */
    public void cuadroEstandar(Canvas c){
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
    }//end method cuadroEstandar

    /**
     * Metodo que dibuja un cuadro de dialogo informativo, con botones e interactuable
     * @param c canvas en el que se va a dibujar
     */
    public void cuadroBotones(Canvas c){
        pincelCuadro.setColor(Color.BLACK);
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
