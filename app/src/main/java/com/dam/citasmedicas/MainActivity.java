package com.dam.citasmedicas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String dni;
    private String contrasenia;
    private String rol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onAcceder(View view){
        EditText dni = findViewById(R.id.inputDni);
        EditText contrasenia = findViewById(R.id.inputContrasenia);

        this.dni = dni.getText().toString();
        this.contrasenia = contrasenia.getText().toString();

        TextView txt = findViewById(R.id.textBienvenida);

        if (this.rol.equals("Administrador")){
            Intent adminIntent = new Intent(this, AdminActivity.class);
            startActivity( adminIntent);
        }
        else if (this.rol.equals("paciente")){
            Intent pacienteIntent = new Intent(this, PacienteActivity.class);
            startActivity( pacienteIntent);
        }
        else if (this.rol.equals("medico")){
            Intent medicoIntent = new Intent(this, MedicoActivity.class);
            //Para que el medico solo vea las suyas descomentar lo de debajo
            medicoIntent.putExtra("dni",dni.getText().toString());
            startActivity( medicoIntent);
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioPaciente:
                if (checked)
                    this.rol = "paciente";
                    break;
            case R.id.radioMedico:
                if (checked)
                    this.rol = "medico";
                    break;
            case R.id.radioAdmin:
                if (checked)
                    this.rol = "Administrador";
                    break;
        }
    }
}