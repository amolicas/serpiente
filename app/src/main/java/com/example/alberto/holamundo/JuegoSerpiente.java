package com.example.alberto.holamundo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ALBERTO on 08/08/2016.
 */
public class JuegoSerpiente extends View {

    //Variables que definen el tamaño del cuadro de juego
    int campox, campoy;

    //Variables que definen las vidas que tienen cada jugador
    int vidas1;

    //Variable que almacena el ciclo de reloj del juego
    int ciclo, acelerar;

    //Otras variables del juego
    int crecimiento, longitudinicial, tamañocuadrado;
    long tiempo, retardo;

    //Variable boolean que define si estamos en modo multiplayer o no, de momento no hace falta
    //boolean multiplayer;

    //Definición de dos serpientes
    Serpiente serpiente;//, serpiente2;

    //Definición de una manzana, que siempre sera la misma pero ira variando su posición cuando se la coma alguien
    Manzana manzana;

    //Definicion TextView para debug y para mostrar información
    TextView tvdebug, tvmensaje, tvpuntuacion, tvvidas;

    //Variables para gestionar cuando se pierde una vida y cuando se pierden todas
    boolean mensajevidas, mensajefin;

    //Variables para pausar el juego, e indicador de inicio
    boolean pausa, iniciado;

    //Variables para almacenar los distintos sprites del juego
    Bitmap bmpcabeza, bmpcuerpo1, bmpcuerpo2, bmpcuerpo3, bmpcuerpo4, bmpcuerpo5, bmpcuerpo6, bmpmanzana;
    MediaPlayer mpcomer, mpmorir, mpmusicafondo;

    //Constructor que inicializa todas las variables del juego
    //public JuegoSerpiente(Context context, int CAMPOX, int CAMPOY, int VIDAS){
    public JuegoSerpiente(Context context) {
        super(context);
        //campox=this.getWidth();
        //campoy=this.getHeight();
        vidas1 = 4;
        longitudinicial = 10;
        crecimiento = 5;
        retardo = 400;
        tamañocuadrado = 80;
        //serpiente=new Serpiente(tamañocuadrado*3,tamañocuadrado*3,longitudinicial,tamañocuadrado);
        serpiente = new Serpiente();
        manzana = new Manzana();
        manzana.grosor = 0;
        manzana.posy = -100;
        manzana.posx = -100;
        ciclo = 0;
        acelerar = 40;
        mensajevidas = false;
        mensajefin = false;
        pausa = false;
        iniciado = false;
    }

    public void reiniciarCampo() {
        //Vacia el cuerpo de la serpiente
        while (!serpiente.cuerpo.isEmpty()) {
            serpiente.cuerpo.remove(0);
        }

        //Iniciación de variables que rigen el fin del juego
        mensajevidas = false;
        mensajefin = false;

        //Posición y dirección inicial de la serpiente
        serpiente.cabeza.posx = tamañocuadrado * 3;
        serpiente.cabeza.posy = tamañocuadrado * 3;
        serpiente.direccion = 1;
        serpiente.colision = false;
        ciclo = 0;
    }

    public void setCuadrado(int cuad) {
        //Calcular el tamaño del cuadrado
        tamañocuadrado = (cuad * tamañocuadrado / 800);

        //Inicialización del groso de la serpiente y de la manzana
        serpiente.grosor = tamañocuadrado;
        manzana.grosor = tamañocuadrado;

        //Inicialización de las imagenes para dibujar con el tamaño del cuadrado calculado
        bmpcabeza = BitmapFactory.decodeResource(getResources(),
                R.mipmap.cabeza);
        bmpcabeza = redimensionarImagenMaximo(bmpcabeza, (float) tamañocuadrado, (float) tamañocuadrado);
        serpiente.bmpcabeza = bmpcabeza;

        bmpcuerpo1 = BitmapFactory.decodeResource(getResources(),
                R.mipmap.cuerpo1);
        bmpcuerpo1 = redimensionarImagenMaximo(bmpcuerpo1, (float) tamañocuadrado, (float) tamañocuadrado);
        serpiente.bmpcuerpo1 = bmpcuerpo1;

        bmpcuerpo2 = BitmapFactory.decodeResource(getResources(),
                R.mipmap.cuerpo2);
        bmpcuerpo2 = redimensionarImagenMaximo(bmpcuerpo2, (float) tamañocuadrado, (float) tamañocuadrado);
        serpiente.bmpcuerpo2 = bmpcuerpo2;

        bmpcuerpo3 = BitmapFactory.decodeResource(getResources(),
                R.mipmap.cuerpo3);
        bmpcuerpo3 = redimensionarImagenMaximo(bmpcuerpo3, (float) tamañocuadrado, (float) tamañocuadrado);
        serpiente.bmpcuerpo3 = bmpcuerpo3;

        bmpcuerpo4 = BitmapFactory.decodeResource(getResources(),
                R.mipmap.cuerpo4);
        bmpcuerpo4 = redimensionarImagenMaximo(bmpcuerpo4, (float) tamañocuadrado, (float) tamañocuadrado);
        serpiente.bmpcuerpo4 = bmpcuerpo4;

        bmpcuerpo5 = BitmapFactory.decodeResource(getResources(),
                R.mipmap.cuerpo5);
        bmpcuerpo5 = redimensionarImagenMaximo(bmpcuerpo5, (float) tamañocuadrado, (float) tamañocuadrado);
        serpiente.bmpcuerpo5 = bmpcuerpo5;

        bmpcuerpo6 = BitmapFactory.decodeResource(getResources(),
                R.mipmap.cuerpo6);
        bmpcuerpo6 = redimensionarImagenMaximo(bmpcuerpo6, (float) tamañocuadrado, (float) tamañocuadrado);
        serpiente.bmpcuerpo6 = bmpcuerpo6;

        bmpmanzana = BitmapFactory.decodeResource(getResources(),
                R.mipmap.manzana);
        bmpmanzana = redimensionarImagenMaximo(bmpmanzana, (float) tamañocuadrado, (float) tamañocuadrado);
        manzana.bmpmanzana = bmpmanzana;

        //Inicializa la longitud inicial para la serpiente
        serpiente.longitud = longitudinicial;
    }

    public boolean hayColision() {
        //La serpiente calcula si la serpiente choca contra si misma
        if (serpiente.hayColision()) return true;

        //Tratamiento de la cabeza de la serpiente cuando sobrepasa los limites del tablero para que salga por el lado contrario
        int auxajuste;
        if (serpiente.cabeza.posx > campox) {
            serpiente.cabeza.posx = 0;
        }
        if (serpiente.cabeza.posy > campoy) {
            serpiente.cabeza.posy = 0;
        }
        if (serpiente.cabeza.posy < 0) {
            auxajuste = Math.abs(campoy / serpiente.grosor);
            auxajuste = auxajuste * serpiente.grosor;
            serpiente.cabeza.posy = auxajuste;
        }
        if (serpiente.cabeza.posx < 0) {
            auxajuste = Math.abs(campox / serpiente.grosor);
            auxajuste = auxajuste * serpiente.grosor;
            serpiente.cabeza.posx = auxajuste;
        }
        return false;
    }

    //Función que devuelve true si se esta comiendo la manzana y false si no se la come
    public boolean comeManzana() {
        if ((serpiente.cabeza.posx == manzana.posx) && (serpiente.cabeza.posy == manzana.posy)) {
            return true;
        } else return false;
    }

    //Método que ejecuta el cambio de direccion, es llamado cuando el usuario presiona sobre la pnatalla
    public void capturaUsuario(int x, int y) {
        serpiente.cambiaDireccion(x, y);
    }

    // Método para actualizar la logica del juego
    public void actualizarLogica() {
        //Condicional: Se actualizara la lógica solo cuando pase el tiempo establecido para el ciclo
        if (System.currentTimeMillis() > (tiempo + retardo)) {
            //Aparición de la primera manzana en el ciclo 5 del juego
            if (ciclo == 5) {
                manzana.nuevaManzana(campox, campoy, serpiente);
            }
            //Reseteamos el tiempo para el cálculo del siguiente ciclo
            tiempo = System.currentTimeMillis();
            //Condicional: Si no están activados los mensajes de perdida de vida o fin de la partida se continua con la partida
            //En caso contrario se gestiona un periodo sin jugar en el cual se seguirá mostrando el mensaje correspiente
            if ((!mensajevidas) && (!mensajefin)) {
                serpiente.moverSerpiente();
                postInvalidate();
                if (hayColision()) {
                    mpmorir.start();
                    if (vidas1 > 0) {
                        vidas1--;
                        mensajevidas = true;
                    } else if (vidas1 == 0) {
                        mensajefin = true;
                    }
                }
                if (comeManzana()) {
                    mpcomer.start();
                    serpiente.longitud = serpiente.longitud + crecimiento;
                    retardo = retardo - (retardo / acelerar);
                    manzana.nuevaManzana(campox, campoy, serpiente);
                }
            } else {
                //Si el mensaje activado es el de vidas se reinicia el campo para seguir jugando
                //y si esta activado mensaje fin, lo que se hace es poner también mensajevidas en true para indicar
                //que se ha acabado la partida y hay que salir al menu principal
                if (mensajevidas) {
                    retardar(3000);
                    mensajevidas = false;
                    manzana.posx = -100;
                    manzana.posy = -100;
                    reiniciarCampo();
                } else if (mensajefin) {
                    retardar(1000);
                    mensajevidas = true;
                    pausa = true;
                }
            }
            ciclo++;
        }
    }

    //Procedimiento para crear un hilo que se ejecutara para controlar la logica del juego
    public void loop() {
        tiempo = System.currentTimeMillis();
        //retardo = 400;

        //Inicializacion de la serpiente atendiendo a una longitud fija predeterminada
        reiniciarCampo();
        invalidate();
        tvmensaje.setVisibility(VISIBLE);

        //Inicialización de las dimensiones del campo, aqui no se las espera todavia
        //campox = getWidth();
        //campoy = getHeight();

        Runnable runnable = new Runnable() {
            public void run() {
                //Solo saldrá del bucle de juego si los dos mensajes están activados
                while ((!mensajefin) || (!mensajevidas)) {
                    if (!pausa) actualizarLogica();
                }
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }


    //Procedimiento para dibujar la partida
    protected void onDraw(Canvas canvas) {
        //Llamada a los metodos de dibujo de la serpiente y manzana
        serpiente.dibujar(canvas);
        manzana.dibujar(canvas);

        //Ajuste del ancho y alto del campo para que el campo este acotado y no sobren espacios
        int auxajuste;
        auxajuste = Math.abs(getWidth() / serpiente.grosor);
        campox = auxajuste * serpiente.grosor;

        auxajuste = Math.abs(getHeight() / serpiente.grosor);
        campoy = auxajuste * serpiente.grosor;

        //Muestra TextViews para debug
        //String auxdebug= String.valueOf(manzana.posx) + " " + String.valueOf(manzana.posy) + " " + String.valueOf((manzana.grosor) + " " + String.valueOf(serpiente.cabeza.posx) + " " + String.valueOf((serpiente.cabeza.posy)) + " " + String.valueOf((serpiente.grosor)) + " " + String.valueOf(serpiente.longitud)) + " " + String.valueOf(campox) + " " + String.valueOf(campoy);
        //String auxdebug= " Puntuación:" + String.valueOf(serpiente.longitud) + "     Vidas:" + String.valueOf(vidas1);
        //tvdebug.setText(auxdebug);

        //Muestra TextViews para el usuario
        tvdebug.setVisibility(INVISIBLE);
        String txtpuntacion = "Puntuación: " + String.valueOf(serpiente.longitud - longitudinicial);
        tvpuntuacion.setText(txtpuntacion);
        String txtvidas = "Vidas: " + String.valueOf(vidas1);
        tvvidas.setText(txtvidas);

        //Condicional si se ha activado mensajevidas es porque se ha perdido una vida
        //por lo tanto se muestra el mensaje de las vidas que le quedan al jugador
        //en caso contrario se asegura de dejar INVISIBlE el TextView
        if (mensajevidas) {
            tvmensaje.setVisibility(VISIBLE);
            String aux = "Te quedan " + String.valueOf(vidas1) + " vidas";
            tvmensaje.setText(aux);
        } else {
            if (iniciado) tvmensaje.setVisibility(INVISIBLE);
        }

        //Condicional si se ha activado mensajefin que quiere decir que se ha acabado la partida
        if ((mensajefin)) {
            tvmensaje.setVisibility(VISIBLE);
            String aux = "FIN" + "\n" + "Ya no te quedan vidas" + "\n" + "Pulsa la pantalla para salir";
            tvmensaje.setText(aux);
        }
    }

    public void pausar() {
        pausa = true;
    }

    public void reanudar() {
        pausa = false;
    }

    public void finalizar(){
        pausa = true;
        mensajefin = true;
        mensajevidas = true;
    }

    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth) {
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }

    public void retardar(long tretardo){
        while (System.currentTimeMillis()< (tiempo+tretardo));
    }
}