package com.example.surfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;



public class EscenaPrincipal extends Escenas {

    Rect pulsador, btnMejora, btnOpciones;


    Timer timer;
    int gap=2000;
    long tempTiempo=0;
    int imagenBoton;

    boolean pulsadoBoton;

    public int getImagenBoton() {
        return imagenBoton;
    }

    public void setImagenBoton(int imagenBoton) {
        if(pulsadoBoton) {
            aux = BitmapFactory.decodeResource(context.getResources(), imagenBoton);
            bitmapBtn = aux.createScaledBitmap(aux, pulsador.width(), pulsador.height(), true);
            this.imagenBoton = imagenBoton;
        }//end if

    }//end set


    int randomPosX;
    movimientoNumero mov;
    Boolean moverNumero;
    Boolean flag = true;


    avisos aviso;

    //Control de la musica
    public MediaPlayer mediaPlayer;

    Bitmap bitmapOpciones, bitmapMejoras, bitmapBtn;


    public EscenaPrincipal(int numEscena, Context context, int altoPantalla, int anchoPantalla, int imagen) {
        super(numEscena, context, altoPantalla, anchoPantalla);
        this.imagenBoton = imagen;
        //Imagen de fondo
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.oficina);
        bitmapFondo = aux.createScaledBitmap(aux,anchoPantalla, altoPantalla,true);

        //Cuadrados que se utilizaran para saber si se a pulsado en ellos
        pulsador = new Rect(anchoPantalla / 3,(altoPantalla/3) * 2,anchoPantalla - anchoPantalla / 3,altoPantalla - anchoPantalla / 3);
        btnMejora = new Rect(anchoPantalla - anchoPantalla/9, 0, anchoPantalla, anchoPantalla/9);
        btnOpciones = new Rect(0, 0, anchoPantalla/9,anchoPantalla/9);

        //Imagen de botón menu de opciones
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.menu);
        bitmapOpciones = aux.createScaledBitmap(aux, btnOpciones.width(), btnOpciones.height(), true);

        //Imagen de  botón pantalla de mejoras
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.mejora);
        bitmapMejoras = aux.createScaledBitmap(aux, btnOpciones.width(), btnOpciones.height(), true);

        //Imagen del botón de la mitad de la pantalla
        aux = BitmapFactory.decodeResource(context.getResources(), imagenBoton);
        bitmapBtn = aux.createScaledBitmap(aux, pulsador.width(), pulsador.height(), true);


        //Timer
        TimerTask timerTask = new TimerTask() {
            //Dentro de run es donde se pone todo el codigo
            public void run() {
                //Codigo del timer
                money += autoclick;
            }//end run
        };
        // Aquí se pone en marcha el timer cada segundo.
        timer = new Timer();
        // Dentro de 0 milisegundos avísame cada 2000 milisegundos
        timer.scheduleAtFixedRate(timerTask, 0, tiempoAutoclick);

        //Musica de la pantalla
        mediaPlayer = MediaPlayer.create(context, R.raw.principal);
        mediaPlayer.setVolume(1, 1);
        //Comentado para que no de por culo
        mediaPlayer.start();

        pulsadoBoton = false;
        moverNumero = false;

        cuadroConBotones = new pantallaAvisos(altoPantalla,anchoPantalla, "", context, pincelFondo, pincelCuadro, pincelTexto);
        cuadroAviso = new pantallaAvisos(altoPantalla,anchoPantalla, "", context, pincelFondo, pincelCuadro, pincelTexto);
        aviso = new avisos(cuadroAviso, cuadroConBotones);

    }//end constructor

    @Override
    public void dibujar(Canvas c) {
        try {
            //setAlpha va desde 0(casper) hasta 255(totalmente visible)
            pincelFondo.setAlpha(150);
            pincelCuadro.setColor(Color.BLACK);
            pincelTexto.setColor(Color.WHITE);
            pincelTexto.setTextAlign(Paint.Align.CENTER);
            pincelTexto.setTextSize(40);
            pincelTexto.setAntiAlias(true);

            pincelPrueba.setColor(Color.BLACK);
            pincelPrueba.setTextSize(70);
            //pincelPrueba.setAlpha(alpha);

            c.drawBitmap(bitmapFondo,0, 0,null);

            //Imagenes de los botones
            c.drawBitmap(bitmapOpciones,0, 0, null);
            c.drawBitmap(bitmapMejoras,anchoPantalla - btnOpciones.width(), 0,null);


            c.drawBitmap(bitmapBtn, anchoPantalla/3, (altoPantalla / 3) * 2, null);

            //Si los trabajadores han ganado algo offline lo mostramos
            if(trabajadores.mensajeBeneficios){
                avisoDineroOffline = new pantallaAvisos(altoPantalla,anchoPantalla, "Has ganado " + moneyOffline + " mientras estabas fuera!", context, pincelFondo, pincelCuadro, pincelTexto);
                avisoDineroOffline.cuadroEstandar(c);
            }//end if



            if(aviso.isRespuesta()){
                cuadroAviso.cuadroEstandar(c);
            }//end if
            else{
                if(aviso.isHayAviso()){
                    cuadroConBotones.cuadroBotones(c);
                }//end if
            }//end else



            //Pruebas para la animación, ya te digo que necesitar
            //un timer o un hilo para esto
            /*
            if(moverNumero){
                mov.dibuja(c);
                mov.movimiento();
                moverNumero = false;
            }//end if
            */



        }catch(Exception e){
            e.printStackTrace();
        }//end catch
        super.dibujar(c);
    }//end method dibujar

    @Override
    public void actualizarFisica() {
        //super.actualizarFisica();


    }//end actualizarFisica



    @Override
    public int onTouchEvent(MotionEvent event) {
        //Tarea pendiente quitar todos los editor.put de cada touch y simplemente
        //ponerlos solo cuando se vaya a cambiar de pantalla
        int x = (int)event.getX();
        int y = (int)event.getY();

        //comprobar();

        if(aviso.isHayAviso()) {
            if(cuadroConBotones.btnAceptar.contains(x, y)){
                respuesta(true);
                aviso.setRespuesta(true);
                aviso.setHayAviso(false);
            }//end if
            if(cuadroConBotones.btnCancelar.contains(x, y)){
                respuesta(false);
                aviso.setRespuesta(true);
                aviso.setHayAviso(false);
            }//end if
            return numEscena;
        }//end if
        else {
            aviso.setRespuesta(false);
            if (btnOpciones.contains(x, y)) {
                timer.cancel();
                timer.purge();
                return 4;
            }//end if
            if (pulsador.contains(x, y)) {
                money += dineroPulsacion;
                editor.putInt("money", money).commit();
                //Sonido que se genera
                int v = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                efectos.play(sonidoCoin,1,1,1,0,1);
                /*
                //Movimiento del numero por pantalla
                randomPosX = new Random().nextInt(pulsador.width()) + anchoPantalla/3;
                Point p = new Point(randomPosX, anchoPantalla/3);
                mov = new movimientoNumero(money, 150, p);
                moverNumero = true;
                 */
                //Si pulsamos dentro del boton cambiamos su sprite
                pulsadoBoton = true;



                return numEscena;
            }//end if
            if (btnMejora.contains(x, y)) {
                timer.cancel();
                timer.purge();
                return 2;
            }//end if



        }//end else

        //Cuando se toca la pantalla se cierra el cuadro de dialogo
        if (trabajadores.mensajeBeneficios)
        {
            trabajadores.mensajeBeneficios = false;
        }//end if




        return numEscena;
   }//end onTouchEvent


    //Tenemos un grave problema con esta pta mierda y ya llevas 4h, mañana recien levantadito le das otra
    //pasadiña. Ponte ahora con otras cosas meu, dejo comentado arriba la llamada a este metodo
    public void comprobar(){
        //Comprobar aqui que texto devolver

        if(money == 50 && flag) {
            String text = "Tus trabajadores están hambrientos.\n" +
                    "¿Que tal si organizas una cena para\n" +
                    "toda la plantilla?";

            cuadroConBotones.setTexto(text);
            aviso.setNumAviso(1);
            aviso.setHayAviso(true);
            flag = false;
        }//end if

    }//end method probar


    public void respuesta(boolean caso){
        switch (aviso.getNumAviso()){

            case 1:
                if(caso){
                    cuadroAviso.setTexto("Tus trabajadores estan más contentos!!!");
                }//end if
                else{
                    cuadroAviso.setTexto("Tus trabajadores se desaniman un poco!!!");
                }//end else

                break;
        }//end switch






    }//end method respuesta


}//end class EscenaPrincipal
