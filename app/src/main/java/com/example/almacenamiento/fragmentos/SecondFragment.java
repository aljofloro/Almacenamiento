package com.example.almacenamiento.fragmentos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.almacenamiento.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class SecondFragment extends Fragment {
    final File ruta = Environment.getExternalStorageDirectory();
    final File fichero = new File(ruta.getAbsolutePath(),"externo.txt");

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verificarPermisosAlmacenamiento(Activity activity){
        int permisos = ActivityCompat.checkSelfPermission(activity
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permisos != PackageManager.PERMISSION_GRANTED){
            ActivityCompat
                    .requestPermissions(activity
                            ,PERMISSIONS_STORAGE
                            ,REQUEST_EXTERNAL_STORAGE);
        }
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();
        actualizarEtiqueta();
        ((Button)getActivity().findViewById(R.id.btn_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarArchivo(((EditText)getActivity()
                        .findViewById(R.id.edt_editar))
                        .getText().toString(),getContext());
                ((EditText)getActivity().findViewById(R.id.edt_editar))
                        .setText("");
                actualizarEtiqueta();
            }
        });

    }

    public void guardarArchivo(String datos, Context context){
        String estadoSD = Environment.getExternalStorageState();
        if(!estadoSD.equals(Environment.MEDIA_MOUNTED)){
            Snackbar.make(getView(),"No es posible escribir en la " +
                    "memoria externa", Snackbar.LENGTH_SHORT).show();
            return;
        }
        try{
            verificarPermisosAlmacenamiento(getActivity());
            FileOutputStream stream = new FileOutputStream(fichero,true);
            String texto = datos + "\n";
            stream.write(texto.getBytes());
            stream.close();
            Snackbar.make(getView(),"Se guardó exitosamente"
                    ,Snackbar.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Vector<String> obtenerDatos(Context context){
        Vector<String> resultado = new Vector<>();
        String estadoSD = Environment.getExternalStorageState();
        if(!estadoSD.equals(Environment.MEDIA_MOUNTED)
        && !estadoSD.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            Snackbar.make(getView(),"No se puede leer" +
                    "el almacenamiento externo",Snackbar.LENGTH_SHORT).show();
            return resultado;
        }
        try{
            verificarPermisosAlmacenamiento(getActivity());
            FileInputStream stream = new FileInputStream(fichero);
            BufferedReader entrada
                    = new BufferedReader(new InputStreamReader(stream));
            String linea;
            do{
                linea = entrada.readLine();
                if(linea != null){
                    resultado.add(linea);
                }
            }while(linea != null);
            stream.close();
            Snackbar.make(getView(),"Fichero cargado!"
                    ,Snackbar.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultado;
    }

    public void actualizarEtiqueta(){
        ((TextView)getActivity().findViewById(R.id.txt_view))
                .setText((obtenerDatos(getContext()).toString()));
    }


}