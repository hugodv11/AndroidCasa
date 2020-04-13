package com.example.TheMoneyMachine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Clase que representa el juego de destruir bloques
 */
public class EscenaMinijuegoBloques extends Escenas {
    /**
     * Colección de objetos de la clase bloques
     */
    public ArrayList<bloques> coleccionBloques;
    /**
     * Colección de objetos de clase Bitmap
     */
    public ArrayList<Bitmap> coleccionColores;
    /**
     * objeto que representa al jugador
     */
    public jugador jugador;
    /**
     * objeto que representa la pelota
     */
    public pelota pelota;
    /**
     * Cuadrado que representa al jugador
     */
    public Rect cuadroJugador;
    /**
     * Cuadrado que representa a los bloques
     */
    public Rect cuadroBloque;
    /**
     * Cuadrado que representa la pelota
     */
    public Rect cuadroPelota;
    /**
     * Cuadrado que representa la parte izquierda del dispositivo
     */
    public Rect izquierda;
    /**
     * Cuadrado que representa la parte derecha del dispositivo
     */
    public Rect derecha;
    /**
     * Cuadrado que representa las vidas
     */
    public Rect cuadroVidas;
    /**
     * Bitmap que representa los bloques
     */
    public Bitmap bitmapBloques;
    /**
     * Bitmap que representa al jugador
     */
    public Bitmap bitmapJugador;
    /**
     * Bitmap que representa la pelota
     */
    public Bitmap bitmapPelota;
    /**
     * Bitmap que representa las vidas
     */
    public Bitmap bitmapVidas;
    /**
     * Velocidad de movimiento del jugador
     */
    public int velocidadJugador;
    /**
     * Objeto de la clase Random
     */
    public Random random;
    /**
     * Representa las vidas restantes del jugador
     */
    public int vidas;
    /**
     * Objeto de la clase pantallaAviso
     */
    public pantallaAvisos aviso;
    /**
     * Indica si el juego esta activo
     */
    public boolean activo;
    /**
     * Indica si el jugador a perdido
     */
    public boolean derrota;
    /**
     * Indica si el jugador a ganado
     */
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
        velocidadJugador = 0;
        aviso = new pantallaAvisos(altoPantalla, anchoPantalla, "", context, pincelFondo, pincelCuadro, pincelTexto);
        activo = false;
        derrota = false;
        victoria = false;
    }//end constructor

    /**
     * Metodo que se encarga de dibuja en el canvas
     * @param c  Canvas en el que se va a dibujar
     */
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

    /**
     * Metodo que se encarga de actualizar el movimiento de los componentes
     */
    @Override
    public void actualizarFisica() {
        if(activo) {
            jugador.moverJugador(velocidadJugador);
            pelota.rebotes();
            if(pelota.detectorColisiones.intersect(jugador.detectorColisiones)) {
                pelota.posicion.y = altoPantalla - altoPantalla / 6 - bitmapJugador.getHeight();
                pelota.direccionY *= -1;
            }//end if

            for (int i = 0; i < coleccionBloques.size(); i++) {
                if (pelota.detectorColisiones.intersect(coleccionBloques.get(i).detectorColisionesDerecha) ||
                        pelota.detectorColisiones.intersect(coleccionBloques.get(i).detectorColisionesIzquierda)) {
                    coleccionBloques.remove(coleccionBloques.get(i));
                    pelota.direccionX *= -1;
                }//end if
                else {
                    if (pelota.detectorColisiones.intersect(coleccionBloques.get(i).detectorColisionesArriba) ||
                            pelota.detectorColisiones.intersect(coleccionBloques.get(i).detectorColisionesAbajo)) {
                        coleccionBloques.remove(coleccionBloques.get(i));
                        pelota.direccionY *= -1;
                    }//end if
                }//end else
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
        public Rect detectorColisiones;

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
            detectorColisiones = new Rect((int)posicion.x, (int)posicion.y, (int)posicion.x + imagen.getWidth(), (int)posicion.y + imagen.getHeight());
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

    }//end class jugador

    /**
     * Clase que representa los bloques
     */
    public class bloques{
        /**
         * Representa el lateral superior del bloque
         */
        public Rect detectorColisionesArriba;
        /**
         * Representa el lateral inferior del bloque
         */
        public Rect detectorColisionesAbajo;
        /**
         * Representa el lateral derecho del bloque
         */
        public Rect detectorColisionesDerecha;
        /**
         * Representa el lateral izquierdo del bloque
         */
        public Rect detectorColisionesIzquierda;
        /**
         * Representa la esquina superior izquierda del bloque
         */
        public Rect arribaIzquierda;
        /**
         * Representa la esquina inferior izquierda del bloque
         */
        public Rect abajoIzquierda;
        /**
         * Representa la esquina superior derecha del bloque
         */
        public Rect arribaDerecha;
        /**
         * Representa la esquina inferior derecha del bloque
         */
        public Rect abajoDerecha;
        /**
         * Imagen que representa al bloque
         */
        public Bitmap imagen;
        /**
         * Posición del bloque
         */
        public PointF posicion;
        /**
         * Longitud de los lados de las esquinas del bloque
         */
        public int lado;

        /**
         * Constructor de la clase
         * @param imagen imagen que va a representar al jugador
         * @param x coordenada x
         * @param y coordenada y
         */
        public bloques(Bitmap imagen, float x ,float y) {
            this.imagen = imagen;
            this.posicion = new PointF(x, y);
            lado = imagen.getWidth()/6;
            detectorColisionesArriba = new Rect((int)posicion.x + lado, (int)posicion.y, (int)posicion.x + imagen.getWidth() - lado, (int)posicion.y + imagen.getHeight() / 6);
            detectorColisionesAbajo = new Rect((int)posicion.x + lado, (int)posicion.y + (imagen.getHeight() - imagen.getHeight() / 6), (int)posicion.x + imagen.getWidth() - lado, (int)posicion.y + imagen.getHeight());
            detectorColisionesDerecha = new Rect((int)posicion.x + (imagen.getWidth() - imagen.getWidth() / 6), (int)posicion.y + lado, (int)posicion.x + imagen.getWidth(), (int)posicion.y + imagen.getHeight() - lado);
            detectorColisionesIzquierda = new Rect((int)posicion.x, (int)posicion.y + lado, (int)posicion.x + imagen.getWidth() / 6, (int)posicion.y + imagen.getHeight() - lado);
            arribaIzquierda = new Rect((int) posicion.x, (int) posicion.y, (int) posicion.x + lado, (int) posicion.y + lado);
            arribaDerecha = new Rect((int) (posicion.x + imagen.getWidth()) - lado, (int) posicion.y, (int) posicion.x + imagen.getWidth(), (int) posicion.y + lado);
            abajoIzquierda = new Rect((int) posicion.x, (int) posicion.y + imagen.getHeight() - lado, (int) posicion.x + lado, (int) posicion.y + imagen.getHeight());
            abajoDerecha = new Rect((int) (posicion.x + imagen.getWidth()) - lado, (int) posicion.y + imagen.getHeight() - lado, (int) posicion.x + imagen.getWidth(), (int) posicion.y + imagen.getHeight());
        }//end constructor

    }//end class bloques

    /**
     * Clase que representa la pelota
     */
    public class pelota{
        /**
         * Cuadrado que se encarga de detectar colisiones
         */
        public Rect detectorColisiones;
        /**
         * Imagen que representa a la pelota
         */
        public Bitmap imagen;
        /**
         * Posición de la pelota en la pantalla
         */
        public PointF posicion;
        /**
         * Velocidad de la pelota
         */
        public int velocidad;
        /**
         * Direccion de la pelota en el eje x
         */
        public int direccionX;
        /**
         * Direccion de la pelota en el eje y
         */
        public int direccionY;

        /**
         * Constructor de la clase
         * @param imagen imagen que va a representar al jugador
         * @param x coordenada x
         * @param y coordenada y
         */
        public pelota(Bitmap imagen, float x, float y, int velocidad) {
            this.imagen = imagen;
            this.posicion = new PointF(x, y);
            setRectangulos();
            this.velocidad = velocidad;
            direccionX = 1;
            direccionY = -1;
        }//end constructor

        /**
         * Metodo que actualiza el detector de colisiones cada vez que el objeto se mueve
         */
        public void setRectangulos(){
            detectorColisiones = new Rect((int)posicion.x, (int)posicion.y, (int)posicion.x + imagen.getWidth(), (int)posicion.y + imagen.getHeight());
        }//end setRectangulos

        /**
         * Metodo que se encarga de que la pelota rebote por la pantalla
         */
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

    /**
     * Metodo que se encarga de generar todos los bloques
     */
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
                //bloque.detectorColisiones = new Rect(anchoPantalla / 7 + separacionX , altoPantalla / 6 + separacionY, anchoPantalla / 7 + separacionX + tamaño, altoPantalla / 6 + separacionY + (anchoPantalla/9) / 2);
                coleccionBloques.add(bloque);
                separacionX += bitmapBloques.getWidth();
            }//end for
            separacionX = 0;
            separacionY += bitmapBloques.getHeight();
        }//end for
    }//end method crearBloques

    /**
     * Carga en una colección los diferentes bloques de colores
     */
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
