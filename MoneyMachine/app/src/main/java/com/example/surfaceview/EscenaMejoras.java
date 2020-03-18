package com.example.surfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.HashMap;

public class EscenaMejoras extends Escenas {

    Rect btnVolver, mejoraPulsación, mejoraAutoclick, mejoraTiempoAutoClick;
    Bitmap bitmapPulsacion, bitmapAutoclick, bitmapCosteAutoClick, bitmapVolver;
    int separacion;


    //Constructor
    public EscenaMejoras(int numEscena, Context context, int altoPantalla, int anchoPantalla) {
        super(numEscena, context, altoPantalla, anchoPantalla);
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.mejoras);
        bitmapFondo = aux.createScaledBitmap(aux,anchoPantalla, altoPantalla,true);

        separacion = altoPantalla / 20;

        //Botones
        btnVolver = new Rect(0, 0, anchoPantalla/9,anchoPantalla/9);
        mejoraPulsación = new Rect(anchoPantalla/20, altoPantalla/10, anchoPantalla - anchoPantalla / 20    , (altoPantalla/10) * 2);
        mejoraTiempoAutoClick =new Rect(anchoPantalla/20, (altoPantalla / 10) * 3 - separacion, anchoPantalla - anchoPantalla / 20, (altoPantalla/10) * 4 - separacion);
        mejoraAutoclick = new Rect(anchoPantalla/20, (altoPantalla / 10) * 5 - (separacion * 2), anchoPantalla - anchoPantalla / 20, (altoPantalla/10) * 6 - (separacion * 2));

        //Bitmaps
        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.dollar);
        bitmapPulsacion = aux.createScaledBitmap(aux,mejoraPulsación.width()/4 , mejoraPulsación.height() - (mejoraPulsación.height() / 9),true);

        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.autoclick);
        bitmapAutoclick = aux.createScaledBitmap(aux,mejoraAutoclick.width()/4 , mejoraAutoclick.height() - (mejoraAutoclick.height() / 9),true);

        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.tiempo);
        bitmapCosteAutoClick = aux.createScaledBitmap(aux,mejoraTiempoAutoClick.width()/4 , mejoraTiempoAutoClick.height() - (mejoraTiempoAutoClick.height() / 9),true);

        aux = BitmapFactory.decodeResource(context.getResources(),R.drawable.izquierda);
        bitmapVolver = aux.createScaledBitmap(aux, btnVolver.width(),btnVolver.height(),true);




        //Muy importante mas ideas para mejorar parte clicker, para hacer más profunda la progresión

        //Probabilidad de critico: cierta probabilidad cada vez que se pulsa el botón de ganar un multiplicador.
        //Se puede mejorar la probabilidad o el multiplicador.

        //Al pulsar el boton de la pantalla principal crear algunos efectos especiales(+Dinero que salga del boton
        //y luego se desvanezca por ejemplo)

        //Los indicadores de la pantalla de trabajadores tienen que ser dinamicos, ajustandose a sus valores
        //Sueldo de los trabajadores, añadir ese calculo al metodo calcularDatos






        //Idea es dibujar las mejoras como si fuera un recycleView, en la parte izquierda el nombre y en la derecha
        //El coste y una pequeña descripción.(Nada muy loco, algo resulton pero sencillito).




    }//end constructor

    @Override
    public void dibujar(Canvas c) {
        //Aqui dibujamos
        c.drawBitmap(bitmapFondo,0, 0,null);
        pincelRec.setColor(Color.BLACK);
        pincelRec.setStyle(Paint.Style.STROKE);
        pincelRec.setStrokeWidth(20);

        c.drawRect(mejoraPulsación, pincelRec);
        c.drawRect(mejoraAutoclick, pincelRec);
        c.drawRect(mejoraTiempoAutoClick, pincelRec);
        //dibujamos imagenes
        c.drawBitmap(bitmapVolver,0,    0,null);
        c.drawBitmap(bitmapPulsacion, anchoPantalla/20, altoPantalla/10, null);
        c.drawBitmap(bitmapAutoclick, anchoPantalla/20, (altoPantalla / 10) * 3 - separacion, null);
        c.drawBitmap(bitmapCosteAutoClick, anchoPantalla/20, (altoPantalla / 10) * 5 - (separacion * 2), null);


        pincelTxt.setColor(Color.WHITE);
        c.drawText("Coste : " + costoMejoraPulsacion, mejoraPulsación.centerX() + anchoPantalla / 8, mejoraPulsación.exactCenterY() + pincelTxt.getTextSize() / 4, pincelTxt);
        c.drawText("Coste : " + costoMejoraAutoclick, mejoraAutoclick.centerX() + anchoPantalla / 8, mejoraAutoclick.exactCenterY() + pincelTxt.getTextSize() / 4, pincelTxt);
        c.drawText("Coste : " + costoTiempoAutoclick, mejoraTiempoAutoClick.centerX() + anchoPantalla / 8, mejoraTiempoAutoClick.exactCenterY() + pincelTxt.getTextSize() / 4, pincelTxt);
        super.dibujar(c);
    }//end dibujar

    @Override
    public void actualizarFisica() {
        super.actualizarFisica();
    }


    @Override
    public int onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        if(btnVolver.contains(x, y)) {
            return 1;
        }//end if
        if(mejoraPulsación.contains(x, y)) {
            if(money >= costoMejoraPulsacion){
                dineroPulsacion += 2;
                money -= costoMejoraPulsacion;
                costoMejoraPulsacion *= 2;
                editor.putInt("money", money);
                editor.putInt("dineroPulsacion", dineroPulsacion);
                editor.putInt("costoMejoraPulsacion", costoMejoraPulsacion);
                editor.commit();
            }//end if
        }//end if

        if(mejoraAutoclick.contains(x, y)) {
            if(money >= costoMejoraAutoclick){
                autoclick++;
                money -= costoMejoraAutoclick;
                costoMejoraAutoclick *= 4;
                editor.putInt("money", money);
                editor.putInt("autoclick", autoclick);
                editor.putInt("costoMejoraAutoclick", costoMejoraAutoclick);
                editor.commit();
            }//end if
        }//end if

        //Limitar cuanto se puede bajar el tiempo, ya que puede llegar a petar
        //(Por ejemplo, maximo nivel que el autoclick lo haga cada medio segundo)
        if(mejoraTiempoAutoClick.contains(x, y)){
            if(money >= costoTiempoAutoclick) {
                tiempoAutoclick -= 500;
                money -= costoTiempoAutoclick;
                costoTiempoAutoclick *= 5;
                editor.putInt("money", money);
                editor.putInt("tiempoAutoClick", tiempoAutoclick);
                editor.putInt("costoTiempoAutoClick", costoTiempoAutoclick);
                editor.commit();
            }//end if
        }//end if

        return numEscena;
    }//end onTouchEvent





}//end class EscenaMejoras
