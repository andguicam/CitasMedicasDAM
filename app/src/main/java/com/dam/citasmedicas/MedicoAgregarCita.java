package com.dam.citasmedicas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class MedicoAgregarCita extends AppCompatActivity {
    private EditText inicio;
    private EditText fin;
    private EditText consulta;

    private String dni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dni = getIntent().getExtras().get("dniMedico").toString();
        setContentView(R.layout.activity_agregar_cita);

        inicio=(EditText) findViewById(R.id.activity_medico_detalle_id);
        fin=(EditText) findViewById(R.id.activity_medico_detalle_inicio);
        consulta=(EditText) findViewById(R.id.activity_medico_detalle_fin);

        inicio.setText("YYYY-MM-DD HH:mm");
        fin.setText("YYYY-MM-DD HH:mm");
        consulta.setText("Consulta");

    }

    public void onAgregarCita(View v){

        final String URL = AdminActivity.direccion + "agregarCita?&fechaInicio="+inicio.getText().toString()+"&fechaFin="+fin.getText().toString()+
                "&consulta="+consulta.getText().toString()+"&dniMedico="+dni;

        final ProgressDialog dlg = ProgressDialog.show(MedicoAgregarCita.this,
                "Agregando la cita",
                "Por favor, espere...", true);

        StringRequest getRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try{
                            dlg.dismiss();
                            Boolean bool = Boolean.parseBoolean(response.toString());
                            if (bool){
                                Toast.makeText(MedicoAgregarCita.this,"Cita añadida correctamente",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MedicoAgregarCita.this,"Error al añadir la cita",Toast.LENGTH_SHORT).show();
                            }
                            //Volvemos atras automaticamente, actualizando la nueva cita
                            Intent intent = new Intent(MedicoAgregarCita.this,MedicoActivity.class);
                            startActivity(intent);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(MedicoAgregarCita.this,"Error al añadir la cita",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(MedicoAgregarCita.this,"Error al añadir la cita",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        ColaServiciosWeb.getInstance(MedicoAgregarCita.this).addToRequestQueue(getRequest);

    }
}