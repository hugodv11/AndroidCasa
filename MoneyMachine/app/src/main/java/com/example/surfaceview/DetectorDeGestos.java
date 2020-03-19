package com.example.surfaceview;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import javax.security.auth.login.LoginException;

/**
 * Clase que se encarga de capturar los gestos en pantalla
 */
public class DetectorDeGestos extends GestureDetector.SimpleOnGestureListener {

    /**
     * Se encarga de detectar los deslizamientos por la pantalla
     * @param e1
     * @param e2
     * @param velocityX velocidad en el eje x
     * @param velocityY velocidad en el eje y
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(velocityX < -10f) {
            return true;
        }//end if
        else{
            return false;
        }//end else
    }//end onFling
}//end class DetectorDeGestos
