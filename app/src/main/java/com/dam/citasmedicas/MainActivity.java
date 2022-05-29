package com.dam.citasmedicas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String dni;
    private String contrasenia;
    private String rol=null;

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
        if(rol==null) {
            Toast.makeText(MainActivity.this,"Seleccione un rol",Toast.LENGTH_SHORT).show();
        }else {
            final String URL = AdminActivity.direccion + "doLogin?id=" + dni.getText().toString() + "&password=" + contrasenia.getText().toString() + "&tipo=" + rol;
            final ProgressDialog dlg = ProgressDialog.show(this,
                    "Obteniendo respuesta",
                    "Por favor, espere...", true);

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {// display response
                            try {
                                dlg.dismiss();
                                String nombre = response.getString("nombre");
                                String apellidos = response.getString("apellidos");
                                String tipo = response.getString("tipo_usuario");
                                String dni = response.getString("dni");
                                if (tipo.equals("administrador")){
                                    Intent adminIntent = new Intent(MainActivity.this, AdminActivity.class);
                                    startActivity( adminIntent);
                                }
                                else if (tipo.equals("paciente")){
                                    Intent pacienteIntent = new Intent(MainActivity.this, PacienteActivity.class);
                                    pacienteIntent.putExtra("dni",dni);
                                    startActivity( pacienteIntent);
                                }
                                else if (tipo.equals("medico")){
                                    Intent medicoIntent = new Intent(MainActivity.this, MedicoActivity.class);
                                    //Para que el medico solo vea las suyas descomentar lo de debajo
                                    medicoIntent.putExtra("dni",dni);
                                    startActivity( medicoIntent);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Error al intentar establecer contacto con el servicio", Toast.LENGTH_SHORT).show();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.toString());
                            dlg.dismiss();
                            Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            ColaServiciosWeb.getInstance(this).addToRequestQueue(getRequest);
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
                    this.rol = "administrador";
                    break;
        }
    }
}