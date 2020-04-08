package com.example.TheMoneyMachine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.view.GestureDetectorCompat;

public class PantallaPrincipal extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Interfaz abstracta para manejar la superficie de dibujado
     */
    private SurfaceHolder surfaceHolder;
    /**
     * Contexto de la aplicación
     */
    private Context context;
    /**
     * Imagen de fondo
     */
    private Bitmap bitmapFondo;
    /**
     * Ancho de pantalla, su valor se actualiza en el método SurfaceChanged
     */
    private int anchoPantalla = 1;
    /**
     * Alto de pantalla, su valor se actualiza en el método SurfaceChanged
     */
    private int altoPantalla = 1;
    /**
     * Hilo encargado de dibujar y actualizar física
     */
    private  Hilo hilo;
    /**
     * Control del hilo
     */
    private boolean funcionando = false;
    /**
     * Objeto encargado del control de la música del juego
     */
    public static MediaPlayer mediaPlayer;
    /**
     * Imagen del boton pulsado
     */
    private int btnPulsado;
    /**
     * Imagen del boton normal
     */
    private int btnNormal;
    /**
     * Objecto que representa la escena actual
     */
    Escenas escenaActual;
    /**
     * id de la escena que se quiere mostrar a continuación
     */
    int nuevaEscena;
    /**
     * Objeto encargado de capturar los gestos en pantalla
     */
    public GestureDetectorCompat detectorDeGestos;

    /**
     * Contructor de la clase
     * @param context contexto de la aplicación
     */
    public PantallaPrincipal(Context context) {
        super(context);
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.context = context;
        hilo = new Hilo();
        setFocusable(true);
        detectorDeGestos = new GestureDetectorCompat(context, new DetectorDeGestos());
        btnNormal = R.drawable.boton;
        btnPulsado = R.drawable.botonpulsado;
        mediaPlayer = MediaPlayer.create(context, R.raw.principal);
        mediaPlayer.setVolume(1, 1);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }//end contructor


    /**
     * Metodo que captura las pulsaciones en la pantalla
     * @param event evento
     * @return devuelve un booleano
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //Solo lanzamos el touch event de la escena actual cuando se pulsa la pantalla
        //y no tambien cuando se levanta el dedo, asi evitamos pulsaciones dobles.
        switch (event.getAction())
        {
            //Dependiendo de si soltamos o apretamos en la pantalla
            //cambiamos en la clase principal la foto del botón
            case MotionEvent.ACTION_DOWN:
                nuevaEscena = escenaActual.onTouchEvent(event);
                if(escenaActual instanceof EscenaPrincipal ){
                    ((EscenaPrincipal) escenaActual).setImagenBoton(btnPulsado);
                }//end if
                break;

            case MotionEvent.ACTION_UP:
                if(escenaActual instanceof EscenaPrincipal ){
                    ((EscenaPrincipal) escenaActual).setImagenBoton(btnNormal);
                    ((EscenaPrincipal) escenaActual).pulsadoBoton = false;
                }//end if
                break;
        }//end switch
        //Control de escenas
        if(nuevaEscena != escenaActual.numEscena){
            switch(nuevaEscena){
                case 1 :  escenaActual = new EscenaPrincipal(1, context,altoPantalla,anchoPantalla, btnNormal);
                    break;
                case 2 :  escenaActual = new EscenaMejoras(2, context,altoPantalla,anchoPantalla);
                    break;
                case 3 : escenaActual = new EscenaTrabajadores(3, context, altoPantalla, anchoPantalla);
                    break;
                case 4 : escenaActual = new EscenaOpciones(4, context, altoPantalla, anchoPantalla);
                    break;
            }//end switch
        }//end if
        //Comprobación de los gestos
        boolean gesto=false;
        //Comprobamos si se produce un gesto y lo guardamos en la variable
        if (detectorDeGestos != null && escenaActual.numEscena == 1){
            gesto=detectorDeGestos.onTouchEvent(event);
        }//end if
        if (gesto) escenaActual = new EscenaTrabajadores(3, context, altoPantalla, anchoPantalla);
        return true;
    }//end method onTouch

    /**
     * Se ejecuta inmediatamente después de que la creación de la superficie de dibujo
     * @param surfaceHolder
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        hilo.setFuncionando(true);
        //mediaPlayer.start();
        //Aqui llamaremos a calcular datos
        if(hilo.getState() == Thread.State.NEW) hilo.start();
        if(hilo.getState() == Thread.State.TERMINATED) {
            hilo = new Hilo();
            hilo.start();
        }//end if
    }//end method surfaceCreated

    /**
     * Se ejecuta inmediatamente después de que la superficie de dibujo tenga cambios o bien
     * de tamaño o bien de forma
     * @param surfaceHolder
     * @param format
     * @param width nuevo ancho
     * @param height nuevo alto
     */
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        anchoPantalla = width;
        altoPantalla = height;
        escenaActual = new EscenaPrincipal(1, context,altoPantalla,anchoPantalla, btnNormal);
        //Ponemos la musica
        //Control temporal y calculo de los datos que lanzamos cuando le damos
        //valor a escenaActual por primera vez.
        escenaActual.controlTemporal();
        escenaActual.calcularDatos();
        hilo.setSurfaceSize(width, height);
    }//end method surfaceChanged

    /**
     * Se ejecuta inmediatamente antes de la destruccíon de la superficie de dibujo
     * @param surfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //Se llama al metodo guardar datos
        escenaActual.guardarDatos();
        hilo.setFuncionando(false);
        try{
            hilo.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }//end method surfaceDestroyed


    public void pararMusica(){
        this.mediaPlayer.pause();
    }//end method pararMusica

    public void iniciarMusica(){
        this.mediaPlayer.start();
    }//end method pararMusica

    /**
     * Clase Hilo en la cual se ejecuta el método de dibujo y de física para que se haga en paralelo con la
     * gestión de la interfaz de usuario
     */
    class Hilo extends Thread {

        /**
         * Metodo que ocurre cuando el hilo esta en funcionamiento
         */
        @Override
        public void run() {
            long tiempoDormido = 0; //Tiempo que va a dormir el hilo
            final int FPS = 30;
            final int TPS = 1000000000; //Ticks en un segundo para la función nanoTime()
            final int FRAGMENTO_TEMPORAL = TPS/FPS; //Espacio de tiempo en el que haremos todo de forma repetida
            //Tomamos un tiempo de referencia
            long tiempoReferencia = System.nanoTime();

            while(funcionando){
                Canvas c = null; //Siempre es necesario repintar todo el lienzo
                try{
                    if(!surfaceHolder.getSurface().isValid()) continue; //Si la superficie no está preparada repetimos
                    c = surfaceHolder.lockCanvas(); //Obtenemos el lienzo con aceleración de software
                    if(c != null) {
                        synchronized (surfaceHolder) { //La sincronización es necesario por ser recurso común
                            if (escenaActual != null) {
                                escenaActual.dibujar(c);
                                escenaActual.actualizarFisica();
                            }//end if
                        }//end synchronized
                    }//end if
                } finally {  //Haya o no excepción, hay que liberar el lienzo
                    if(c != null){
                        surfaceHolder.unlockCanvasAndPost(c);
                    }//end if
                }//end finally

                //Calculamos el siguiente instante temporal donde volvemos a actualizar y pintar
                tiempoReferencia += FRAGMENTO_TEMPORAL;

                //El tiempo que duerme será el siguiente menos el actual
                tiempoDormido = tiempoReferencia - System.nanoTime();

                //Si tarda mucho dormimos
                if(tiempoDormido > 0){
                    try{
                        Thread.sleep(tiempoDormido / 1000000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }//end catch
                }//end if
            }//end while
        }//end run

        /**
         * Activa o desactiva el funcionamiento del hilo
         * @param flag indica si se activa o desctiva el hilo
         */
        void setFuncionando(boolean flag){
            funcionando = flag;
        }//end setFuncionando

        //Función llamada si cambia el tamaño del view

        /**
         * Función llamada si cambia el tamaño del view
         * @param width nuevo ancho
         * @param height nuevo alto
         */
        public void setSurfaceSize(int width, int height){
            synchronized (surfaceHolder){
                if(bitmapFondo != null){
                    bitmapFondo = Bitmap.createScaledBitmap(bitmapFondo, width, height, true);
                }//end if
            }//end synchronized
        }//end setSurfaceSize
    }//end class Hilo
}//end class PantallaPrincipal