package com.example.miscitas.Presentador;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.miscitas.Vistas.VistaCitas;
import com.example.miscitas.Vistas.VistaInicio;
import com.example.miscitas.Vistas.VistaLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PresenterLogin {

    private Context mContext;
    private DatabaseReference databaseReference;
   // private FirebaseAuth firebaseAuth;

    public PresenterLogin(Context mContext, DatabaseReference databaseReference) {
        this.mContext = mContext;
        this.databaseReference = databaseReference;
    }


    public  String datos(String dni){

        return  "d";
    }

    public void saveFirebase(String nombres,String apellidos,String dni){
        if (TextUtils.isEmpty(nombres)){
            Toast.makeText(mContext, "falta nombres", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(apellidos)){
            Toast.makeText(mContext, "falta apellidos", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(dni)){
            Toast.makeText(mContext, "falta Dni", Toast.LENGTH_SHORT).show();
        }
        else {
            ProgressDialog progressDialog= new ProgressDialog(mContext);
            progressDialog.setMessage("Cargando..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            String key =databaseReference.push().getKey();

            Map<String,Object> usuario= new HashMap<>();
            usuario.put("nombres",nombres);
            usuario.put("apellidos",apellidos);
            usuario.put("urlfoto","default_image");
            usuario.put("dni",dni);
            usuario.put("token","");

            databaseReference.child("Usuarios").child(dni).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()){
                        //    alert.dismiss();
                        progressDialog.dismiss();
                        SharedPreferences preferences=mContext.getSharedPreferences("datosuser", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("dni",dni);
                        editor.putString("nombres",nombres);
                        editor.putString("apellidos",apellidos);
                        editor.commit();
                        String Sucripcion="FamiliaSihuayro";
                        FirebaseMessaging.getInstance().subscribeToTopic(Sucripcion).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        Intent intent = new Intent(mContext, VistaInicio.class);
                        mContext.startActivity(intent);
                        Toast.makeText(mContext, "Registrado", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(mContext, "err "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            });
        }

    }

    private  void saveUserFirebase(String nombres,String apellidos,String dni){
        ProgressDialog progressDialog= new ProgressDialog(mContext);
        progressDialog.setMessage("Cargando..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String key =databaseReference.push().getKey();

        Map<String,Object> usuario= new HashMap<>();
        usuario.put("nombres",nombres);
        usuario.put("apellidos",apellidos);
        usuario.put("urlfoto","default_image");
        usuario.put("dni",dni);
        usuario.put("token","");
        usuario.put("key",key);

        databaseReference.child("Usuarios").child(key).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()){
                    //    alert.dismiss();
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "Registrado", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(mContext, "err "+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

}
