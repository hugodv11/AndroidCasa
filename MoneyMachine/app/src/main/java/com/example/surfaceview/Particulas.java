package com.example.surfaceview;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

/**
 * Clase que se encarga de crear particulas
 */
public class Particulas {
    /**
     * posicion de la particula
     */
    public PointF posicion;
    /**
     * Imagen de la particula
     */
    public Bitmap imagen;
    /**
     * Transparencia de la imagen de la particula
     */
    public int alpha;
    /**
     * Pincel que se utiliza para pintar el bitmap(Imagen)
     */
    public Paint pincelImagen;
    /**
     * Indica si la particula es visible o no
     */
    public Boolean visible;
    /**
     * Bitmap con la imagen con las animaciones
     */
    private Bitmap imagenes;
    /**
     * ancho del bitmap imagenes
     */
    private int anchoImagenes;
    /**
     * alto del bitmap imagenes
     */
    private int altoImagenes;
    /**
     * indica que en que fila del bitmap imagenes nos encontramos
     * se utiliza para conseguir las distintas imagenes que contiene
     * el bitmap imagenes
     */
    private int fila = 0;
    /**
     * indica que en que columna del bitmap imagenes nos encontramos
     * se utiliza para conseguir las distintas imagenes que contiene
     * el bitmap imagenes
     */
    private int col = 0;

    /**
     * Constructor de la clase
     * @param imagenes bitmap con la imagen que contiene la animación
     * @param x posición del eje x
     * @param y posicion del eje y
     */
    public Particulas(Bitmap imagenes, float x, float y) {
        alpha = 255;
        pincelImagen = new Paint();
        pincelImagen.setAlpha(alpha);
        visible = true;
        this.imagenes = imagenes;
        anchoImagenes = imagenes.getWidth();
        altoImagenes = imagenes.getHeight();
        this.imagen = Bitmap.createBitmap(imagenes, 0, altoImagenes/3, anchoImagenes/4, altoImagenes/3);
        this.posicion = new PointF(x, y);
    }//end constructor

    //Establece el movimiento de un enemigo en una pantalla definida por alto y ancho y cierta velocidad

    /**
     * Establece el movimiento de la particula en una pantalla definida por un alto
     * y un ancho y cierta velocidad
     * @param velocidad Velocidad con la que se mueve la particula
     * @param velocidadTransparencia Velocidad con la que se desvanece la particula
     */
    public void moverParticula(int velocidad, int velocidadTransparencia){
        posicion.y -= velocidad;
        alpha -= velocidadTransparencia;
        pincelImagen.setAlpha(alpha);
    }//end method moverParticula

    /**
     * Se encarga de cambiar la imagen de la particula para recrear la
     * animación
     */
    public void actualizarImagen(){
        int posIniY = col * altoImagenes/3;
        int posIniX = fila * anchoImagenes/4;
        this.imagen = Bitmap.createBitmap(imagenes, posIniX, posIniY, anchoImagenes / 4, altoImagenes / 3);
        fila++;
        if(fila > 3){
            col++;
            fila = 0;
            if(col > 2){
                col = 0;
            }//end if
        }//end if
    }//end method actualizarImagen
}//end class Particulas
