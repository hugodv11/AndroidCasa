package com.example.TheMoneyMachine;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Clase que gestiona scrolls
 */
public class Scroll {
    /**
     * Posición de la imagen
     */
    public PointF posicion;
    /**
     * Imagen a la que se le va a aplicar el efecto scroll
     */
    public Bitmap imagen;

    /**
     * Constructor de la clase
     * @param imagen Imagen a representar
     * @param x posición en el eje x
     * @param y posicion en el eje y
     */
    public Scroll(Bitmap imagen, float x, float y) {
        this.posicion = new PointF(x,y);
        this.imagen = imagen;
    }//end constructor

    /**
     * Segundo constructor de la clase
     * @param imagen Imagen a representar
     * @param altoPantalla alto actual de la pantalla
     */
    public Scroll(Bitmap imagen, int altoPantalla) {
        this(imagen,0,altoPantalla-imagen.getHeight());
    }//end constructor

    /**
     * Metodo encargado de crear el efect scroll
     * @param velocidad velocidad de movimiento del scroll
     */
    public void mover(int velocidad){
        posicion.y += velocidad;
    }//end method velocidad

}//end class Scroll
