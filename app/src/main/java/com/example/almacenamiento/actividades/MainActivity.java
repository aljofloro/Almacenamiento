package com.example.almacenamiento.actividades;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.example.almacenamiento.R;
import com.example.almacenamiento.fragmentos.FirstFragment;
import com.example.almacenamiento.fragmentos.SecondFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import es.dmoral.coloromatic.ColorOMaticDialog;
import es.dmoral.coloromatic.IndicatorMode;
import es.dmoral.coloromatic.OnColorSelectedListener;
import es.dmoral.coloromatic.colormode.ColorMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void cambiarColorBarra(int color, Activity activity){
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams
                .FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams
                .FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(color);
        window.setNavigationBarColor(color);

    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences =
                this.getPreferences(Context.MODE_PRIVATE);

        int color = sharedPreferences.getInt(getString(R.string.sp_color_bar)
                ,0);
        if(color != 0){
            cambiarColorBarra(color,this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            cargarColores();
            return true;
        }else if(id == R.id.action_almint){
            cargarAlmacenamientoInterno();
        }else if(id == R.id.action_almext){
            cargarAlmacenamientoExterno();
        }

        return super.onOptionsItemSelected(item);
    }

    public void cargarAlmacenamientoExterno(){
        SecondFragment almExtFragment = new SecondFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,almExtFragment)
                .addToBackStack(null)
                .commit();
    }

    public void cargarAlmacenamientoInterno(){
        FirstFragment almIntFragment = new FirstFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, almIntFragment)
                .addToBackStack(null)
                .commit();
    }

    public void cargarColores(){
        new ColorOMaticDialog.Builder()
                .initialColor(Color.BLACK)
                .colorMode(ColorMode.ARGB)
                .indicatorMode(IndicatorMode.HEX)
                .onColorSelected(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        SharedPreferences sharedPreferences =
                                MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor =
                                sharedPreferences.edit();
                        editor.putInt(getString(R.string.sp_color_bar),color);
                        editor.commit();
                        cambiarColorBarra(color,MainActivity.this);
                    }
                })
                .showColorIndicator(true)
                .create()
                .show(getSupportFragmentManager(),"ColorOMaticDialog");
    }
}