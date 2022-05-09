package com.dam.citasmedicas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void onAgregar(View view) {
        Intent agregarIntent = new Intent(this, AgregarUsuario.class);
        startActivity(agregarIntent);
    }

    public void onEliminar(View view) {
        Intent eliminarIntent = new Intent(this, EliminarUsuario.class);
        startActivity(eliminarIntent);
    }

    public void onModificar(View view) {
        Intent modificarIntent = new Intent(this, ModificarUsuario.class);
        startActivity(modificarIntent);
    }
}