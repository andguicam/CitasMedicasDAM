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

public class AgregarUsuario extends AppCompatActivity {
    private EditText nombre;
    private EditText apellidos;
    private EditText fechaNac;
    private EditText tipo;
    private EditText direccion;
    private EditText password;
    private EditText email;
    private EditText dni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario);

        nombre=(EditText) findViewById(R.id.activity_medico_detalle_id);
        apellidos=(EditText) findViewById(R.id.activity_medico_detalle_inicio);
        fechaNac=(EditText) findViewById(R.id.activity_medico_detalle_fin);
        tipo=(EditText) findViewById(R.id.activity_agregar_tipo);
        direccion=(EditText) findViewById(R.id.activity_agregar_direccion);
        password=(EditText) findViewById(R.id.activity_agregar_password);
        email=(EditText) findViewById(R.id.activity_agregar_email);
        dni=(EditText) findViewById(R.id.activity_medico_detalle_consulta);

        nombre.setText("Nombre");
        apellidos.setText("Apellidos");
        fechaNac.setText("YYYY-MM-DD");
        tipo.setText("Tipo");
        direccion.setText("Direccion");
        password.setText("Contraseña");
        email.setText("Email");
        dni.setText("DNI");


    }

    public void onAgregarUsuario(View v){
        final String URL = AdminActivity.direccion + "agregarUsuario?&dni="+dni.getText().toString()+"&nombre="+nombre.getText().toString()+
                "&apellidos="+apellidos.getText().toString()+"&password="+password.getText().toString()+"&fecha="+fechaNac.getText().toString()+"&direccion="+direccion.getText().toString()+
                "&tipo="+tipo.getText().toString()+"&email="+email.getText().toString();

        final ProgressDialog dlg = ProgressDialog.show(AgregarUsuario.this,
                "Agregando el usuario",
                "Por favor, espere...", true);

        StringRequest getRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try{
                            dlg.dismiss();
                            Boolean bool = Boolean.parseBoolean(response.toString());
                            if (bool){
                                Toast.makeText(AgregarUsuario.this,"Usuario añadido correctamente",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(AgregarUsuario.this,"Error al añadir el usuario",Toast.LENGTH_SHORT).show();
                            }
                            //Volvemos atras automaticamente, actualizando el nuevo usuario
                            Intent intent = new Intent(AgregarUsuario.this,AdminActivity.class);
                            startActivity(intent);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(AgregarUsuario.this,"Error al añadir el usuario",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(AgregarUsuario.this,"Error al añadir el usuario",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        ColaServiciosWeb.getInstance(AgregarUsuario.this).addToRequestQueue(getRequest);


    }
}