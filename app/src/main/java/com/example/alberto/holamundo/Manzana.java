package com.example.alberto.holamundo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by ALBERTO on 08/08/2016.
 */
public class Manzana {
    int posx, posy, grosor;
    Bitmap bmpmanzana;

    public void Manzana(){
        posx=100000;
        posy=100000;
        grosor=0;
    }

    public void Manzana(int x, int y){
        posx=x;
        posy=y;
        grosor=0;
    }

    public void nuevaManzana(int maxX, int maxY, Serpiente serpiente){
        boolean manbuenza=false;
        while (!manbuenza) {
            posx = (int) Math.floor(Math.random() * (0 - maxX) + maxX);
            posx = Math.abs(posx / grosor);
            posx = posx * grosor;

            posy = (int) Math.floor(Math.random() * (0 - maxY) + maxY);
            posy = Math.abs(posy / grosor);
            posy = posy * grosor;
            if (!serpiente.posicionOcupada(posx, posy)) manbuenza = true;
        }
    }

    public void dibujar(Canvas canvas){
        Paint pincel1 = new Paint();
        pincel1.setARGB(255, 0, 0, 0);
        canvas.drawBitmap(bmpmanzana, posx, posy, null);
        //canvas.drawCircle(posx+(grosor/2), posy+(grosor/2), grosor/2, pincel1);

    }
}
