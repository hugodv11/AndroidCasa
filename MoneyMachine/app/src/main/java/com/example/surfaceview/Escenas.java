package com.example.surfaceview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import androidx.core.view.GestureDetectorCompat;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.security.auth.login.LoginException;

public class Escenas {
    //ATRIBUTOS PARA LAS ESCENAS

    int numEscena;
    Context context;
    int altoPantalla, anchoPantalla;

    //Pinceles//Cambiar a una clase
    Paint pincelTxt = new Paint();
    Paint pincelRec = new Paint();
    Paint pincelPrueba = new Paint();
    //Pinceles que se usan para la creación de cuadros de dialogo
    Paint pincelFondo = new Paint();
    Paint pincelCuadro = new Paint();
    Paint pincelTexto = new Paint();


    //Variables para el control de sonido
    public AudioManager audioManager;
    public SoundPool efectos;
    public int sonidoCoin;
    public int maxSonidosSimultaneos = 10;

    //Control de gestos
    public GestureDetectorCompat detectorDeGestos;

    //Bitmap que se una para dibujar los fondos, la clase que lo hereda lo define;
    Bitmap bitmapFondo;

    //Bitmap auxiliar que utilizamos para luego rescalar la imagen al tamaño de la pantalla
    Bitmap aux;

    //Objetos de la clase pantallaAviso para generar cuadros de dialogo
    pantallaAvisos avisoDineroOffline, cuadroConBotones;


    //Variables con los valores que rescatamos del shared preference
    int money, dineroPulsacion, autoclick, tiempoAutoclick, moneyOffline;
    int costoMejoraPulsacion, costoMejoraAutoclick, costoTiempoAutoclick;

    int horaAn, minutosAn, diffTiempo, diffMin;

    //Objeto de tipo trabajadores
    Trabajadores trabajadores = new Trabajadores();



    //Se utiliza para la consistencia de datos
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    //Tiempo actual
    Date currentTime;



    //CONSTRUCTOR
    public Escenas(int numEscena, Context context, int altoPantalla, int anchoPantalla) {
        this.numEscena = numEscena;
        this.context = context;
        this.altoPantalla = altoPantalla;
        this.anchoPantalla = anchoPantalla;

        //Control de sonido
        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if((android.os.Build.VERSION.SDK_INT) >= 21){
            SoundPool.Builder spb=new SoundPool.Builder();
            spb.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build());
            spb.setMaxStreams(maxSonidosSimultaneos);
            this.efectos = spb.build();
        }//end if
        else{
            this.efectos = new SoundPool(maxSonidosSimultaneos, AudioManager.STREAM_MUSIC, 0);
        }//end else
        sonidoCoin = efectos.load(context, R.raw.coin,1);

        //USAR SHARED PREFERENCES PARA LA CONSISTENCIA DE DATOS
        //Estoy pensado que como getSharedPreferences te deja tener mas de un archivo
        //Separar datos que si se pueden borrar para empezar pero otros guardarlos como los logros
        //y cosas asi.
        preferences = context.getSharedPreferences("Mis datos", Context.MODE_PRIVATE);
        //Objecto de edición del shared preference
        editor = preferences.edit();

        //Tiempo actual, para hacer toda la parte de las mejoras offline
        //(Gethour y asi está deprecated, y la otra opción que encontre no la soporta
        //la api para la que estoy haciendo la appp
        //Investiga lo de la api y toda esa puta basura
        currentTime = Calendar.getInstance().getTime();


        //Utilizamos el shared Preference para darle valor a las variables
        //Utilizo los valores por defecto por si es la primera vez que se juega o por si
        //se han borrado los datos
        //Este ciclo lo repite en el constructor, por lo tanto se hace cada vez que se
        //cambia de escena conservando así todos los cambios entre ellas
        //Cambiar todo este codigo a un metodo o una clase
        money = preferences.getInt("money", 0);
        dineroPulsacion = preferences.getInt("dineroPulsacion", 1);
        autoclick = preferences.getInt("autoclick", 0);
        tiempoAutoclick = preferences.getInt("tiempoAutoClick", 2000);
        costoMejoraPulsacion = preferences.getInt("costoMejoraPulsacion", 4);
        costoMejoraAutoclick = preferences.getInt("costoMejoraAutoclick", 30);
        costoTiempoAutoclick = preferences.getInt("costoTiempoAutoClick", 300);

        //Valores de la clase Trabajadores
        trabajadores.numero = preferences.getInt("numeroTrabajadores", 5);
        trabajadores.energia = preferences.getInt("energiaTrabajadores", 100);
        trabajadores.salud = preferences.getInt("saludTrabajadores", 100);
        trabajadores.salario = preferences.getInt("salarioTbj", 1500);
        trabajadores.dineroBase = preferences.getInt("dineroBase", 100);
        trabajadores.eficiencia = preferences.getInt("eficienciaTbj", 5);
        trabajadores.tiempo = preferences.getInt("tiempoTbj", 240);
        trabajadores.costeEnergia = preferences.getInt("costeEnergiaTbj", 1);

        //Tiempo de la ultima conexión, si no hay una guardada se guarda la actual
        horaAn = preferences.getInt("horaAn", currentTime.getHours());
        minutosAn = preferences.getInt("minutosAn", currentTime.getMinutes());
        //Dinero que se gana offline
        moneyOffline = preferences.getInt("moneyOffline", 0);

        //detector de gestos
        detectorDeGestos = new GestureDetectorCompat(context, new DetectorDeGestos());


        Log.i("tiempo", "Ultima hora de sesión : " + horaAn + " : " + minutosAn);
        Log.i("tiempo", "Sesión actual : " + currentTime.getHours() + " : " + currentTime.getMinutes());

    }//end constructor


    public void dibujar(Canvas c){
        //Atributos de los pinceles
        pincelTxt.setTextSize(70);
        pincelTxt.setAntiAlias(true);
        pincelTxt.setTextAlign(Paint.Align.CENTER);
        pincelTxt.setColor(Color.BLACK);
        pincelRec.setColor(Color.GREEN);
        try{
            c.drawText("Dinero = " + money,anchoPantalla/2, 100, pincelTxt);
        }//end try
        catch(Exception e){
            e.printStackTrace();
        }//end catch
    }//end method dibujar


    //Metodo que se llama en el momento antes de la destrucción de la
    //superficie de dibujo para guardar datos en el shared preference
    //Tambien se utilizara en un botón del menú de opciones para guardar cada vez que el usuario
    //quiera
    public void guardarDatos(){

        editor.putInt("horaAn", currentTime.getHours());
        editor.putInt("minutosAn", currentTime.getMinutes());
        editor.commit();
    }//end method guardarDatos


    //Metodo que controla el tiempo transcurrido desde la ultima conexión
    public void controlTemporal(){

        diffTiempo = currentTime.getHours() - horaAn;
        //si difftiempo = 0 significa que no a pasado una hora, por lo tanto los minutos se tienen que calcular
        //de forma diferente
        if(diffTiempo == 0) {
            diffMin = currentTime.getMinutes() - minutosAn;
            diffTiempo += diffMin;
            Log.i("tiempo", "" + diffTiempo);
        }//end if
        else{
            if(diffTiempo > 0) {
                //Se pasan las horas a minutos
                diffTiempo *= 60;
                diffMin = minutosAn - currentTime.getMinutes();
                //Segun la diferencia entre minutos calculamos el tiempo total que ha pasado
                if(diffMin > 0){
                    //si es positivo significa que no han pasado las horas completas, por lo que
                    //restamos al total
                    diffTiempo -= diffMin;
                    Log.i("tiempo", "" + diffTiempo);
                }//end if
                else{
                    //por el contrario se le suma ya que han pasado las horas justas mas algunos minutos
                    //tambien lo utilizamos si es 0
                    diffTiempo += diffMin;
                    Log.i("tiempo", "" + diffTiempo);
                }//end else
            }//end if
            //si despues de calcular la diferencia de tiempo el valor es negativo significa que han pasado
            //24h o mas
            else {
                Log.i("tiempo", "han pasado 24h");
                diffTiempo = 1440;
            }//end else
        }//end else
    }//end method control temporal





    //Metodo que calcula los beneficios que se generan de forma offline
    public void calcularDatos(){

        //primero calculamos el dinero que generan cada trabajador
        //Esto dependera de la salud de los trabajadores
        trabajadores.gananciasTrabajador();

        //Antes de hacer ningun cambio o calculo mas, calculamos cuantos ciclos son capaces de hacer
        //los trabajadores antes de que se les acabe la energia
        trabajadores.ciclosDisponibles(diffTiempo);

        //despues de calcular cuanto gana cada trabajador y cada cuanto ya podemos sumar el dinero
        //ganado a la variable money
        //Tenemos que tener en cuenta la diferencia entre los ciclos disponibles y los completados

        money += trabajadores.dineroCiclo * trabajadores.ciclosCompletados;
        moneyOffline = trabajadores.dineroCiclo * trabajadores.ciclosCompletados;
        trabajadores.energia -= trabajadores.ciclosCompletados * trabajadores.costeEnergia;

        /*
        Log.i("tiempo", "salud = " + trabajadores.salud);
        Log.i("tiempo", "dineroBase = " + trabajadores.dineroBase);
        Log.i("tiempo", "ganancias = " + trabajadores.ganancias);
        Log.i("tiempo", "ciclosDisponibles = " + trabajadores.ciclosDisponibles);
        Log.i("tiempo", "ciclosCompletados = " + trabajadores.ciclosCompletados);
        Log.i("tiempo", "calculo" + (trabajadores.dineroCiclo * trabajadores.ciclosCompletados));
        Log.i("tiempo", "moneyOffline " + moneyOffline);
        Log.i("tiempo", "energia trabajadores " + trabajadores.energia);
        */

        if(moneyOffline > 0){
            trabajadores.mensajeBeneficios = true;
        }//end if


        //Actualización de los datos en el shared preference
        editor.putInt("energiaTbj", trabajadores.energia);
        editor.putInt("money", money);
        editor.putInt("moneyOffline", moneyOffline);
        editor.commit();



    }//end method calcular Datos



    public void actualizarFisica(){
    }


    public int  onTouchEvent(MotionEvent event){

        return numEscena;
    }//end onTouchEvent

}//end class Escenas
