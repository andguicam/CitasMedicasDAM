package com.dam.citasmedicas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacienteActivity extends AppCompatActivity {


    private TextView textReservadas;
    private ListView listReservadas;

    private TextView textDisponibles;
    private ListView listDisponibles;

    private ArrayAdapter<Cita> reservadasAdapter;
    private ArrayAdapter<Cita> disponiblesAdapter;

    private String dniPaciente = "";

    private RequestQueue requestQueue;

    private String idReserva;
    private String idAnulacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);

        dniPaciente = (String) getIntent().getExtras().get("dni").toString();

        requestQueue = Volley.newRequestQueue(this);

        textReservadas = (TextView) findViewById(R.id.text_reservadas);
        listReservadas = (ListView) findViewById(R.id.lista_citas_reservadas);
        reservadasAdapter = new ArrayAdapter<Cita> (this, R.layout.paciente_citas_concedidas_items);
        listReservadas.setAdapter(reservadasAdapter);
        obtenerCitasReservadas();

        registerForContextMenu(listReservadas);

        listReservadas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cita cita = reservadasAdapter.getItem(i);
                idAnulacion = cita.getId();
                Log.d("ID Anular", idAnulacion);
                return false;
            }
        });



        textDisponibles = (TextView) findViewById(R.id.text_disponibles);
        listDisponibles = (ListView) findViewById(R.id.lista_citas_disponibles);
        disponiblesAdapter = new ArrayAdapter<Cita> (this, R.layout.paciente_citas_disponibles_items);
        listDisponibles.setAdapter(disponiblesAdapter);
        obtenerCitasDisponibles();

        registerForContextMenu(listDisponibles);
        listDisponibles.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cita cita = disponiblesAdapter.getItem(i);
                idReserva = cita.getId();
                Log.d("ID Reservar: ", idReserva);
                return false;
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.lista_citas_reservadas) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_anulacion, menu);
        }
        else if (v.getId()==R.id.lista_citas_disponibles) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_reserva, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.ReservarAction:
                ReservarCita(idReserva);
                finish();
                startActivity(getIntent());
                return true;
            case R.id.AnularAction:
                AnularCita(idAnulacion);
                finish();
                startActivity(getIntent());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void ReservarCita (String idCita){
        List<Cita> lista = null;
        String direccion = "https://sdyswcitas.duckdns.org/";
        final String URL = direccion + "reservarCita?dniPaciente="+dniPaciente+"&idCita="+idCita;

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {// display response
                        try{

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
        Singleton.getInstance(this).addToRequestQueue(getRequest);
    }

    private void AnularCita (String idCita){
        List<Cita> lista = null;
        String direccion = "https://sdyswcitas.duckdns.org/";
        final String URL = direccion + "anularCita?dniPaciente="+dniPaciente+"&idCita="+idCita;

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {// display response
                        try{

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
        Singleton.getInstance(this).addToRequestQueue(getRequest);
    }

    private void obtenerCitasReservadas() {
        List<Cita> lista = null;
        String direccion = "https://sdyswcitas.duckdns.org/";
        final String URL = direccion + "citasReservadas?dniPaciente="+dniPaciente;

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {// display response
                        ArrayList<Cita> arrayReservadas = new ArrayList<>();

                        try{
                                int length = response.length();


                            for (int i = 0; i < length; i++) {
                                    JSONObject cita = response.getJSONObject(i);
                                    String id = cita.getString("id");


                                    Date fechaInicio = stringtoDate(cita.getString("fechaInicio"));
                                    Date fechaFin = stringtoDate(cita.getString("fechaFin"));

                                    String consulta = cita.getString("consulta");
                                    JSONObject medicoResponsable = cita.getJSONObject("medicoResponsable");
                                    String nombre = medicoResponsable.getString("nombre");
                                    String apellidos = medicoResponsable.getString("apellidos");
                                    String dni = medicoResponsable.getString("dni");
                                    String email = medicoResponsable.getString("email");
                                    String password = medicoResponsable.getString("password");
                                    Date fechaDeNacimiento = stringtoDate(medicoResponsable.getString("fechaDeNacimiento"));
                                    String direccion = medicoResponsable.getString("direccion");
                                    String tipo_usuario = medicoResponsable.getString("tipo_usuario");
                                    Usuario medico = new Usuario(nombre, apellidos, dni, email, password, fechaDeNacimiento, direccion, tipo_usuario);
                                    Cita cita_i = new Cita(id, fechaInicio, fechaFin, consulta, medico);

                                    arrayReservadas.add(cita_i);
                                    Log.d("onResponse: ", String.valueOf(cita_i));
                                }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        reservadasAdapter.clear();
                        reservadasAdapter.addAll(arrayReservadas);
                        reservadasAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        Singleton.getInstance(this).addToRequestQueue(getRequest);
    }


    private void obtenerCitasDisponibles() {
        List<Cita> lista = null;
        String direccion = "https://sdyswcitas.duckdns.org/";
        final String URL = direccion + "citasDisponibles";

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Cita> arrayDisponibles = new ArrayList<>();
                        try {


                            int length = response.length();

                            for (int i = 0; i < length; i++) {
                                JSONObject cita = response.getJSONObject(i);
                                String id = cita.getString("id");


                                Date fechaInicio = stringtoDate(cita.getString("fechaInicio"));
                                Date fechaFin = stringtoDate(cita.getString("fechaFin"));

                                String consulta = cita.getString("consulta");
                                JSONObject medicoResponsable = cita.getJSONObject("medicoResponsable");
                                String nombre = medicoResponsable.getString("nombre");
                                String apellidos = medicoResponsable.getString("apellidos");
                                String dni = medicoResponsable.getString("dni");
                                String email = medicoResponsable.getString("email");
                                String password = medicoResponsable.getString("password");
                                Date fechaDeNacimiento = stringtoDate(medicoResponsable.getString("fechaDeNacimiento"));
                                String direccion = medicoResponsable.getString("direccion");
                                String tipo_usuario = medicoResponsable.getString("tipo_usuario");
                                Usuario medico = new Usuario(nombre, apellidos, dni, email, password, fechaDeNacimiento, direccion, tipo_usuario);
                                Cita cita_i = new Cita(id, fechaInicio, fechaFin, consulta, medico);
                                arrayDisponibles.add(cita_i);
                                Log.d("onResponse: ", String.valueOf(cita_i));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        disponiblesAdapter.clear();
                        disponiblesAdapter.addAll(arrayDisponibles);
                        disponiblesAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        Singleton.getInstance(this).addToRequestQueue(getRequest);
    }

        private Date stringtoDate (String fecha){
        String fecha2 = fecha.substring(0, 10) +" "+ fecha.substring(11, 19);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fecha2);
        } catch (ParseException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return date;
    }
}