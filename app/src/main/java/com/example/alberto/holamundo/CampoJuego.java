package com.example.alberto.holamundo;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CampoJuego extends AppCompatActivity implements View.OnTouchListener{
    private int corx, cory;
    private JuegoSerpiente juego;
    private TextView tvdebug, tvmensaje;
    private TextView tvpuntuacion, tvvidas;
    private Button btnsalir, btnpausa;
    private boolean iniciado;
    private MediaPlayer mpcomer, mpmorir, mpmusicafondo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campo_juego);

        //Quitamos barra de notificaciones
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Quitar la barra inferior
        View mDecorView = getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //Captura el boton de pausar para usarlo mas tarde
        btnpausa = (Button)findViewById(R.id.btnpausa);

        //Layout del campo de juego
        RelativeLayout layoutcampo =(RelativeLayout)findViewById(R.id.campo);

        //Captura de los campos de texto para mostrar información
        tvdebug = (TextView)findViewById(R.id.tvdebug);
        tvmensaje = (TextView)findViewById(R.id.tvmensaje);
        tvpuntuacion = (TextView)findViewById((R.id.tvpuntuacion));
        tvvidas = (TextView)findViewById(R.id.tvvidas);

        //Inicialización del juego serpiente
        juego=new JuegoSerpiente(this);
        juego.setOnTouchListener(this);
        Resources resources = getResources();
        int dips = 200;
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, resources.getDisplayMetrics());
        juego.setCuadrado((int)pixels);

        //Añadir el View del juego al layout del campo de juego
        layoutcampo.addView(juego);

        //Inicialización de textos por defecto
        String auxdebug= String.valueOf(String.valueOf(layoutcampo.getHeight()));
        tvdebug.setText(auxdebug);

        //Inicializacion de la variable que almacena si se ha iniciado el juego o no
        iniciado=false;
    }

    public void onStart(){
        super.onStart();
        //Pasar los TextView a la clase juego JuegoSerpiente para que los gestione
        juego.tvdebug = tvdebug;
        juego.tvmensaje = tvmensaje;
        juego.tvpuntuacion = tvpuntuacion;
        juego.tvvidas = tvvidas;

        // Inicializa los sonidos y los pasa al juego
        mpcomer = MediaPlayer.create(this, R.raw.niamniam);
        juego.mpcomer = mpcomer;

        mpmorir = MediaPlayer.create(this, R.raw.uiuiui);
        juego.mpmorir = mpmorir;

        //Trae al frente el TextView para mostrar mensajes
        tvmensaje.bringToFront();
        tvmensaje.setVisibility(View.VISIBLE);
        String aux = "Pulsa la pantalla para comenzar";
        tvmensaje.setText(aux);
    }

    public boolean onTouch(View v, MotionEvent event){
        if (!iniciado){
            tvmensaje.setVisibility(View.INVISIBLE);
            juego.loop();
            iniciado=true;
            juego.iniciado= true;
        }else{
            int action = MotionEventCompat.getActionMasked(event);
            if ((!juego.pausa)&&(!juego.mensajevidas)&&(!juego.mensajefin)) {
                if (action == MotionEvent.ACTION_DOWN) {
                    corx = (int) event.getX();
                    cory = (int) event.getY();
                    juego.capturaUsuario(corx, cory);
                }
            }
        }

        if (juego.mensajevidas&&juego.mensajefin){
            iniciado = false;
            juego.iniciado = false;
            finish();
        }
        return true;
    }

    public void pausarJuego(View view){
        String aux = (String)btnpausa.getText();
        if (aux.equalsIgnoreCase("Pausar")){
            btnpausa.setText("Reanudar");
            juego.pausar();
        }else if (aux.equalsIgnoreCase("Reanudar")){
            btnpausa.setText("Pausar");
            juego.reanudar();
        }
    }

    public void reanudarJuego(View view){
        juego.reanudar();
    }

    public void salirJuego(View view){
        juego.finalizar();
        finish();
    }

    public void onDestroy(){
        super.onDestroy();
        juego.finalizar();
    }
}