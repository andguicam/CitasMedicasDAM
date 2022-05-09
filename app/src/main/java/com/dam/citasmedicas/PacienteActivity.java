package com.dam.citasmedicas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PacienteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);
    }

    public void onVerCitas(View view) {
        Intent verCitasIntent = new Intent(this, VerCitasPaciente.class);
        startActivity(verCitasIntent);
    }

    public void onReservar(View view) {
        Intent reservarIntent = new Intent(this, ReservarCita.class);
        startActivity(reservarIntent);
    }

    public void onAnular(View view) {
        Intent anularIntent = new Intent(this, AnularCita.class);
        startActivity(anularIntent);
    }
}