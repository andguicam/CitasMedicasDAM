package com.dam.citasmedicas;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccesoREST extends AppCompatActivity {
    private static final String direccion = "http://localhost:8080/";
    private static List<Usuario> lista = null;
    private static Context context;
    public AccesoREST(Context context){
        this.context=context;
    }

    public static List<Usuario> obtenerUsuarios() {

        List<Usuario> listausers = new ArrayList<Usuario>();
        final String URL = direccion + "obtenerUsuarios";
        final ProgressDialog dlg = ProgressDialog.show(context,
                "Obteniendo los datos",
                "Por favor, espere...", true);
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {// display response
                        try{
                            dlg.dismiss();
/*                            setContentView(R.layout.activity_admin);
                            ListView lv = (ListView) findViewById(R.id.lista_admin);*/
                            String[] listaNombres = new String[response.length()];
                            List<String> al = new ArrayList<String>();


                            for (int i = 0; i<response.length();i++) {

                                JSONObject ob = response.getJSONObject(i);
                                listaNombres[i]=ob.getString("nombre");
                                Date fechaNac = new SimpleDateFormat("yyyy-MM-DD").parse(ob.getString("fechaDeNacimiento"));

                                Usuario user = new Usuario(ob.getString("nombre"),ob.getString("apellidos"),
                                        ob.getString("dni"),ob.getString("password"),fechaNac,ob.getString("direccion"),
                                        ob.getString("tipo_usuario"));
                                listausers.add(user);
                            }
/*                            for (String v : listaNombres) {
                                al.add(v);

                            }*/
/*                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( , android.R.layout.simple_list_item_1, al);
                            lv.setAdapter(arrayAdapter);*/
                            /*                            arrayAdapter.notifyDataSetChanged();*/

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
                    }
                }
        );

        //ColaServiciosWeb.getInstance().getRequestQueue().add(getRequest);
        //Singleton.getInstance(context).addToRequestQueue(getRequest);
        //SystemClock.sleep(2000);
        return listausers;
    }




    /*public static List<Usuario> obtenerUsuarios(){
        final String URL=direccion+"obtenerUsuarios";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    int codigo = response.getInt("cod");
                    if (codigo == 200) {
                        //Se ha recibido una reppuesta
                        //TODO: mirar que se devuelve, y rellenar creando objetos usuario
                        JSONObject main = response.getJSONObject("main");
                        String temp=main.getString("temp");
                        AccesoREST.lista=null;
                        te.setText(temp + " ºC");
                        pr.setText(response.getJSONObject("main").getString("pressure")
                                +" bar");
                        Date date = new Date();
                        date.setTime((long)response.getJSONObject("sys").getInt("sunrise")*1000);
                        am.setText(date.toString());


                        vt.setText(response.getJSONObject("wind").getString("speed")+
                                " km/h, orientación: "+
                                response.getJSONObject("wind").getString("deg")+"º");
                        //Ocaso
                        Date date_ocaso = new Date();
                        date_ocaso.setTime(response.getJSONObject("sys").getInt("sunset")*1000);
                        ocaso.setText(date_ocaso.toString());
                        //Hora
                        Date date_hora = new Date();
                        date_hora.setTime(response.getInt("dt"));
                        hora.setText(date_hora.toString());
                        //Visibilidad
                        visibilidad.setText(response.getInt("visibility"));
                        //Humedad
                        humedad.setText(response.getJSONObject("main").getInt("humidity"));
                    } else {
                        System.out.println("Error");
                    } } catch (JSONException e){
                    e.printStackTrace();
                }
                VolleyLog.v("Response:%n %s", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e("Error: ", error.getMessage());

            }

        });
            ColaServiciosWeb.getInstance().getRequestQueue().add(request);
            return lista;
                } */
}
