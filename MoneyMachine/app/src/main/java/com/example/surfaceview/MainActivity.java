package com.example.surfaceview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //En algunas versiones de android es necesario el siguiente código para ocultar la barra de título
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        PruebaSurfaceView pantalla = new PruebaSurfaceView(this);
        pantalla.setKeepScreenOn(true);
        setContentView(pantalla);


        //Para poner la aplicación en pantalla completa
        View decorView = getWindow().getDecorView();
        int opciones = View.SYSTEM_UI_FLAG_FULLSCREEN //Pone la pantalla en modo pantalla completa
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Oculta la barra de navegación
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(opciones);



    }//end onCreated



}//end class MainActivity
