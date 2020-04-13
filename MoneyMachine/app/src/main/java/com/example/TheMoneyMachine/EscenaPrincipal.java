package com.example.TheMoneyMachine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.MotionEvent;


import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Clase que se encarga de dibujar la pantalla principal
 * Es la que se dibuja por defecto
 */
public class EscenaPrincipal extends Escenas {

    /**
     * Cuadrado que representa el botón rojo
     */
    Rect pulsador;
    /**
     * Cuadrado que representa el botón de mejoras
     */
    Rect btnMejora;
    /**
     * Cuadrado que representa el botón de opciones
     */
    Rect btnOpciones;
    /**
     * Cuadrado que representa el botón de ayuda
     */
    Rect btnAyuda;
    /**
     * Colección de particulas
     */
    ArrayList<Particulas> particulas = new ArrayList<>();
    /**
     * Objeto que representa una particula
     */
    Particulas particula;
    /**
     * Objeto que genera numeros aleatorios
     */
    Random random;
    /**
     * Objeto que permite acceder al hardware del dispositivo
     */
    SensorManager sensorManager;
    /**
     * Objeto que representa un elemento de hardware
     */
    Sensor sensor;
    /**
     * Listener de el objeto sensor
     */
    SensorEventListener sensorEventListener;
    /**
     * objeto de tipo Timer
     */
    Timer timer;
    /**
     * Imagen del boton
     */
    int imagenBoton;
    /**
     * Indica si el boton a sido pulsado
     */
    boolean pulsadoBoton;
    /**
     * Indica si hay un aviso que mostrar
     */
    boolean hayAviso;
    /**
     * Indica si hay una respuesta que mostrar
     */
    boolean respuesta;
    /**
     * Indica si se debe mostrar la pantalla de ayuda
     */
    boolean ayuda;
    /**
     * ID que identifica cada uno de los avisos disponibles
     */
    int numAviso;
    /**
     * set de la propiedad imagenBoton
     * @param imagenBoton int que representa una imagen
     */
    public void setImagenBoton(int imagenBoton) {
        if(pulsadoBoton) {
            aux = BitmapFactory.decodeResource(context.getResources(), imagenBoton);
            bitmapBtn = aux.createScaledBitmap(aux, pulsador.width(), pulsador.height(), true);
            this.imagenBoton = imagenBoton;
        }//end if
    }//end set

    /**
     * Indica si el dispositivo a sido agitado
     */
    Boolean shake;
    /**
     * Indica si el disposivo a sido agitado
     */
    Boolean oneTime;
    /**
     * Imagen del boton de opciones
     */
    Bitmap bitmapOpciones;
    /**
     * Imagen del boton de mejoras
     */
    Bitmap bitmapMejoras;
    /**
     * Imagen del boton rojo
     */
    Bitmap bitmapBtn;
    /**
     * Imagen de la particula
     */
    Bitmap bitmapParticula;
    /**
     * Imagen del boton de ayuda
     */
    Bitmap bitmapbtnAyuda;
    /**
     * Imagen de la pantalla de ayuda
     */
    Bitmap bitmapAyuda;
    /**
     * Imagen que representa el botón bloqueado
     */
    Bitmap bitmapbtnBloqueado;
    /**
     * Objeto de la clase pantallaAvisos
     */
    pantallaAvisos avisoBloqueo;
    /**
     * Indica si la pulsación ha sido bloqueada porque el botón está bloqueado
     */
    Boolean pulsacionBLoqueada;

    /**
     * Constructor de la clase
     * @param numEscena numero de escena actual
     * @param context contexto de la applicación
     * @param altoPantalla alto de la pantalla del dispositivo
     * @param anchoPantalla ancho de la pantalla del dispositivo
     */
    public EscenaPrincipal(int numEscena, Context context, int altoPantalla, int anchoPantalla, int imagen) {
        super(numEscena, context, altoPantalla, anchoPantalla);
        this.imagenBoton = imagen;
        random = new Random();
        //Imagen de fondo
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.oficina);
        bitmapFondo = aux.createScaledBitmap(aux,anchoPantalla, altoPantalla,true);

        //Cuadrados que se utilizaran para saber si se a pulsado en ellos
        pulsador = new Rect(anchoPantalla / 3,(altoPantalla/5) * 4,anchoPantalla - anchoPantalla / 3,altoPantalla - altoPantalla / 12);
        btnMejora = new Rect(anchoPantalla - anchoPantalla/9, 0, anchoPantalla, anchoPantalla/9);
        btnOpciones = new Rect(0, 0, anchoPantalla/9,anchoPantalla/9);
        btnAyuda = new Rect(anchoPantalla - anchoPantalla/9, anchoPantalla/9 * 3, anchoPantalla, anchoPantalla/9 * 4);

        //Imagen de botón menu de opciones
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.menu);
        bitmapOpciones = aux.createScaledBitmap(aux, btnOpciones.width(), btnOpciones.height(), true);

        //Imagen de  botón pantalla de mejoras
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.mejora);
        bitmapMejoras = aux.createScaledBitmap(aux, btnOpciones.width(), btnOpciones.height(), true);

        //Imagen de  botón pantalla de mejoras
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.ayuda);
        bitmapbtnAyuda = aux.createScaledBitmap(aux, btnAyuda.width(), btnAyuda.height(), true);

        //Imagen del botón de la mitad de la pantalla
        aux = BitmapFactory.decodeResource(context.getResources(), imagenBoton);
        bitmapBtn = aux.createScaledBitmap(aux, pulsador.width(), pulsador.height(), true);

        aux = BitmapFactory.decodeResource(context.getResources(), R.drawable.cadenas);
        bitmapbtnBloqueado = aux.createScaledBitmap(aux, pulsador.width(), pulsador.height(), true);
        //bitmap de las particulas
        bitmapParticula = BitmapFactory.decodeResource(context.getResources(), R.drawable.animacionmoney);

        //Timer del autoclick
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
        //Variables para el uso del acelerómetro
        shake = false;
        oneTime = false;
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                //Cada vez que inclinas el movil hacia la izquiera es
                //como si hicieras una pulsación
                if(x >= 5){
                    if(!oneTime){
                        shake = true;
                    }//end if
                }//end if
                if(shake){
                    if(x <= 5){
                        money += dineroPulsacion;
                        shake = false;
                        oneTime = true;
                    }//end if
                }//end if
                else{
                    oneTime = false;
                }//end else
                editor.putInt("money", money);
            }//end method onSensorChanged
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }//end method onAccuracyChanged
        };
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        avisoBloqueo = new pantallaAvisos(altoPantalla, anchoPantalla, "", context, pincelFondo, pincelCuadro, pincelTexto);
        pulsacionBLoqueada = false;
        pulsadoBoton = false;
        hayAviso = false;
        respuesta = false;

        ayuda = false;

        if(!ingles){
            aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.ayudaprincipal);
            bitmapAyuda = aux.createScaledBitmap(aux, anchoPantalla, altoPantalla,true);
        }//end if
        else {
            aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.helpmain);
            bitmapAyuda = aux.createScaledBitmap(aux, anchoPantalla, altoPantalla,true);
        }//end else
    }//end constructor


    /**
     * Metodo que se encarga de dibuja en el canvas
     * @param c  Canvas en el que se va a dibujar
     */
    @Override
    public void dibujar(Canvas c) {
        try {
            c.drawBitmap(bitmapFondo,0, 0,null);
            //Imagenes de los botones
            c.drawBitmap(bitmapOpciones,0, 0, null);
            c.drawBitmap(bitmapMejoras,anchoPantalla - btnOpciones.width(), 0,null);
            c.drawBitmap(bitmapbtnAyuda,anchoPantalla - anchoPantalla/9, anchoPantalla/9 * 3,null);
            c.drawBitmap(bitmapBtn, anchoPantalla / 3,(altoPantalla/5) * 4, null);
            if(bloqueado){
                c.drawBitmap(bitmapbtnBloqueado, anchoPantalla / 3,(altoPantalla/5) * 4, null);
                if(pulsacionBLoqueada){
                    avisoBloqueo.setTexto("El botón está bloqueado!!. Gana un minijuego. para desbloquearlo");
                    avisoBloqueo.cuadroBotones(c);
                }//end if
            }//end if

            //Si los trabajadores han ganado algo offline lo mostramos
            if(trabajadores.mensajeBeneficios){
                String s = context.getResources().getString(R.string.ganado1) + " " + moneyOffline + " " + context.getResources().getString(R.string.ganado2);
                avisoDineroOffline = new pantallaAvisos(altoPantalla,anchoPantalla, s, context, pincelFondo, pincelCuadro, pincelTexto);
                avisoDineroOffline.cuadroEstandar(c);
            }//end if
            if(respuesta){
                cuadroAviso.cuadroEstandar(c);
            }//end if
            else{
                if(hayAviso){
                    cuadroConBotones.cuadroBotones(c);
                }//end if
            }//end else

            if(ayuda) {
                c.drawBitmap(bitmapAyuda, 0, 0, null);
            }//end if


        }catch(Exception e){
            e.printStackTrace();
        }//end catch

        //Particulas
        for(int i = 0; i < particulas.size(); i++){
            if(particulas.get(i).visible){
                c.drawBitmap(particulas.get(i).imagen, particulas.get(i).posicion.x, particulas.get(i).posicion.y, particulas.get(i).pincelImagen);
            }//end if
        }//end for
        super.dibujar(c);
    }//end method dibujar

    /**
     * Metodo que se encarga de actualizar el movimiento de los componentes
     */
    @Override
    public void actualizarFisica() {

        for(int i = 0; i < particulas.size(); i++){
            if(particulas.get(i).alpha <= 0){
                particulas.get(i).visible = false;
            }//end if

            if(particulas.get(i).visible){
                particulas.get(i).moverParticula(5,3);
                particulas.get(i).actualizarImagen();
            }//end if

            if(!particulas.get(i).visible){
                particulas.remove(particulas.get(i));
            }//end if
        }//end for


    }//end actualizar fisica

    /**
     * Metodo que se lanza cuando se produce una pulsación en la pantalla
     * @param event evento de la pulsación
     * @return devuelve el número de escena que se debe dibujar
     */
    @Override
    public int onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        //Comprueba si se tiene que producir un evento
        comprobar();

        if(hayAviso) {
            if(cuadroConBotones.btnAceptar.contains(x, y)){
                cuadroAviso.setTexto(respuesta(true));
                respuesta = true;
                hayAviso = false;
            }//end if
            if(cuadroConBotones.btnCancelar.contains(x, y)){
                cuadroAviso.setTexto(respuesta(false));
                respuesta = true;
                hayAviso = false;
            }//end if
            return numEscena;
        }//end if
        else {
            respuesta = false;
            if(pulsacionBLoqueada){
                if(avisoBloqueo.btnAceptar.contains(x, y )){
                    pulsacionBLoqueada = false;
                    bloqueado = false;
                    editor.putBoolean("bloqueado", bloqueado).commit();
                    int number = random.nextInt(2) + 5;
                    return number;
                }//end if
                else{
                    pulsacionBLoqueada = false;
                }//end else
            }//end if
            if(btnAyuda.contains(x, y)){
                ayuda = true;
            }//end if
            else {
                ayuda = false;
            }//end else
            if (btnOpciones.contains(x, y)) {
                timer.cancel();
                timer.purge();

                return 4;
            }//end if

            if (pulsador.contains(x, y)) {
                if(bloqueado){
                    pulsacionBLoqueada = true;
                }//end if
                else {
                    int val = random.nextInt(30);
                    if (val == 1) {
                        bloqueado = true;
                        editor.putBoolean("bloqueado", bloqueado).commit();
                    }//end if
                    else {
                        if (!bloqueado) {
                            money += dineroPulsacion;
                            editor.putInt("money", money).commit();
                            //Sonido que se genera
                            int v = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                            efectos.play(sonidoCoin, 1, 1, 1, 0, 1);
                            //Si pulsamos dentro del boton cambiamos su sprite
                            pulsadoBoton = true;
                            //Creamos la particula y la añadimos al arrayList
                            particula = new Particulas(bitmapParticula, random.nextInt(anchoPantalla), pulsador.centerY());
                            particulas.add(particula);
                        }//end if
                    }//end else
                }//end else
                //return numEscena;
            }//end if
            if (btnMejora.contains(x, y)) {
                timer.cancel();
                timer.purge();
                //Despues de utilizar el objeto aviso lo guardamos
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


    /**
     * Comprueba si se cumplen las condiciones de alguno
     * de los eventos.
     */
    public void comprobar(){
        if(money >= 4000 && flag1) {
            String text = context.getResources().getString(R.string.evento1);
            cuadroConBotones.setTexto(text);
            numAviso = 1;
            hayAviso = true;
            flag1 = false;
            editor.putBoolean("flag1", false).commit();
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        }//end if

        if(money >= 2000 && flag2) {
            String text = context.getResources().getString(R.string.evento2);
            cuadroConBotones.setTexto(text);
            numAviso = 2;
            hayAviso = true;
            flag2 = false;
            editor.putBoolean("flag2", false).commit();
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        }//end if

        if(money >= 10000 && flag3) {
            String text = context.getResources().getString(R.string.evento3);
            cuadroConBotones.setTexto(text);
            numAviso = 3;
            hayAviso = true;
            flag3 = false;
            editor.putBoolean("flag3", false).commit();
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        }//end if

        if(money >= 30000000 && flag4) {
            String text = context.getResources().getString(R.string.evento4);
            cuadroConBotones.setTexto(text);
            numAviso = 4;
            hayAviso = true;
            flag4 = false;
            editor.putBoolean("flag4", false).commit();
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        }//end if
    }//end method probar


    /**
     * Dependiendo del evento, devuelve sus resultados
     * @param caso booleano que indica si el usuario a dado una respuesta positiva o negativa
     * @return devuelve un string correspondiente a la decisión del usuario(caso)
     */
    public String respuesta(boolean caso){
        switch (numAviso){
            case 1:
                if(caso){
                    trabajadores.salud += 5;
                    editor.putInt("saludTrabajadores", trabajadores.salud).commit();
                    return context.getResources().getString(R.string.evento1True);
                }//end if
                else{
                    trabajadores.salud -= 5;
                    editor.putInt("saludTrabajadores", trabajadores.salud).commit();
                    return context.getResources().getString(R.string.evento1False);
                }//end else

            case 2:
                return context.getResources().getString(R.string.evento2);

            case 3:
                if(caso){
                    trabajadores.salud += 10;
                    trabajadores.energia = 0;
                    editor.putInt("saludTrabajadores", trabajadores.salud);
                    editor.putInt("energiaTrabajadores", trabajadores.energia).commit();
                    return context.getResources().getString(R.string.evento3True);
                }//end if
                else{
                    trabajadores.salud -= 10;
                    trabajadores.energia += 40;
                    if(trabajadores.energia > 100){
                        trabajadores.energia = 100;
                    }//end if
                    editor.putInt("saludTrabajadores", trabajadores.salud);
                    editor.putInt("energiaTrabajadores", trabajadores.energia).commit();
                    return context.getResources().getString(R.string.evento3False);
                }//end else

            case 4:
                if(caso){
                    trabajadores.salud += 10;
                    trabajadores.energia += 60;
                    if (trabajadores.energia > 100) {
                        trabajadores.energia = 100;
                    }//end if
                    money -= dineroPulsacion * 10;
                    if(money >0){
                        money = 0;
                    }//end if
                    editor.putInt("saludTrabajadores", trabajadores.salud);
                    editor.putInt("money", money);
                    editor.putInt("energiaTrabajadores", trabajadores.energia).commit();
                    return context.getResources().getString(R.string.evento4True);
                }//end if
                else {
                    trabajadores.salud -= 20;
                    trabajadores.energia -= 80;
                    if (trabajadores.energia < 0) {
                        trabajadores.energia = 0;
                    }//end if
                    money += dineroPulsacion * 20;
                    editor.putInt("saludTrabajadores", trabajadores.salud);
                    editor.putInt("money", money);
                    editor.putInt("energiaTrabajadores", trabajadores.energia).commit();
                    return context.getResources().getString(R.string.evento4False);
                }//end else
            default:
                return "";
        }//end switch
    }//end method respuesta
}//end class EscenaPrincipal
