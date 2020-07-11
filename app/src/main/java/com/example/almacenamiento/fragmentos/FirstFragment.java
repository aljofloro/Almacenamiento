package com.example.almacenamiento.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.almacenamiento.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FirstFragment extends Fragment {

    private final static int FICHERO_INTERNO = 100;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first
                , container, false);
    }

    @Override
    public void onResume(){
        super.onResume();
        ((Button)getActivity().findViewById(R.id.btn_save))
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nombreFichero = ((EditText)getActivity()
                        .findViewById(R.id.edt_namefile)).getText().toString();
                final String contenido = ((EditText)getActivity()
                        .findViewById(R.id.edt_texto)).getText().toString();

                if(nombreFichero.isEmpty()){
                    Snackbar.make(getView(),"Seleccione un fichero"
                    ,Snackbar.LENGTH_SHORT).show();
                }else{
                    if(contenido.isEmpty()){
                        Snackbar.make(getView(),"Ingrese Contenido"
                        ,Snackbar.LENGTH_SHORT).show();
                    }else{
                        FileOutputStream stream;
                        try{
                            stream = getContext().openFileOutput(nombreFichero
                                    , Context.MODE_PRIVATE);
                            stream.write(contenido.getBytes());
                            stream.close();
                            Snackbar.make(getView(),"Se guardó con éxito"
                            ,Snackbar.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        ((Button)getActivity().findViewById(R.id.btn_load))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String nombreFichero = ((EditText)getActivity()
                                .findViewById(R.id.edt_namefile)).getText().toString();

                        EditText contenido = (EditText)getActivity()
                                .findViewById(R.id.edt_texto);

                        if(nombreFichero.isEmpty()){
                            Snackbar.make(getView(),"Seleccione un Fichero"
                            ,Snackbar.LENGTH_SHORT).show();
                        }else{
                            try{
                                FileInputStream stream = getContext()
                                        .openFileInput(nombreFichero);
                                int contador = 0;
                                String lectura = "";

                                while((contador = stream.read()) != -1){
                                    lectura = lectura + Character.toString((char) contador);
                                }
                                contenido.setText(lectura);
                                Snackbar.make(getView(),"Fichero Cargado"
                                ,Snackbar.LENGTH_SHORT).show();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }


}