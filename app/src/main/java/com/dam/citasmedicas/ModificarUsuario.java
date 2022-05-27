package com.dam.citasmedicas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

//TODO: representar fechas bien
public class ModificarUsuario extends AppCompatActivity {
    private String nombre_s;
    private String apellidos_s;
    private String fechaNac_s;
    private String tipo_s;
    private String direccion_s;
    private String password_s;
    private String email_s;
    private String dni_s;

    private TextView nombre;
    private TextView apellidos;
    private TextView fechaNac;
    private TextView tipo;
    private TextView direccion;
    private TextView password;
    private TextView email;
    private TextView dni;

    private EditText nombre_nuevo;
    private EditText apellidos_nuevo;
    private EditText fechaNac_nuevo;
    private EditText tipo_nuevo;
    private EditText direccion_nuevo;
    private EditText password_nuevo;
    private EditText email_nuevo;
    private EditText dni_nuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_usuario);

        nombre_s = (String) getIntent().getExtras().get("nombre").toString();
        apellidos_s = (String) getIntent().getExtras().get("apellidos").toString();
        fechaNac_s = (String) getIntent().getExtras().get("fechaNac").toString();
        password_s = (String) getIntent().getExtras().get("password").toString();
        dni_s = (String) getIntent().getExtras().get("dni").toString();
        direccion_s = (String) getIntent().getExtras().get("direccion").toString();
        tipo_s = (String) getIntent().getExtras().get("tipo").toString();
        email_s = (String) getIntent().getExtras().get("email").toString();

        nombre=(TextView)findViewById(R.id.activity_medico_detalle_id);
        apellidos=(TextView) findViewById(R.id.activity_medico_detalle_inicio);
        fechaNac=(TextView) findViewById(R.id.activity_medico_detalle_fin);
        password=(TextView) findViewById(R.id.activity_agregar_password);
        dni=(TextView) findViewById(R.id.activity_medico_detalle_consulta);
        direccion=(TextView) findViewById(R.id.activity_agregar_direccion);
        tipo=(TextView) findViewById(R.id.activity_agregar_tipo);
        email=(TextView) findViewById(R.id.activity_agregar_email);

        nombre_nuevo=(EditText)findViewById(R.id.activity__modificar_nuevo_nombre);
        apellidos_nuevo=(EditText) findViewById(R.id.activity__modificar_nuevo_apellidos);
        fechaNac_nuevo=(EditText) findViewById(R.id.activity__modificar_nuevo_fechaNac);
        password_nuevo=(EditText) findViewById(R.id.activity__modificar_nuevo_password);
        dni_nuevo=(EditText) findViewById(R.id.activity__modificar_nuevo_dni);
        direccion_nuevo=(EditText) findViewById(R.id.activity__modificar_nuevo_direccion);
        tipo_nuevo=(EditText) findViewById(R.id.activity__modificar_nuevo_tipo);
        email_nuevo=(EditText) findViewById(R.id.activity__modificar_nuevo_email);

        nombre.setText(nombre_s);
        apellidos.setText(apellidos_s);
        fechaNac.setText(fechaNac_s);
        password.setText(password_s);
        dni.setText(dni_s);
        direccion.setText(direccion_s);
        tipo.setText(tipo_s);
        email.setText(email_s);

        nombre_nuevo.setText(nombre_s,TextView.BufferType.EDITABLE);
        apellidos_nuevo.setText(apellidos_s,TextView.BufferType.EDITABLE);
        fechaNac_nuevo.setText(fechaNac_s,TextView.BufferType.EDITABLE);
        password_nuevo.setText(password_s,TextView.BufferType.EDITABLE);
        dni_nuevo.setText(dni_s,TextView.BufferType.EDITABLE);
        direccion_nuevo.setText(direccion_s,TextView.BufferType.EDITABLE);
        tipo_nuevo.setText(tipo_s,TextView.BufferType.EDITABLE);
        email_nuevo.setText(email_s,TextView.BufferType.EDITABLE);

    }



    public void onConfirmar(View view) {
        //TODO: probar este metodo por completo cuando tenga la base de datos con email actualizada
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ModificarUsuario.this);
        alertBuilder.setTitle("¿Modificar usuario?");
        alertBuilder.setMessage("¿Está seguro de que desea modificar el usuario? Los datos anteriores se perderán");
        alertBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO: comprobar que funciona cuando tenga el email
                final String URL = AdminActivity.direccion + "modificarUsuario?tipo_ant="+tipo_s+"&dni_ant="+dni_s+"&dni_nuevo="+dni_nuevo.getText().toString()+"&nombre="+nombre_nuevo.getText().toString()+
                        "&apellidos="+apellidos_nuevo.getText().toString()+"&password="+password_nuevo.getText().toString()+"&fecha="+fechaNac_nuevo.getText().toString()+"&direccion="+direccion_nuevo.getText().toString()+
                        "&tipo="+tipo_nuevo.getText().toString()+"&email="+email_nuevo.getText().toString();

                final ProgressDialog dlg = ProgressDialog.show(ModificarUsuario.this,
                        "Modificando el usuario",
                        "Por favor, espere...", true);

                // prepare the Request
                StringRequest getRequest = new StringRequest(Request.Method.GET, URL,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try{
                                    dlg.dismiss();
                                    //TODO: da fallo en la fecha, no lo pilla bien
                                    Boolean bool = Boolean.parseBoolean(response.toString());
                                    if (bool){
                                        Toast.makeText(ModificarUsuario.this,"Usuario modificado correctamente",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(ModificarUsuario.this,"Error al modificar el usuario",Toast.LENGTH_SHORT).show();
                                    }
                                    //Volvemos atras automaticamente, actualizando el nuevo usuario
                                    Intent intent = new Intent(ModificarUsuario.this,AdminDetalle.class);
                                    startActivity(intent);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(ModificarUsuario.this,"Error al modificar el usuario",Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", error.toString());
                                Toast.makeText(ModificarUsuario.this,"Error al modificar el usuario",Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                ColaServiciosWeb.getInstance(ModificarUsuario.this).addToRequestQueue(getRequest);


            }
        });

        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ModificarUsuario.this,"Modificación cancelada",Toast.LENGTH_SHORT).show();
            }
        });

        alertBuilder.show();

    }
}