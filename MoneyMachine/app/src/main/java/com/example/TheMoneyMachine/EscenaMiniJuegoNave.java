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
import java.util.Timer;
import java.util.TimerTask;

public class EscenaMiniJuegoNave extends Escenas {

    public Bitmap bitmapScroll;
    public Scroll[] scrolls;
    public Rect izquierda;
    public Rect derecha;
    public Rect cuadroJugador;
    public Rect cuadroAsteroide;
    public Rect cuadroVidas;
    public jugador jugador;
    public Bitmap bitmapJugador;
    public Bitmap bitmapEnemigo;
    public Bitmap bitmapVidas;
    public int velocidad;
    public ArrayList<enemigos> coleccionEnemigos = new ArrayList<>();
    public enemigos enemigo;
    public Timer timer;
    public int vidas;
    public int separacion;
    public pantallaAvisos aviso;
    public boolean activo;
    public boolean derrota;
    public boolean victoria;
    public int tiempo;



     /**
     * Constructor de la clase
     *
     * @param numEscena     numero de escena actual
     * @param context       contexto de la aplicación
     * @param altoPantalla  alto de la pantalla del dispositivo
     * @param anchoPantalla ancho de la pantalla del dispositivo
     */
    public EscenaMiniJuegoNave(int numEscena, Context context, int altoPantalla, int anchoPantalla) {
        super(numEscena, context, altoPantalla, anchoPantalla);
        izquierda = new Rect(0,0,anchoPantalla / 2, altoPantalla);
        derecha = new Rect(anchoPantalla/2,0, anchoPantalla , altoPantalla);

        cuadroAsteroide = new Rect(0, 0, anchoPantalla / 6, anchoPantalla / 6);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.asteroid1);
        bitmapEnemigo = aux.createScaledBitmap(aux, cuadroAsteroide.width(), cuadroAsteroide.height(), true);

        cuadroJugador = new Rect(anchoPantalla / 2 - anchoPantalla / 8,(altoPantalla/5) * 4,anchoPantalla / 2 + anchoPantalla / 8,altoPantalla - altoPantalla / 12);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.ship);
        bitmapJugador = aux.createScaledBitmap(aux, cuadroJugador.width(), cuadroJugador.height(), true);

        cuadroVidas = new Rect(0, 0, anchoPantalla / 9, anchoPantalla / 9);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.lives);
        bitmapVidas = aux.createScaledBitmap(aux, cuadroVidas.width(), cuadroVidas.height(), true);
        vidas = 3;
        separacion = 0;

        jugador = new jugador(bitmapJugador, anchoPantalla / 3, (altoPantalla/5) * 4);
        velocidad = 0;
        //gestión del scroll
        bitmapScroll = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondoscroll);
        bitmapScroll = Bitmap.createScaledBitmap(bitmapScroll, anchoPantalla, altoPantalla, true);
        scrolls = new Scroll[2];
        scrolls[0] = new Scroll(bitmapScroll, altoPantalla);
        scrolls[1] = new Scroll(bitmapScroll, 0, scrolls[0].posicion.y - bitmapScroll.getHeight());
        aviso = new pantallaAvisos(altoPantalla, anchoPantalla, "", context, pincelFondo, pincelCuadro, pincelTexto);
        activo = false;
        derrota = false;
        victoria = false;
        tiempo = 0;

        //Timer que genera Los enemigos
        TimerTask timerTask = new TimerTask() {
            //Dentro de run es donde se pone todo el codigo
            public void run() {
                //Codigo del timer
                if(activo) {
                    if(tiempo <= 30) {
                        enemigo = new enemigos(bitmapEnemigo, 0);
                        coleccionEnemigos.add(enemigo);
                        tiempo++;
                    }//end if
                }//end if
            }//end run
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 1200);

    }//end constructor

    @Override
    public void dibujar(Canvas c) {
        c.drawBitmap(scrolls[0].imagen,scrolls[0].posicion.x,scrolls[0].posicion.y,null);
        c.drawBitmap(scrolls[1].imagen,scrolls[1].posicion.x,scrolls[1].posicion.y,null);
        dibujarVidas(c);
        c.drawBitmap(jugador.imagen, jugador.posicion.x, jugador.posicion.y, null);
        for(int i = 0; i < coleccionEnemigos.size(); i++) {
            c.drawBitmap(coleccionEnemigos.get(i).imagen, coleccionEnemigos.get(i).posicion.x, coleccionEnemigos.get(i).posicion.y, null);
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
            aviso.setTexto(context.getResources().getString(R.string.minijuegoNave));
            aviso.cuadroEstandar(c);
        }//end if
    }//end dibujar

    @Override
    public void actualizarFisica() {
        //Gestión del scroll
        if(activo) {
            scrolls[0].mover(10);
            scrolls[1].mover(10);
        }//end if
        //Comprobamos que se sobrepase la pantalla y reiniciamos
        if(scrolls[0].posicion.y > altoPantalla){
            scrolls[0].posicion.y = scrolls[1].posicion.y - scrolls[0].imagen.getHeight();
        }//end if
        if(scrolls[1].posicion.y > altoPantalla){
            scrolls[1].posicion.y = scrolls[0].posicion.y - scrolls[1].imagen.getHeight();
        }//end if
        //Movemos jugador y comprobamos que no se salga de la pantalla
        jugador.moverJugador(velocidad);
        //Movemos los enemigos y borramos los que salen de la pantalla o los que
        //Colisionan con el jugador
        for(int i = 0; i < coleccionEnemigos.size(); i++) {
            coleccionEnemigos.get(i).moverEnemigo(30);
            if(coleccionEnemigos.get(i).posicion.y >= altoPantalla){
                coleccionEnemigos.remove(coleccionEnemigos.get(i));
            }//end if
            if(coleccionEnemigos.get(i).detectorColision.intersect(jugador.detectorColision)){
                coleccionEnemigos.remove(coleccionEnemigos.get(i));
                vidas -= 1;
            }//end if
        }//end for
        if(tiempo >= 30){
            victoria = true;
        }//end if
        if(vidas == 0){
            derrota = true;
        }//end if
    }//end actualizarFisica

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
        if(!activo){
            activo = true;
        }//end if
        //Si el jugador toca el lado izquierdo de la pantalla
        //cambiamos la velocidad en x del jugador para que se
        //mueva hacia alli
        if(izquierda.contains(x,y)){
            velocidad = -20;
        }//end if

        if(derecha.contains(x,y)){
            velocidad = 20;
        }//end if
        return numEscena;
    }//end onTouchEvent

    public void dibujarVidas(Canvas c){
        for(int i = 0; i < vidas; i++){
            c.drawBitmap(bitmapVidas, anchoPantalla / 3 + separacion, 0, null);
            separacion += bitmapVidas.getWidth() + anchoPantalla / 12;
        }//end for
        separacion = 0;
    }//end method dibujarVidas;

    public class jugador{
        public Bitmap imagen;
        public PointF posicion;
        public Rect detectorColision;

        public jugador(Bitmap imagen, float x, float y) {
            this.imagen = imagen;
            this.posicion = new PointF(x, y);
            setRectangulos();
        }//end constructor

        public void setRectangulos(){
            detectorColision = new Rect((int)posicion.x, (int)posicion.y, (int)posicion.x + imagen.getWidth(), (int)posicion.y + imagen.getHeight());
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
    }//end clase jugador

    public class enemigos{
        public Bitmap imagen;
        public PointF posicion;
        public Rect detectorColision;
        public Random random;

        public enemigos(Bitmap imagen, float y) {
            this.imagen = imagen;
            random = new Random();
            float x = random.nextInt(anchoPantalla - imagen.getWidth());
            this.posicion = new PointF(x, y);
            setRectangulos();
        }//end constructor

        public void moverEnemigo(int velocidad){
            posicion.y += velocidad;
            setRectangulos();
        }//end method moverEnemigo

        public void setRectangulos(){
            detectorColision = new Rect((int)posicion.x, (int)posicion.y, (int)posicion.x + imagen.getWidth(), (int)posicion.y + imagen.getHeight());
        }//end setRectangulos
    }//end class enemigos

}//end class EscenaMinijuego


