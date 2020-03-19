package com.example.surfaceview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.view.GestureDetectorCompat;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class PruebaSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;  //Interfaz abstracta para manejar la superficie de dibujado
    private Context context;              //Contexto de la aplicación
    private Bitmap bitmapFondo;           //Imagen de fondo
    private int anchoPantalla = 1;        //Ancho de pantalla, su valor se actualiza en el método SurfaceChanged
    private int altoPantalla = 1;         //Alto de pantalla, su valor se actualiza en el método SurfaceChanged
    private  Hilo hilo;                   //Hilo encargado de dibujar y actualizar física
    private boolean funcionando = false;  //Control del hilo
    private int btnPulsado, btnNormal;
    //Control de la musica
    public static MediaPlayer mediaPlayer;

    //Variables control de escenas
    Escenas escenaActual;
    int nuevaEscena;
    public GestureDetectorCompat detectorDeGestos;
    //Constructor //Donde se inicializan las variables
    public PruebaSurfaceView(Context context) {
        super(context);
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.context = context;
        hilo = new Hilo();
        setFocusable(true);
        detectorDeGestos = new GestureDetectorCompat(context, new DetectorDeGestos());
        btnNormal = R.drawable.boton;
        btnPulsado = R.drawable.botonpulsado;
        //Musica de la pantalla
        mediaPlayer = MediaPlayer.create(context, R.raw.principal);
        mediaPlayer.setVolume(1, 1);
        mediaPlayer.setLooping(true);
    }//end contructor



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
                case 4 : escenaActual = new EscenaOpciones(4, context, altoPantalla, anchoPantalla, mediaPlayer);
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




    //Se ejecuta inmediatamente después de que la creación de la superficie de dibujo.
    //En cuanto se crea el SurfaceView se lanze el hilo
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        hilo.setFuncionando(true);
        //Aqui llamaremos a calcular datos
        if(hilo.getState() == Thread.State.NEW) hilo.start();
        if(hilo.getState() == Thread.State.TERMINATED) {
            hilo = new Hilo();
            hilo.start();
        }//end if
    }//end method surfaceCreated


    //Se ejecuta inmediatamente después de que la superficie de dibujo tenga cambios o bien
    //de tamaño o bien de forma.
    //Si hay algún cambio en la superficie de dibujo(normalmente su tamaño) obtenemos el nuevo tamaño
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        anchoPantalla = width;
        altoPantalla = height;
        escenaActual = new EscenaPrincipal(1, context,altoPantalla,anchoPantalla, btnNormal);
        //Ponemos la musica
        mediaPlayer.start();
        //Control temporal y calculo de los datos que lanzamos cuando le damos
        //valor a escenaActual por primera vez.
        escenaActual.controlTemporal();
        escenaActual.calcularDatos();
        hilo.setSurfaceSize(width, height);
    }//end method surfaceChanged




    //Se ejecuta inmediatamente antes de la destruccíon de la superficie de dibujo
    //Al finalizar el surface, se para el hilo
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





    //Clase Hilo en la cual se ejecuta el método de dibujo y de física para que se haga en paralelo con la
    //gestión de la interfaz de usuario.
    class Hilo extends Thread {

        @Override
        public void run() {
            while(funcionando){
                Canvas c = null; //Siempre es necesario repintar todo el lienzo
                try{
                    if(!surfaceHolder.getSurface().isValid()) continue; //Si la superficie no está preparada repetimos
                    c = surfaceHolder.lockCanvas(); //Obtenemos el lienzo con aceleración de software
                    //c = surfaceHolder.lockHardwareCanvas(); //Obtenemos el lienzo con aceleracion Hw. Desde la API 26
                    if(c != null) {
                        synchronized (surfaceHolder) { //La sincronización es necesario por ser recurso común
                            if (escenaActual != null) {
                                escenaActual.actualizarFisica();
                                escenaActual.dibujar(c);
                                if(escenaActual instanceof EscenaOpciones){
                                    MediaPlayer newmediaPlayer = ((EscenaOpciones) escenaActual).cambiarSonido(mediaPlayer);
                                    if(newmediaPlayer != mediaPlayer){
                                        mediaPlayer.stop();
                                        newmediaPlayer.start();
                                        mediaPlayer = newmediaPlayer;
                                    }//end if
                                }//end if

                            }//end if
                        }//end synchronized
                    }//end if
                } finally {  //Haya o no excepción, hay que liberar el lienzo
                    if(c != null){
                        surfaceHolder.unlockCanvasAndPost(c);
                    }//end if
                }//end finally
            }//end while
        }//end run

        //Activa o desactiva el funcionamiento del hilo
        void setFuncionando(boolean flag){
            funcionando = flag;
        }//end setFuncionando

        //Función llamada si cambia el tamaño del view
        public void setSurfaceSize(int width, int height){
            synchronized (surfaceHolder){
                if(bitmapFondo != null){
                    bitmapFondo = Bitmap.createScaledBitmap(bitmapFondo, width, height, true);
                }//end if
            }//end synchronized
        }//end setSurfaceSize
    }//end class Hilo
}//end class SurfaceView