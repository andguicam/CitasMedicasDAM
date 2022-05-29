package com.dam.citasmedicas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

public class MedicoDetalle extends AppCompatActivity {
    private TextView inicio;
    private TextView fin;
    private TextView responsable;
    private TextView consulta;
    private String id_cita;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_detalle);
        inicio = (TextView) findViewById(R.id.activity_medico_detalle_inicio);
        fin = (TextView) findViewById(R.id.activity_medico_detalle_fin);
        responsable = (TextView) findViewById(R.id.activity_medico_detalle_responsable);
        consulta = (TextView) findViewById(R.id.activity_medico_detalle_consulta);

        id_cita = (String) getIntent().getExtras().get("id");

        final String URL = AdminActivity.direccion_citas + "obtenerCita?idCita=" + id_cita;

        final ProgressDialog dlg = ProgressDialog.show(this,
                "Obteniendo los datos de la cita",
                "Por favor, espere...", true);

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            dlg.dismiss();

                            inicio.setText(response.getString("fechaInicio").substring(0, 10) +" "+
                                    response.getString("fechaInicio").substring(11, 16));
                            fin.setText(response.getString("fechaFin").substring(0, 10) +" "+
                                    response.getString("fechaFin").substring(11, 16));
                            responsable.setText(response.getJSONObject("medicoResponsable").getString("nombre") + " " + response.getJSONObject("medicoResponsable").getString("apellidos"));
                            consulta.setText(response.getString("consulta"));

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        ColaServiciosWeb.getInstance(this).addToRequestQueue(getRequest);
    }

    public void onEliminarCita(View v){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MedicoDetalle.this);
        alertBuilder.setTitle("¿Eliminar cita?");
        alertBuilder.setMessage("¿Está seguro de que desea eliminar la cita? Esta acción es irreversible");
        alertBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final String URL = AdminActivity.direccion + "eliminarCita?idCita="+id_cita;

                final ProgressDialog dlg = ProgressDialog.show(MedicoDetalle.this,
                        "Eliminando la cita",
                        "Por favor, espere...", true);

                StringRequest getRequest = new StringRequest(Request.Method.GET, URL,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try{
                                    dlg.dismiss();
                                    Boolean bool = Boolean.parseBoolean(response.toString());
                                    if (bool){
                                        Toast.makeText(MedicoDetalle.this,"Cita eliminada correctamente",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(MedicoDetalle.this,"Error al eliminar la cita",Toast.LENGTH_SHORT).show();
                                    }
                                    Intent intent = new Intent(MedicoDetalle.this,MedicoActivity.class);
                                    startActivity(intent);
                                }
                                catch (Exception e){
                                    e.printStackTrace();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", error.toString());
                                Toast.makeText(MedicoDetalle.this,"Error al eliminar la cita",Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                ColaServiciosWeb.getInstance(MedicoDetalle.this).addToRequestQueue(getRequest);


            }
        });

        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MedicoDetalle.this,"Cancelado",Toast.LENGTH_SHORT).show();
            }
        });

        alertBuilder.show();

    }
}