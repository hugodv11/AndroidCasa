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

/**
 * Clase que representa el minijuego de la nave
 */
public class EscenaMiniJuegoNave extends Escenas {
    /**
     * bitmap con efecto scroll
     */
    public Bitmap bitmapScroll;
    /**
     * Colección que se utiliza para pintar el scroll
     */
    public Scroll[] scrolls;
    /**
     * Cuadrado que representa la mitad izquierda del dispositivo
     */
    public Rect izquierda;
    /**
     * Cuadrado que representa la mitad derecha del dispositivo
     */
    public Rect derecha;
    /**
     * Cuadrado que representa al jugador
     */
    public Rect cuadroJugador;
    /**
     * Cuadrado que representa a cada asteroide
     */
    public Rect cuadroAsteroide;
    /**
     * Cuadrado que representa las vidas del jugador
     */
    public Rect cuadroVidas;
    /**
     * Objeto de la clase jugador
     */
    public jugador jugador;
    /**
     * Bitmap que representa al jugador
     */
    public Bitmap bitmapJugador;
    /**
     * Bitmap que representa a los enemigos
     */
    public Bitmap bitmapEnemigo;
    /**
     * Bitmap que representa las vidas
     */
    public Bitmap bitmapVidas;
    /**
     * Velocidad de movimiento
     */
    public int velocidad;
    /**
     * Colección de enemigos
     */
    public ArrayList<enemigos> coleccionEnemigos = new ArrayList<>();
    /**
     * Objeto de tipo enemigo
     */
    public enemigos enemigo;
    /**
     * Objeto Timer
     */
    public Timer timer;
    /**
     * Numero de vidas
     */
    public int vidas;
    /**
     * Objeto de la clase pantallaAvisos
     */
    public pantallaAvisos aviso;
    /**
     * Indica si el juego está activo
     */
    public boolean activo;
    /**
     * Indica si el jugador ha perdido
     */
    public boolean derrota;
    /**
     * Indica si el jugador a ganado
     */
    public boolean victoria;
    /**
     * Indica el tiempo que se tiene que aguantar para ganar
     */
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
        timer.scheduleAtFixedRate(timerTask, 0, 800);
    }//end constructor

    /**
     * Metodo que se encarga de dibuja en el canvas
     * @param c  Canvas en el que se va a dibujar
     */
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

    /**
     * Metodo que se encarga de actualizar el movimiento de los componentes
     */
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

    /**
     * Metodo que se lanza cuando se produce una pulsación en la pantalla
     * @param event evento de la pulsación
     * @return devuelve el número de escena que se debe dibujar
     */
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


    /**
     * Metodo encargado de dibujar las vidas restantes del jugador
     * @param c canvas donde se va a dibujar
     */
    public void dibujarVidas(Canvas c){
        int separacion = 0;
        int calculo =  (anchoPantalla / 3) / 3;
        for(int i = 0; i < vidas; i++){
            c.drawBitmap(bitmapVidas, anchoPantalla / 3 + separacion, 0, null);
            separacion += calculo;
        }//end for
    }//end method dibujarVidas;

    /**
     * Clase que representa al jugador
     */
    public class jugador{
        /**
         * Imagen que representa al jugador
         */
        public Bitmap imagen;
        /**
         * Posición del objeto en la pantalla
         */
        public PointF posicion;
        /**
         * Cuadrado encargado de detectar colisiones
         */
        public Rect detectorColision;

        /**
         * Constructor de la clase
         * @param imagen imagen que va a representar al jugador
         * @param x coordenada x
         * @param y coordenada y
         */
        public jugador(Bitmap imagen, float x, float y) {
            this.imagen = imagen;
            this.posicion = new PointF(x, y);
            setRectangulos();
        }//end constructor

        /**
         * Metodo que actualiza el detector de colisiones cada vez que el objeto se mueve
         */
        public void setRectangulos(){
            detectorColision = new Rect((int)posicion.x, (int)posicion.y, (int)posicion.x + imagen.getWidth(), (int)posicion.y + imagen.getHeight());
        }//end setRectangulos

        /**
         * Metodo encargado de mover al jugador por pantalla
         * @param velocidad velocidad de movimiento
         */
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

    /**
     * Clase que representa a los enemigos
     */
    public class enemigos{
        /**
         * Imagen que representa al jugador
         */
        public Bitmap imagen;
        /**
         * Posición del objeto en la pantalla
         */
        public PointF posicion;
        /**
         * Cuadrado encargado de detectar colisiones
         */
        public Rect detectorColision;
        /**
         * Objeto de la clase Random
         */
        public Random random;

        /**
         * Constructor de la clase
         * @param imagen Imagen que va a representar al enemigo
         * @param y coordenada y
         */
        public enemigos(Bitmap imagen, float y) {
            this.imagen = imagen;
            random = new Random();
            float x = random.nextInt(anchoPantalla - imagen.getWidth());
            this.posicion = new PointF(x, y);
            setRectangulos();
        }//end constructor

        /**
         * Metodo encargado de mover al enemigo por pantalla
         * @param velocidad velocidad de movimiento
         */
        public void moverEnemigo(int velocidad){
            posicion.y += velocidad;
            setRectangulos();
        }//end method moverEnemigo

        /**
         * Metodo que actualiza el detector de colisiones cada vez que el objeto se mueve
         */
        public void setRectangulos(){
            detectorColision = new Rect((int)posicion.x, (int)posicion.y, (int)posicion.x + imagen.getWidth(), (int)posicion.y + imagen.getHeight());
        }//end setRectangulos
    }//end class enemigos

}//end class EscenaMinijuego


