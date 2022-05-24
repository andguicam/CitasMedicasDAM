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

//TODO: representar fechas bien
public class AdminDetalle extends AppCompatActivity {
    private String[] datos;
    private TextView nombre;
    private TextView apellidos;
    private TextView fechaNac;
    private TextView tipo;
    private TextView direccion;
    private TextView password;
    private TextView email;
    private TextView dni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detalle);
        nombre=(TextView)findViewById(R.id.activity_medico_detalle_id);
        apellidos=(TextView) findViewById(R.id.activity_medico_detalle_inicio);
        fechaNac=(TextView) findViewById(R.id.activity_medico_detalle_fin);
        password=(TextView) findViewById(R.id.activity_agregar_password);
        dni=(TextView) findViewById(R.id.activity_medico_detalle_consulta);
        direccion=(TextView) findViewById(R.id.activity_agregar_direccion);
        tipo=(TextView) findViewById(R.id.activity_agregar_tipo);
        email=(TextView) findViewById(R.id.activity_agregar_email);

        String user = (String) getIntent().getExtras().get("item");

        //0: Nombre y apellidos
        //1: tipo de usuario
        //2: DNI
        datos = user.split("[,]",0);

        obtenerUsuario(datos);
    }

    public void obtenerUsuario(String[] datos){

        final String URL = AdminActivity.direccion + "getUsuario?tipo="+datos[1].trim()+"&dni="+datos[2].trim();

        final ProgressDialog dlg = ProgressDialog.show(this,
                "Obteniendo los datos del usuario",
                "Por favor, espere...", true);

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            dlg.dismiss();

                            nombre.setText(response.getString("nombre"));
                            apellidos.setText(response.getString("apellidos"));
                            fechaNac.setText(response.getString("fechaDeNacimiento"));
                            password.setText(response.getString("password"));
                            dni.setText(response.getString("dni"));
                            direccion.setText(response.getString("direccion"));
                            tipo.setText(response.getString("tipo_usuario"));
                            //TODO: email
                            email.setText("emailDeEjemplo@email.es");

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
        ColaServiciosWeb.getInstance(this).addToRequestQueue(getRequest);
    }
    public void onAgregar(View view) {
        Intent agregarIntent = new Intent(this, AgregarUsuario.class);
        startActivity(agregarIntent);
    }

    public void onEliminar(View view) {
    //TODO: probar este metodo por completo cuando tenga la base de datos con email actualizada
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminDetalle.this);
        alertBuilder.setTitle("¿Eliminar usuario?");
        alertBuilder.setMessage("¿Está seguro de que desea eliminar el usuario? Esta acción es irreversible");
        alertBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final String URL = AdminActivity.direccion + "eliminarUsuario?tipo="+datos[1].trim()+"&dni="+datos[2].trim();

                final ProgressDialog dlg = ProgressDialog.show(AdminDetalle.this,
                        "Eliminando el usuario",
                        "Por favor, espere...", true);

                // prepare the Request
                StringRequest getRequest = new StringRequest(Request.Method.GET, URL,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try{
                                    dlg.dismiss();
                                    //TODO: probar si esta linea funciona
                                    Boolean bool = Boolean.parseBoolean(response.substring(0,10));
                                    if (bool){
                                        Toast.makeText(AdminDetalle.this,"Usuario eliminado correctamente",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(AdminDetalle.this,"Error al eliminar el usuario",Toast.LENGTH_SHORT).show();
                                    }

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
                                Toast.makeText(AdminDetalle.this,"Error al eliminar el usuario",Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                ColaServiciosWeb.getInstance(AdminDetalle.this).addToRequestQueue(getRequest);


            }
        });

        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AdminDetalle.this,"Cancelado",Toast.LENGTH_SHORT).show();
            }
        });

        alertBuilder.show();

    }

    public void onModificar(View view) {
        Intent modificarIntent = new Intent(this, ModificarUsuario.class);
        modificarIntent.putExtra("nombre",nombre.getText().toString());
        modificarIntent.putExtra("apellidos",apellidos.getText().toString());
        modificarIntent.putExtra("dni",dni.getText().toString());
        modificarIntent.putExtra("direccion",direccion.getText().toString());
        modificarIntent.putExtra("tipo",tipo.getText().toString());
        modificarIntent.putExtra("password",password.getText().toString());
        modificarIntent.putExtra("email",email.getText().toString());
        modificarIntent.putExtra("fechaNac",fechaNac.getText().toString());

        startActivity(modificarIntent);
    }
}