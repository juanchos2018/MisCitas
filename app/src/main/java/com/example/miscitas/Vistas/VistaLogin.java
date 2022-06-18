package com.example.miscitas.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miscitas.Presentador.PresenterIncio;
import com.example.miscitas.Presentador.PresenterLogin;
import com.example.miscitas.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class VistaLogin extends AppCompatActivity implements View.OnClickListener  {


    EditText etdni,etnombres,etapellidos;
    Button btnconsultar,btnregistrar;
    PresenterLogin presenterLogin;
    private DatabaseReference reference;
    String nombreusuario,apellidousuario,dniusuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_login);

        etdni=(EditText)findViewById(R.id.etDni);
        etnombres=(EditText)findViewById(R.id.etNombres);
        etapellidos=(EditText)findViewById(R.id.etApellidos);
        btnconsultar=(Button)findViewById(R.id.btnconsultar);
        btnregistrar=(Button)findViewById(R.id.btningresar);
        btnconsultar.setOnClickListener(this);
        btnregistrar.setOnClickListener(this);
        reference= FirebaseDatabase.getInstance().getReference();
        presenterLogin =new PresenterLogin(this,reference);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnconsultar:
                String dni=etdni.getText().toString();
                ConsultarDatos(dni);
                break;
            case R.id.btningresar:
                Ingresar();
                break;
        }
    }

    private void Ingresar() {
        String dni =etdni.getText().toString();
        String name =etnombres.getText().toString();
        String apes =etapellidos.getText().toString();

        presenterLogin.saveFirebase(name,apes,dni);
        finish();
    }

    private  void ConsultarDatos(String dni){
        if (TextUtils.isEmpty(dni)) {
            Toast.makeText(this, "poner dni", Toast.LENGTH_SHORT).show();
        }
        else if (dni.length()!=8){
            Toast.makeText(this, "dni de 8 digitos", Toast.LENGTH_SHORT).show();

        }else{
            ProgressDialog progressDialog= new ProgressDialog(VistaLogin.this);
            progressDialog.setMessage("Buscando ..");
            progressDialog.show();
            final String URL2="https://quertium.com/api/v1/reniec/dni/"+dni+"?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.MTM3Mw.x-jUgUBcJukD5qZgqvBGbQVMxJFUAIDroZEm4Y9uTyg";
            RequestQueue requestQueue2= Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest1 =new StringRequest(Request.Method.GET,URL2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String responses) {
                            try {
                                JSONObject jsonObject2=new JSONObject(responses);
                                //  JSONObject nombre=jsonObject.getJSONObject("nombres");
                                String name=jsonObject2.getString("primerNombre");
                                String name2=jsonObject2.getString("segundoNombre");
                                String apellido_paterno=jsonObject2.getString("apellidoPaterno");
                                String apellido_materno=jsonObject2.getString("apellidoMaterno");
                                etnombres.setText(name +" "+name2);
                                etapellidos.setText(apellido_paterno +" " +apellido_materno);
                                progressDialog.dismiss();

                                InputMethodManager imm = (InputMethodManager) getSystemService(VistaLogin.this.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(etnombres.getWindowToken(), 0);
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(VistaLogin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest1.setRetryPolicy(policy);
            requestQueue2.add(stringRequest1);
        }

    }
}