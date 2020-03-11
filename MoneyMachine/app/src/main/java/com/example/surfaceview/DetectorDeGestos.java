package com.example.surfaceview;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import javax.security.auth.login.LoginException;

public class DetectorDeGestos extends GestureDetector.SimpleOnGestureListener {

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
