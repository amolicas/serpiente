package com.alberto.burgersnake;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int corx, cory;
    private JuegoSerpiente juego;
    private TextView tvdebug;
    MediaPlayer mpmusicafondo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        mpmusicafondo = MediaPlayer.create(this, R.raw.musicafondo);
        mpmusicafondo.setLooping(true);
        mpmusicafondo.start();

    }

    public void cargarCampoJuego(View view){
        //mpmusicafondo.stop();
        Intent i = new Intent(this, CampoJuego.class);
        startActivity(i);
    }

    public void  salirAplicacion(View view){
        mpmusicafondo.stop();
        finish();
    }
}
