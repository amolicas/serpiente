package com.example.alberto.holamundo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by ALBERTO on 08/08/2016.
 */
public class Serpiente{
    int longitud, grosor;
    int direccion;//1.Derecha 2.Izquierda 3.Arriba 4.Abajo
    ArrayList<Trozocuerpo> cuerpo;
    Trozocuerpo cabeza;
    boolean bloqueadireccion, colision;
    Bitmap bmpcabeza, bmpcuerpo1, bmpcuerpo2, bmpcuerpo3, bmpcuerpo4, bmpcuerpo5, bmpcuerpo6;
    int sigimagen;

    public Serpiente(){
        cabeza = new Trozocuerpo(-100,-100);
        grosor=0;
        direccion=1;
        longitud=0;
        colision=false;
        bloqueadireccion=false;
        cuerpo = new ArrayList();
    }

    //Constructor de la serpiente con los valores iniciales de la cabeza, el tamaño y el grosor de la serpiente
    public Serpiente(int x, int y, int tamaño, int gorda){
        cabeza = new Trozocuerpo(x,y);
        grosor=gorda;
        direccion=1;
        longitud=tamaño;
        colision=false;
        bloqueadireccion=false;
        cuerpo = new ArrayList();
    }

    //Funciones que devuelven las esquinas de cada cuerpo de la serpiente
    //Sirven para dibujar el cuadrado de cada trozo de serpiente
    public int getleftTrozocuerpo(int poscuerpo){
        Trozocuerpo aux = cuerpo.get(poscuerpo);
        return aux.posx;
    }
    public int gettopTrozocuerpo(int poscuerpo){
        Trozocuerpo aux = cuerpo.get(poscuerpo);
        return aux.posy;
    }
    public int getrightTrozocuerpo(int poscuerpo){
        Trozocuerpo aux = cuerpo.get(poscuerpo);
        return aux.posx + grosor;
    }
    public int getbottomTrozocuerpo(int poscuerpo) {
        Trozocuerpo aux = cuerpo.get(poscuerpo);
        return aux.posy + grosor;
    }

    //Funciones que devuelven las esquinas de la cabeza de la serpiente
    //Sirven para dibujar el cuadrado de la cabeza de la serpiente
    public int getleftCabeza(){
        return cabeza.posx;
    }
    public int gettopCabeza(){
        return cabeza.posy;
    }
    public int getrightCabeza(){
        return cabeza.posx + grosor;
    }
    public int getbottomCabeza(){
        return cabeza.posy + grosor;
    }

    //Funciones que hacen avanzar la serpiente
    //Cada funcion hace avanzar la serpiente en una direccion
    public void avanzaRight(boolean crece){
        Trozocuerpo aux = new Trozocuerpo(cabeza.posx, cabeza.posy);
        if (sigimagen>0){
            aux.imagen=sigimagen;
        }else{
            aux.imagen=1;
        }
        cuerpo.add(aux);
        if (crece==false) cuerpo.remove(0);
        cabeza.posx = cabeza.posx+grosor;
        //- grosor;
        cabeza.posy = cabeza.posy;
    }
    public void avanzaUp(boolean crece){
        Trozocuerpo aux = new Trozocuerpo(cabeza.posx, cabeza.posy);
        if (sigimagen>0){
            aux.imagen=sigimagen;
        }else{
            aux.imagen=2;
        }
        cuerpo.add(aux);
        if (crece==false) cuerpo.remove(0);
        cabeza.posy = cabeza.posy-grosor;
        // - grosor;
    }
    public void avanzaLeft(boolean crece){
        Trozocuerpo aux = new Trozocuerpo(cabeza.posx, cabeza.posy);
        if (sigimagen>0){
            aux.imagen=sigimagen;
        }else{
            aux.imagen=1;
        }
        cuerpo.add(aux);
        if (crece==false) cuerpo.remove(0);
        cabeza.posx = cabeza.posx-grosor;
        // + grosor;
    }
    public void avanzaDown(boolean crece){
        Trozocuerpo aux = new Trozocuerpo(cabeza.posx, cabeza.posy);
        if (sigimagen>0){
            aux.imagen=sigimagen;
        }else{
            aux.imagen=2;
        }
        cuerpo.add(aux);
        if (crece==false) cuerpo.remove(0);
        cabeza.posy = cabeza.posy+grosor;
        // + grosor;
    }

    //Metodo que recibe la posicion de la pulsacion en pantalla y calcula el cambio de direccion de la serpiente
    //1.Derecha 2.Izquierda 3.Arriba 4.Abajo
    public void cambiaDireccion(int x, int y){
        if (!bloqueadireccion) {
            switch (direccion) {
                case 1: {
                    if (y > cabeza.posy) {
                        direccion = 4;
                        sigimagen = 3;
                    }
                    if (y < cabeza.posy) {
                        direccion = 3;
                        sigimagen = 4;
                    }
                    bloqueadireccion = true;
                    break;
                }
                case 2: {
                    if (y > cabeza.posy) {
                        direccion = 4;
                        sigimagen = 5;
                    }
                    if (y < cabeza.posy) {
                        direccion = 3;
                        sigimagen = 6;
                    }
                    bloqueadireccion = true;
                    break;
                }
                case 3: {
                    if (x > cabeza.posx) {
                        direccion = 1;
                        sigimagen = 5;
                    }
                    if (x < cabeza.posx) {
                        direccion = 2;
                        sigimagen = 3;
                    }
                    bloqueadireccion = true;
                    break;
                }
                case 4: {
                    if (x > cabeza.posx) {
                        direccion = 1;
                        sigimagen = 6;
                    }
                    if (x < cabeza.posx) {
                        direccion = 2;
                        sigimagen = 4;
                    }
                    bloqueadireccion = true;
                    break;
                }
            }
        }
    }

    //Metodo que hace mover la serpiente según la dirección que lleve en cada momento
    //También comprueba si existe alguna colision, salida del campo o si se come una manzana
    public void moverSerpiente(){
        //Calcula si la serpiente tiene que crecer y deja grabado el resultado en la variable crecer
        boolean crecer=false;
        if (cuerpo.size()<longitud) crecer=true;

        //Realiza el movimiento dependiendo de la direccion que esta siguiendo la serpiente
        if (direccion==1) avanzaRight(crecer);
        if (direccion==2) avanzaLeft(crecer);
        if (direccion==3) avanzaUp(crecer);
        if (direccion==4) avanzaDown(crecer);
        bloqueadireccion=false;
        sigimagen = 0;

        if (hayColision()) colision=true;
    }

    public boolean hayColision(){
        //Comprueba si hay una colision con el cuerpo de la serpiente
        Trozocuerpo auxtrozo = new Trozocuerpo(0,0);
        int cont=0;
        while (cont<cuerpo.size()){
            auxtrozo.posx = cuerpo.get(cont).posx;
            auxtrozo.posy = cuerpo.get(cont).posy;
            if ((cabeza.posx==auxtrozo.posx)&&(cabeza.posy==auxtrozo.posy)) colision=true;
            cont++;
        }
        return colision;

        //Comprueba si la cabeza de la serpiente se sale del campo permitido
        //if ((cabeza.posx<0)||(cabeza.posy<0)||(cabeza.posx>))

        //Comprueba si la serpiente se come una manzana

    }

    public boolean posicionOcupada(int x, int y){
        if ((cabeza.posx==x)&&(cabeza.posy==y)) return true;
        Trozocuerpo auxtrozo = new Trozocuerpo(0,0);
        int cont=0;
        while (cont<cuerpo.size()){
            auxtrozo.posx = cuerpo.get(cont).posx;
            auxtrozo.posy = cuerpo.get(cont).posy;
            if ((auxtrozo.posx==x)&&(auxtrozo.posy==y)) return true;
            cont++;
        }
        return false;
    }

    public void dibujar(Canvas canvas){
        Paint pincel1 = new Paint();
        pincel1.setARGB(255, 255, 132, 0);

        //Dibujar cabeza
        //canvas.drawRect(getleftCabeza(), gettopCabeza(), getrightCabeza(), getbottomCabeza(), pincel1);

        canvas.drawBitmap(bmpcabeza, cabeza.posx, cabeza.posy, null);

        //Dibujar resto del cuerpo
        int tam = cuerpo.size();
        int cont=0;
        while (cont<tam){
            //canvas.drawRect(getleftTrozocuerpo(cont), gettopTrozocuerpo(cont), getrightTrozocuerpo(cont), getbottomTrozocuerpo(cont), pincel1);
            Trozocuerpo aux = cuerpo.get(cont);
            aux.dibujar(canvas);
            //canvas.drawBitmap(bmpcuerpo, getleftTrozocuerpo(cont), gettopTrozocuerpo(cont), null);
            cont++;
        }
        //campox = canvas.getWidth();
        //campoy = canvas.getHeight();
        //canvas.drawCircle(manzana.posx-(serpiente.grosor/2), manzana.posy-(serpiente.grosor/2), serpiente.grosor/2, pincel1);


    }

    //Clase que define cada uno de las partes que componen la serpiente
    //Se define una cabeza y un array de trozos para el resto del cuerpo
    class Trozocuerpo{
        int posx, posy, imagen;
        public Trozocuerpo(int x, int y){
            posx=x;
            posy=y;
            imagen=0;//0:Sin imagen, 1: Horizontal, 2:Vertical, 3:GiroDerechaAbajo, 4:GiroDerechaArriba, 5:GiroIzquierdaAbajo, 6:GiroIzquierdaArriba
        }

        public void dibujar(Canvas canvas){
            Bitmap idibuja = bmpcuerpo1;
            switch (imagen){
                case 1:{
                    idibuja = bmpcuerpo1;
                    break;
                }
                case 2:{
                    idibuja = bmpcuerpo2;
                    break;
                }
                case 3:{
                    idibuja = bmpcuerpo3;
                    break;
                }
                case 4:{
                    idibuja = bmpcuerpo4;
                    break;
                }
                case 5:{
                    idibuja = bmpcuerpo5;
                    break;
                }
                case 6:{
                    idibuja = bmpcuerpo6;
                    break;
                }
            }

            canvas.drawBitmap(idibuja, posx, posy, null);
        }
    }
}
