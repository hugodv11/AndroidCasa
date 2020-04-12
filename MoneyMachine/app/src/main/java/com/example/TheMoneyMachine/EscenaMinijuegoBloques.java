package com.example.TheMoneyMachine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

public class EscenaMinijuegoBloques extends Escenas {

    public ArrayList<bloques> coleccionBloques;
    public ArrayList<Bitmap> coleccionColores;
    public jugador jugador;
    public pelota pelota;
    public Rect cuadroJugador;
    public Rect cuadroBloque;
    public Rect cuadroPelota;
    public Rect izquierda;
    public Rect derecha;
    public Rect cuadroVidas;
    public Bitmap bitmapBloques;
    public Bitmap bitmapJugador;
    public Bitmap bitmapPelota;
    public Bitmap bitmapVidas;
    public int velocidadJugador;
    public Random random;
    public int vidas;
    public int separacion;
    public pantallaAvisos aviso;
    public boolean activo;
    public boolean derrota;
    public boolean victoria;


    /**
     * Constructor de la clase
     *
     * @param numEscena     numero de escena actual
     * @param context       contexto de la aplicación
     * @param altoPantalla  alto de la pantalla del dispositivo
     * @param anchoPantalla ancho de la pantalla del dispositivo
     */
    public EscenaMinijuegoBloques(int numEscena, Context context, int altoPantalla, int anchoPantalla) {
        super(numEscena, context, altoPantalla, anchoPantalla);

        cuadroPelota = new Rect(0, 0, anchoPantalla/17,anchoPantalla/17);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.pelota);
        bitmapPelota = aux.createScaledBitmap(aux, cuadroPelota.width(), cuadroPelota.height(), true);

        izquierda = new Rect(0,0,anchoPantalla / 2, altoPantalla);
        derecha = new Rect(anchoPantalla/2,0, anchoPantalla , altoPantalla);

        cuadroVidas = new Rect(0, 0, anchoPantalla / 9, anchoPantalla / 9);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.pelota);
        bitmapVidas = aux.createScaledBitmap(aux, cuadroVidas.width(), cuadroVidas.height(), true);

        cuadroJugador = new Rect(0, 0, anchoPantalla / 5, (anchoPantalla/9) / 2);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.tabla);
        bitmapJugador = aux.createScaledBitmap(aux, cuadroJugador.width(), cuadroJugador.height(), true);

        jugador = new jugador(bitmapJugador, (anchoPantalla / 2) - (bitmapJugador.getWidth() / 2), altoPantalla - altoPantalla / 6);
        pelota = new pelota(bitmapPelota, (anchoPantalla / 2) - (bitmapPelota.getWidth() / 2), altoPantalla - altoPantalla / 6 - bitmapJugador.getHeight(), 0);
        coleccionBloques = new ArrayList<>();
        coleccionColores = new ArrayList<>();
        random = new Random();
        crearBloques();
        vidas = 3;
        separacion = 0;
        velocidadJugador = 0;
        aviso = new pantallaAvisos(altoPantalla, anchoPantalla, "", context, pincelFondo, pincelCuadro, pincelTexto);
        activo = false;
        derrota = false;
        victoria = false;
    }//end constructor

    @Override
    public void dibujar(Canvas c) {
        c.drawColor(Color.BLACK);
        dibujarVidas(c);
        c.drawBitmap(jugador.imagen, jugador.posicion.x, jugador.posicion.y, null);
        c.drawBitmap(pelota.imagen, pelota.posicion.x, pelota.posicion.y, null);
        for(int i = 0; i < coleccionBloques.size(); i++){
            c.drawBitmap(coleccionBloques.get(i).imagen, coleccionBloques.get(i).posicion.x, coleccionBloques.get(i).posicion.y, null);
            //c.drawRect(coleccionBloques.get(i).detectorColisiones, pincelRec);
        }//end for
        if(derrota){
            aviso.setTexto(context.getResources().getString(R.string.minijuegoBloquesDerrota));
            aviso.cuadroEstandar(c);
        }//end if
        if(victoria){
            aviso.setTexto(context.getResources().getString(R.string.minijuegoBloquesVictoria));
            aviso.cuadroEstandar(c);
        }//end if
        if(!activo){
            aviso.setTexto(context.getResources().getString(R.string.minijuegoBloques));
            aviso.cuadroEstandar(c);
        }//end if
    }//end method dibujar

    @Override
    public int onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        if(derrota){
            if(dineroPulsacion <= 20){
                dineroPulsacion  = 1;
            }//end if
            else {
                dineroPulsacion -= 20;
            }//end else
            editor.putInt("dineroPulsacion", dineroPulsacion).commit();
            return 1;
        }//end if
        if(victoria){
            return 1;
        }//end if
        if(!activo) {
            activo = true;
        }//end if
        if(izquierda.contains(x,y)){
            velocidadJugador = -13;
            if(pelota.velocidad == 0){
                pelota.velocidad = 17;
                pelota.direccionX = -1;
            }//end if
        }//end if

        if(derecha.contains(x,y)){
            velocidadJugador = 13;
            if(pelota.velocidad == 0){
                pelota.velocidad = 17;
                pelota.direccionX = 1;
            }//end if
        }//end if
        return numEscena;
    }//end onTouchEvent

    @Override
    public void actualizarFisica() {
        if(activo) {
            jugador.moverJugador(velocidadJugador);
            pelota.rebotes();
            if (pelota.detectorColisiones.intersect(jugador.detectorColisiones)) {
                pelota.posicion.y = altoPantalla - altoPantalla / 6 - bitmapJugador.getHeight();
                pelota.direccionY *= -1;
            }//end if
            for (int i = 0; i < coleccionBloques.size(); i++) {
                if (coleccionBloques.get(i).detectorColisiones.intersect(pelota.detectorColisiones)) {
                    coleccionBloques.remove(coleccionBloques.get(i));
                    pelota.direccionY *= -1;
                }//end if
            }//end for
            if(coleccionBloques.size() == 0){
                victoria = true;
                jugador.moverJugador(0);
                pelota.velocidad = 0;
            }//end if
        }//end if
        if(vidas == 0){
            derrota = true;
        }//end if
    }//end method actualizarFisica


    public void dibujarVidas(Canvas c){
        for(int i = 0; i < vidas; i++){
            c.drawBitmap(bitmapVidas, anchoPantalla / 3 + separacion, 0, null);
            separacion += bitmapVidas.getWidth() + anchoPantalla / 12;
        }//end for
        separacion = 0;
    }//end method dibujarVidas;

    public class jugador{
        public Rect detectorColisiones;
        public Bitmap imagen;
        public PointF posicion;

        public jugador(Bitmap imagen, float x, float y) {
            this.imagen = imagen;
            this.posicion = new PointF(x, y);
            setRectangulos();
        }//end constructor

        public void setRectangulos(){
            detectorColisiones = new Rect((int)posicion.x, (int)posicion.y, (int)posicion.x + imagen.getWidth(), (int)posicion.y + imagen.getHeight());
        }//end setRectangulos
        public void moverJugador(int velocidad){
            posicion.x += velocidad;
            setRectangulos();
            if(posicion.x < 0){
                posicion.x = 0;
            }//end if
            if((posicion.x + imagen.getWidth()) > anchoPantalla){
                posicion.x = anchoPantalla - imagen.getWidth();
            }//end if
        }//end method moverJugador

    }//end class jugador

    public class bloques{
        public Rect detectorColisiones;
        public Bitmap imagen;
        public PointF posicion;

        public bloques(Bitmap imagen, float x ,float y) {
            this.imagen = imagen;
            this.posicion = new PointF(x, y);
        }//end constructor

        //detectorColisiones = new Rect(0, 0, anchoPantalla/8,(anchoPantalla/9) / 2);
    }//end class bloques

    public class pelota{
        public Rect detectorColisiones;
        public Bitmap imagen;
        public PointF posicion;
        public int velocidad;
        public int direccionX;
        public int direccionY;

        public pelota(Bitmap imagen, float x, float y, int velocidad) {
            this.imagen = imagen;
            this.posicion = new PointF(x, y);
            setRectangulos();
            this.velocidad = velocidad;
            direccionX = 1;
            direccionY = -1;
        }//end constructor

        public void setRectangulos(){
            detectorColisiones = new Rect((int)posicion.x, (int)posicion.y, (int)posicion.x + imagen.getWidth(), (int)posicion.y + imagen.getHeight());
        }//end setRectangulos

        public void rebotes(){
            posicion.x += velocidad * direccionX;
            posicion.y += velocidad * direccionY;
            setRectangulos();
            if(posicion.x < 0 || posicion.x > (anchoPantalla - bitmapPelota.getWidth())){
                direccionX *= -1;
            }//end if
            if(posicion.y < 0){
                direccionY *= -1;
            }//end if
            if( posicion.y > (altoPantalla - bitmapPelota.getHeight())){
                vidas--;
                jugador.posicion.x = (anchoPantalla / 2) - (bitmapJugador.getWidth() / 2);
                velocidadJugador = 0;
                pelota.posicion.x = (anchoPantalla / 2) - (bitmapPelota.getWidth() / 2);
                pelota.posicion.y = altoPantalla - altoPantalla / 6 - bitmapJugador.getHeight();
                pelota.velocidad = 0;
            }//end if
        }//end method rebotes

    }//end class pelota

    public void crearBloques(){
        int espacio = anchoPantalla - ((anchoPantalla / 7) * 2);
        int  tamaño = espacio / 5;
        cuadroBloque = new Rect(0, 0, tamaño,(anchoPantalla/9) / 2);
        crearColores();
        int separacionX = 0;
        int separacionY = 0;
        bloques bloque;
        for(int i = 0; i < 5; i++){
            for(int e  = 0; e < 5; e++){
                bitmapBloques = coleccionColores.get(random.nextInt(5));
                bloque = new bloques(bitmapBloques, anchoPantalla / 7 + separacionX, altoPantalla / 6 + separacionY);
                bloque.detectorColisiones = new Rect(anchoPantalla / 7 + separacionX , altoPantalla / 6 + separacionY, anchoPantalla / 7 + separacionX + tamaño, altoPantalla / 6 + separacionY + (anchoPantalla/9) / 2);
                coleccionBloques.add(bloque);
                separacionX += bitmapBloques.getWidth();
            }//end for
            separacionX = 0;
            separacionY += bitmapBloques.getHeight();
        }//end for
    }//end method crearBloques

    public void crearColores(){
        Bitmap bitmap;
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.bloqueazul);
        bitmap = aux.createScaledBitmap(aux, cuadroBloque.width(), cuadroBloque.height(), true);
        coleccionColores.add(bitmap);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.bloqueamarillo);
        bitmap = aux.createScaledBitmap(aux, cuadroBloque.width(), cuadroBloque.height(), true);
        coleccionColores.add(bitmap);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.bloqueazul2);
        bitmap = aux.createScaledBitmap(aux, cuadroBloque.width(), cuadroBloque.height(), true);
        coleccionColores.add(bitmap);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.bloquenaranja);
        bitmap = aux.createScaledBitmap(aux, cuadroBloque.width(), cuadroBloque.height(), true);
        coleccionColores.add(bitmap);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.bloquerojo);
        bitmap = aux.createScaledBitmap(aux, cuadroBloque.width(), cuadroBloque.height(), true);
        coleccionColores.add(bitmap);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.bloqueverde);
        bitmap = aux.createScaledBitmap(aux, cuadroBloque.width(), cuadroBloque.height(), true);
        coleccionColores.add(bitmap);
    }//end method crear colores

}//end class EscenaMinijuegoBloques
