package com.dam.citasmedicas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private ListView lv;
    public List<Usuario> lista=null;
    //private ArrayAdapter arrayAdapter;
   // AccesoREST accesoREST;
    public static final String direccion = "http://192.168.0.196:8080/";


    public AdminActivity(){
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        obtenerUsuarios();


    }

    public void onAgregar(View view) {
        Intent agregarIntent = new Intent(this, AgregarUsuario.class);
        startActivity(agregarIntent);
    }


    public void obtenerUsuarios() {

        List<Usuario> listausers = new ArrayList<Usuario>();
        final String URL = direccion + "obtenerUsuarios";
        final ProgressDialog dlg = ProgressDialog.show(this,
                "Obteniendo los datos",
                "Por favor, espere...", true);

        ArrayAdapter<String> arrayAdapter;
        List<String> al = new ArrayList<String>();
        setContentView(R.layout.activity_admin);
        ListView lv = (ListView) findViewById(R.id.lista_admin);
        Context contexto = this;

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {// display response
                        try{
                            dlg.dismiss();

                            for (int i = 0; i<response.length();i++) {
                                JSONObject ob = response.getJSONObject(i);
                                al.add(ob.getString("nombre") + " "+ob.getString("apellidos")+", " + ob.getString("tipo_usuario")+", "+ob.getString("dni"));
                            }
                            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(contexto, android.R.layout.simple_list_item_1, al);
                            lv.setAdapter(arrayAdapter);

                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedItem = (String) parent.getItemAtPosition(position);
                                    Intent intent = new Intent(contexto,AdminDetalle.class);
                                    intent.putExtra("item",selectedItem);
                                    startActivity(intent);
                                }
                            });

                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(AdminActivity.this,"Error al intentar establecer contacto con el servicio",Toast.LENGTH_SHORT).show();

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