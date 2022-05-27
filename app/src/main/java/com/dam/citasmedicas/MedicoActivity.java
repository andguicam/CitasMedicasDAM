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
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MedicoActivity extends AppCompatActivity {
    //Esta variable direccion se usa para todas las operaciones del administrador y del medico
    public static final String direccion = "https://sdyswusuarios.duckdns.org/";
    public static final String direccion_citas = "https://sdyswcitas.duckdns.org/";
    private String dni;

    public MedicoActivity(){
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dni = getIntent().getExtras().get("dni").toString();
        obtenerCitas(dni);


    }

    public void onAgregar(View view) {
        Intent agregarIntent = new Intent(this, MedicoAgregarCita.class);
        agregarIntent.putExtra("dniMedico",dni);
        startActivity(agregarIntent);
    }


    public void obtenerCitas(String dni) {

        final String URL = direccion_citas + "obtenerListaCitasMedico?dniMedico="+dni;
        final ProgressDialog dlg = ProgressDialog.show(this,
                "Obteniendo las citas",
                "Por favor, espere...", true);

        List<String> al = new ArrayList<String>();
        setContentView(R.layout.activity_medico);
        ListView lv = (ListView) findViewById(R.id.lista_medico);
        Context contexto = this;

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {// display response
                        try{
                            dlg.dismiss();

                            for (int i = 0; i<response.length();i++) {
                                JSONObject ob = response.getJSONObject(i);
                                al.add("Inicio: "+ob.getString("fechaInicio") +" | Fin: " + ob.getString("fechaFin")+" | Consulta: "+ob.getString("consulta")+" | ID:"+ob.getString("id"));
                            }
                            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(contexto, android.R.layout.simple_list_item_1, al);
                            lv.setAdapter(arrayAdapter);

                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedItem = (String) parent.getItemAtPosition(position);

                                    Intent intent = new Intent(contexto,MedicoDetalle.class);
                                    intent.putExtra("item",selectedItem);
                                    startActivity(intent);
                                }
                            });

                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(MedicoActivity.this,"Error al intentar establecer contacto con el servicio",Toast.LENGTH_SHORT).show();

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
}