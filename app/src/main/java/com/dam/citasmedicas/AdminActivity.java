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
        //accesoREST=new AccesoREST();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        obtenerUsuarios();

        //ARRAY SACADO DE BBDD, usando AccesoREST.java. Se ponen datos en fila y subfila.
        //Fila: Nombre usuario
        //Subfila: DNI, tipo usuario
        //List<Usuario> lista = obtenerUsuarios();

        //Onclick: Abre la vista detallada (por hacer)



//        lv.setOnItemClickListener(
//                new OnItemClickListener()
//                {
//                    @Override
//                    public void onItemClick(AdapterView<?> arg0, View view,
//                                            int position, long id) {
//
//                        //Take action here.
//                    }
//                }
//        );



//        lv.setOnClickListener(new AdapterView.onClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
//                Intent appInfo = new Intent(YourActivity.this, ApkInfoActivity.class);
//                startActivity(appInfo);
//            }
//        });

    }
/*    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) l.getItemAtPosition(position);

        Intent intent = new Intent(this,AdminDetalle.class);
        intent.putExtra("item",item);
        startActivity(intent);
        //con la posicion podemos identificar el usuario en el array.
        //simplemente sacamos la clase usuario de ahi y la pasamos a la nueva actividad


    }*/

//    @Override
    //al hacer tap en algun elemento de la vista, setea el texto del elemento en el nuevo fragment
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        String user = (String) l.getItemAtPosition(position);
        //Le pasa a la nueva actividad el intent
        //Crea nueva actividad
//        Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
        //con la posicion podemos identificar el usuario en el array.
        //simplemente sacamos la clase usuario de ahi y la pasamos a la nueva actividad

        //para paso de parametros??
//        intent.putExtra("value", item);
//        startActivity(intent);
        //Desde la nueva actividad se hacen las operaciones necesarias.
//    }


    public void onAgregar(View view) {
        Intent agregarIntent = new Intent(this, AgregarUsuario.class);
        startActivity(agregarIntent);
    }

    /*public void onEliminar(View view) {
        Intent eliminarIntent = new Intent(this, EliminarUsuario.class);
        startActivity(eliminarIntent);
    }

    public void onModificar(View view) {
        Intent modificarIntent = new Intent(this, ModificarUsuario.class);
        startActivity(modificarIntent);
    }*/


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
        //Singleton.getInstance(this).addToRequestQueue(getRequest);
    }
}