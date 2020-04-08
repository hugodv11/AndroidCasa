package com.example.TheMoneyMachine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

/**
 * Clase principal del proyecto
 */
public class MainActivity extends AppCompatActivity {


    public static PantallaPrincipal pantalla;
    public static int opciones;

    /**
     * Se lanza cuando se crea la pantalla
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pantalla = new PantallaPrincipal(this);
        pantalla.setKeepScreenOn(true);
        setContentView(pantalla);
        //Para poner la aplicación en pantalla completa
        View decorView = getWindow().getDecorView();
        opciones = View.SYSTEM_UI_FLAG_FULLSCREEN //Pone la pantalla en modo pantalla completa
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Oculta la barra de navegación
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(opciones);
    }//end onCreated

    @Override
    protected void onPause() {
        pantalla.pararMusica();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        pantalla.iniciarMusica();
        //Para poner la aplicación en pantalla completa
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(opciones);
        super.onRestart();
    }
}//end class MainActivity
