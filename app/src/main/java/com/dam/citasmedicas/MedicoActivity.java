package com.dam.citasmedicas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MedicoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico);
    }

    public void onVerCitas(View view) {
        Intent verCitasIntent = new Intent(this, VerCitasMedico.class);
        startActivity(verCitasIntent);
    }

    public void onAgregarCita(View view) {
        Intent agregarCitaintent = new Intent(this, AgregarCita.class);
        startActivity(agregarCitaintent);
    }

    public void onEliminarCita(View view) {
        Intent eliminarCitaIntent = new Intent(this, EliminarCita.class);
        startActivity(eliminarCitaIntent);
    }
}