package com.example.TheMoneyMachine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import androidx.core.view.GestureDetectorCompat;


import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Escena padre que se encarga de cargar todos los recursos necesarios para las demas escenas
 */
public class Escenas {

    /**
     * Representa la escena actual
     */
    int numEscena;
    /**
     * Contexto de la aplicación
     */
    Context context;
    /**
     * Alto de la pantalla del dispositivo
     */
    int altoPantalla;
    /**
     * Ancho de la pantalla del dispositivo
     */
    int anchoPantalla;
    /**
     * Pincel del texto
     */
    Paint pincelTxt;
    /**
     * Objeto que permte cambiar el tipo de fuente
     */
    Typeface faw;
    /**
     * Pincel de los cuadrados
     */
    Paint pincelRec;
    /**
     * Pincel de la imagen de fondo
     */
    Paint pincelFondo;
    /**
     * Pincel del cuadro de pantalla aviso
     */
    Paint pincelCuadro;
    /**
     * Pincel del texto de pantalla aviso
     */
    Paint pincelTexto;
    /**
     * objeto que representa el vibrador del dispositivo
     */
    Vibrator vibrator;
    /**
     * Objecto que permite generar sonidos
     */
    public AudioManager audioManager;
    /**
     * Coleccion de sonidos
     */
    public SoundPool efectos;
    /**
     * Representa un archivo de audio oon el sonido de una moneda
     */
    public int sonidoCoin;
    /**
     * Maximo de sonidos simultaneos
     */
    public int maxSonidosSimultaneos = 10;
    /**
     * objeto encargado de capturar los gestos que se producen en la pantalla
     */
    public GestureDetectorCompat detectorDeGestos;
    /**
     * Imagen de fondo
     */
    Bitmap bitmapFondo;
    /**
     * bitmap auxiliar utilizado para redimensionar las imagenes
     */
    Bitmap aux;
    /**
     * Objeto que permite generar una pantalla de aviso
     */
    pantallaAvisos avisoDineroOffline;
    /**
     * Objeto que permite generar una pantalla de aviso
     * En este caso con botones
     */
    pantallaAvisos cuadroConBotones;
    /**
     * Objeto que permite generar una pantalla de aviso
     */
    pantallaAvisos cuadroAviso;
    /**
     * Dinero que posee el jugador
     */
    int money;
    /**
     * Dinero generado con cada pulsación
     */
    int dineroPulsacion;
    /**
     * Dinero que se genera con el autoclick
     */
    int autoclick;
    /**
     * Cada cuanto tiempo se produce el autoclick
     */
    int tiempoAutoclick;
    /**
     * Cuando dinero se ha ganado offline
     */
    int moneyOffline;
    /**
     * Dinero necesario para mejorar cuanto dinero que gana con una pulsación
     */
    int costoMejoraPulsacion;
    /**
     * Dinero necesario para mejorar cuanto genera el autoclick
     */
    int costoMejoraAutoclick;
    /**
     * Dinero necesario para mejorar cada cuanto se produce el autoclick
     */
    int costoTiempoAutoclick;
    /**
     * Dinero necesario para rellenar la energia
     */
    int rellenarEnergia;
    Boolean bloqueado;
    /**
     * Hora de la ultima conexión
     */
    int horaAn;
    /**
     * Minutos de la ultima conexión
     */
    int minutosAn;
    /**
     * Diferencia de tiempo entre la ultima hora de conexión y la actual
     */
    int diffTiempo;
    /**
     * Diferencia de minutos entre la ultima hora de conexión y la actual
     */
    int diffMin;
    /**
     * Objeto que representa a los trabajadores
     */
    Trabajadores trabajadores = new Trabajadores();
    /**
     * Objeto donde se guardan todos los datos
     */
    SharedPreferences preferences;
    /*
     * Objeto que permite editar el sharedPreferences
     */
    SharedPreferences.Editor editor;
    /**
     * Fecha actual
     */
    Date currentTime;
    /**
     * Booleana que indica si el evento 1 ya a ocurrido
     */
    boolean flag1;
    /**
     * Booleana que indica si el evento 2 ya a ocurrido
     */
    boolean flag2;
    /**
     * Booleana que indica si el evento 3 ya a ocurrido
     */
    boolean flag3;
    /**
     * Booleana que indica si el evento 4 ya a ocurrido
     */
    boolean flag4;
    /**
     * Booleana que indica si el idioma activo es el español o el ingles
     */
    boolean ingles;

    /**
     * Constructor de la clase
     * @param numEscena numero de escena actual
     * @param context contexto de la aplicación
     * @param altoPantalla alto de la pantalla del dispositivo
     * @param anchoPantalla ancho de la pantalla del dispositivo
     */
    public Escenas(int numEscena, Context context, int altoPantalla, int anchoPantalla) {
        this.numEscena = numEscena;
        this.context = context;
        this.altoPantalla = altoPantalla;
        this.anchoPantalla = anchoPantalla;
        pincelTxt = new Paint();
        pincelCuadro = new Paint();
        pincelFondo = new Paint();
        pincelRec = new Paint();
        pincelTexto = new Paint();
        pincelFondo.setAlpha(150);
        pincelCuadro.setColor(Color.BLACK);
        pincelTexto.setColor(Color.WHITE);
        pincelTexto.setTextAlign(Paint.Align.CENTER);
        pincelTexto.setTextSize(40);
        pincelTexto.setAntiAlias(true);
        //Atributos de los pinceles
        //Cambiamos la fuente al pincel que utilizamos para pintar el texto
        faw = Typeface.createFromAsset(context.getAssets(), "acknowtt.ttf");
        pincelTxt.setTypeface(faw);
        pincelTxt.setTextSize(80);
        pincelTxt.setAntiAlias(true);
        pincelTxt.setTextAlign(Paint.Align.CENTER);
        pincelTxt.setColor(Color.BLACK);
        pincelRec.setColor(Color.GREEN);
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
        //SHARED PREFERENCES PARA LA CONSISTENCIA DE DATOS
        preferences = context.getSharedPreferences("Mis datos", Context.MODE_PRIVATE);
        //Objecto de edición del shared preference
        editor = preferences.edit();
        //Tiempo actual
        currentTime = Calendar.getInstance().getTime();
        cuadroConBotones = new pantallaAvisos(altoPantalla,anchoPantalla, "", context, pincelFondo, pincelCuadro, pincelTexto);
        cuadroAviso = new pantallaAvisos(altoPantalla,anchoPantalla, "", context, pincelFondo, pincelCuadro, pincelTexto);
        //Utilizamos el shared Preference para darle valor a las variables
        //Utilizo los valores por defecto por si es la primera vez que se juega o por si
        //se han borrado los datos
        //Este ciclo lo repite en el constructor, por lo tanto se hace cada vez que se
        //cambia de escena conservando así todos los cambios entre ellas
        money = preferences.getInt("money", 0);
        dineroPulsacion = preferences.getInt("dineroPulsacion", 1);
        autoclick = preferences.getInt("autoclick", 0);
        tiempoAutoclick = preferences.getInt("tiempoAutoClick", 2000);
        costoMejoraPulsacion = preferences.getInt("costoMejoraPulsacion", 4);
        costoMejoraAutoclick = preferences.getInt("costoMejoraAutoclick", 70);
        costoTiempoAutoclick = preferences.getInt("costoTiempoAutoClick", 600);
        rellenarEnergia = preferences.getInt("rellenarEnergia", 1000);
        bloqueado = preferences.getBoolean("bloqueado", false);
        //Valores de la clase Trabajadores
        trabajadores.numero = preferences.getInt("numeroTrabajadores", 1);
        trabajadores.energia = preferences.getInt("energiaTrabajadores", 100);
        trabajadores.salud = preferences.getInt("saludTrabajadores", 50);
        trabajadores.salario = preferences.getInt("salarioTbj", 1500);
        trabajadores.dineroBase = preferences.getInt("dineroBase", 100);
        trabajadores.eficiencia = preferences.getInt("eficienciaTbj", 5);
        trabajadores.tiempo = preferences.getInt("tiempoTbj", 240);
        trabajadores.costeEnergia = preferences.getInt("costeEnergiaTbj", 1);
        //Tiempo de la ultima conexión, si no hay una guardada se guarda la actual
        //PUNTO DE DEPURACIÓN//
        //Cuando se instala por primera vez los valores son erroneos, no corre la hora actual
        //y los minutos sino otra totalmente diferente
        horaAn = preferences.getInt("horaAn", currentTime.getHours());
        minutosAn = preferences.getInt("minutosAn", currentTime.getMinutes());

        //Dinero que se gana offline
        moneyOffline = preferences.getInt("moneyOffline", 0);
        //Avisos
        flag1 = preferences.getBoolean("flag1", true);
        flag2 = preferences.getBoolean("flag2", true);
        flag3 = preferences.getBoolean("flag3", true);
        flag4 = preferences.getBoolean("flag4", true);
        ingles = preferences.getBoolean("ingles", false);
        if(ingles){
            setAppLocale("en");
        }else {
            setAppLocale("es_ES");
        }//end else
        //Vibrador
         vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        //detector de gestos
        detectorDeGestos = new GestureDetectorCompat(context, new DetectorDeGestos());
    }//end constructor

    /**
     * Sirve para cambiar la localidad del dispositivo
     * @param localCode codigo que indica a que región se quiere cambiar
     */
    public void setAppLocale(String localCode){
        Resources res = context.getResources();
        DisplayMetrics ds = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localCode.toLowerCase()));
        res.updateConfiguration(conf, ds);
    }//end method setAppLocale

    /**
     * Metodo que se utiliza para dibujar en el canvas.
     * @param c  Canvas en el que se va a dibujar
     */
    public void dibujar(Canvas c){
        try{
            if(numEscena == 2)
                pincelTxt.setColor(Color.WHITE);
            c.drawText( context.getResources().getString(R.string.dinero) + " " + money,anchoPantalla/2, 100, pincelTxt);
        }//end try
        catch(Exception e){
            e.printStackTrace();
        }//end catch

    }//end method dibujar

    /**
     *Se llama antes de la destrucción de la superficie de dibujo y sirve para guardar datos
     */
    public void guardarDatos(){

        editor.putInt("horaAn", currentTime.getHours());
        editor.putInt("minutosAn", currentTime.getMinutes());
        editor.commit();
    }//end method guardarDatos

    /**
     * Calcula la diferencia de tiempo entre la ultima hora de
     * conexión y la actual y guarda ese valor en la variable
     * diffTiempo.
     * diffTiempo se mide en minutos.
     */
    public void controlTemporal(){
        diffTiempo = currentTime.getHours() - horaAn;
        //si difftiempo = 0 significa que no a pasado una hora, por lo tanto los minutos se tienen que calcular
        //de forma diferente
        if(diffTiempo == 0) {
            diffMin = currentTime.getMinutes() - minutosAn;
            diffTiempo += diffMin;
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
                }//end if
                else{
                    //por el contrario se le suma ya que han pasado las horas justas mas algunos minutos
                    //tambien lo utilizamos si es 0
                    diffTiempo += diffMin;
                    }//end else
            }//end if
            //si despues de calcular la diferencia de tiempo el valor es negativo significa que han pasado
            //24h o mas
            else {
                diffTiempo = 1440;
            }//end else
        }//end else
    }//end method control temporal

    /**
     * Calcula los beneficioas que se generan  de forma offline
     */
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
        if(moneyOffline > 0){
            trabajadores.mensajeBeneficios = true;
        }//end if
        //Actualización de los datos en el shared preference
        editor.putInt("energiaTrabajadores", trabajadores.energia);
        editor.putInt("money", money);
        editor.putInt("moneyOffline", moneyOffline);
        editor.commit();
    }//end method calcular Datos

    /**
     * Metodo que se lanza cuando se produce una pulsación en la pantalla
     * @param event evento de la pulsación
     * @return devuelve el número de escena que se debe dibujar
     */
    public int  onTouchEvent(MotionEvent event){
        return numEscena;
    }//end onTouchEvent


    public void actualizarFisica(){

    }//end actualizarFisica
}//end class Escenas
