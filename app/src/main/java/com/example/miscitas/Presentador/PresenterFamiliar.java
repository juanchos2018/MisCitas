package com.example.miscitas.Presentador;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.miscitas.Adapters.AdapterCitas;
import com.example.miscitas.Adapters.AdapterFamiliar;
import com.example.miscitas.Interface.InterfaceFamiliar;
import com.example.miscitas.Modelo.CitaModel;
import com.example.miscitas.Modelo.FamiliarModel;
import com.example.miscitas.Modelo.UserModel;
import com.example.miscitas.R;
import com.example.miscitas.Vistas.VistaCitas;
import com.example.miscitas.Vistas.VistaDetalleCita;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PresenterFamiliar {

    private Context mContext;
    private DatabaseReference databaseReference;
    private FirebaseAuth  firebaseAuth;
    private StorageReference storageReference;

    android.app.AlertDialog.Builder builder1;
    AlertDialog alert;
    private AdapterCitas adapterCitas;
    public final Calendar c = Calendar.getInstance();
    //Variables para obtener la fecha
    private static final String CERO = "0";
    private static final String BARRA = "-";
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    final int hora = 07;
    final int minuto = 0;
    private static final String DOS_PUNTOS = ":";
    private final int frames = 9;
    private int currentAnimationFrame = 0;
    private LottieAnimationView animationView;
    long maxin=0;
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", id);
    Date tgl_daftar_date;
    private String NombreFamiliar;
    public PresenterFamiliar(Context mContext, DatabaseReference databaseReference,StorageReference storageReference) {
        this.mContext = mContext;
        this.databaseReference = databaseReference;
        this.storageReference=storageReference;
       // this.firebaseAuth = firebaseAuth;
    }

    public  void  cargarDialogo(){
        builder1 = new AlertDialog.Builder(mContext);
        Button btcerrrar,brnregistrar;
        final EditText etnombre,etapellidos;
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialogoadd_familiar, null);
        builder1.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.btnCerrar);
        brnregistrar=(Button)v.findViewById(R.id.btnCrear);
        etnombre=(EditText)v.findViewById(R.id.etNombre);
        etapellidos=(EditText)v.findViewById(R.id.etApellido);
        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //agregarCarpeta(nonbrecarpeta);
                alert.dismiss();
            }
        });
        brnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomre =etnombre.getText().toString();
                String apellido=etapellidos.getText().toString();
                saveFirebase(nomre,apellido);
            }
        });
        alert  = builder1.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }
    private void  DialogOk(String mensaje){
        builder1 = new AlertDialog.Builder(mContext);
        Button btcerrrar;
        TextView tvestado;
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialogo_ok, null);
        animationView = v.findViewById(R.id.animation_viewcheck);
        resetAnimationView();
        animationView.playAnimation();
        builder1.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.idbtncerrardialogo);
        tvestado=(TextView)v.findViewById(R.id.idestado);
        tvestado.setText(mensaje);
        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert  = builder1.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }

    private void resetAnimationView() {
        currentAnimationFrame = 0;
        animationView.addValueCallback(new KeyPath("**"), LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return null;
                    }
                }
        );
    }
    private  void saveFirebase(String nombres,String apellidos){
        ProgressDialog progressDialog= new ProgressDialog(mContext);
        progressDialog.setMessage("Cargando..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String key =databaseReference.push().getKey();
        Map<String,Object> pariente= new HashMap<>();
        pariente.put("nombres",nombres);
        pariente.put("apellidos",apellidos);
        pariente.put("urlfoto","default_image");
        pariente.put("fechanaci","");
        pariente.put("telefono","");
        pariente.put("edad","");
        pariente.put("key",key);

        databaseReference.child("Familiares").child(key).setValue(pariente).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()){
                    alert.dismiss();
                    progressDialog.dismiss();

                    DialogOk("Registrado Familiar");
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

    public void saveImageFirebase(String keyfamiliar, Uri uriphoto){
    
        if (uriphoto!=null){
            ProgressDialog progressDialog= new ProgressDialog(mContext);
            progressDialog.setMessage("Cargando..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            StorageReference fotoref=storageReference.child("Familiares").child(uriphoto.getLastPathSegment());
            fotoref.putFile(uriphoto).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  new Exception();
                    }
                    return fotoref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri uridownload =task.getResult();

                        Map<String,Object> fami= new HashMap<>();
                        fami.put("urlfoto",uridownload.toString());

                        databaseReference.child("Familiares").child(keyfamiliar).updateChildren(fami).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(mContext, "Actualizado foto", Toast.LENGTH_SHORT).show();
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
            });
        }else{
            Toast.makeText(mContext, "No selecciono Imagen ", Toast.LENGTH_SHORT).show();
        }
    }

    public void infoFamiliar(InterfaceFamiliar interfaceFamiliar,String key) {
        databaseReference.child("Familiares").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
             if (snapshot.exists()){
                 FamiliarModel userModel = snapshot.getValue(FamiliarModel.class);
                 NombreFamiliar=userModel.getNombres()+" "+userModel.getApellidos();
                 interfaceFamiliar.onCallback(userModel);
             }
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
                Toast.makeText(mContext, "Err "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  void  cargarDialogoCita(String keyfamiliar){
        builder1 = new AlertDialog.Builder(mContext);
        Button btcerrrar,brnregistrar;
        ImageButton imgbtnhora,imgbtnfecha;
        final EditText etfecha,ethora;
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialogoadd_cita, null);
        builder1.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.btncerrarCita);
        brnregistrar=(Button)v.findViewById(R.id.btnsaveCita);
        imgbtnfecha=(ImageButton)v.findViewById(R.id.ib_obtener_fecha);
        imgbtnhora=(ImageButton)v.findViewById(R.id.ib_obtener_hora);
        etfecha=(EditText)v.findViewById(R.id.etFecha);
        ethora=(EditText)v.findViewById(R.id.etHora);

        imgbtnfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog recogerFecha = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        final int mesActual = month + 1;
                        c.set(year, month, dayOfMonth);
                        String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                        etfecha.setText(simpleDateFormat.format(c.getTime()));
                        tgl_daftar_date = c.getTime();
                    //    etfecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                    }
                },anio, mes, dia);
                recogerFecha.show();
            }
        });
        imgbtnhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog recogerHora = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                        String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                        String AM_PM;
                        if(hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                      //  recogerHora.set
                        ethora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                    }
                }, hora, minuto, false);
                recogerHora.show();
            }
        });
        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        brnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fe =etfecha.getText().toString();
                String hour=ethora.getText().toString();
                saveCitaFirebase(keyfamiliar,fe,hour);
            }
        });
        alert  = builder1.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }
    private void  Notificar(String nombre){
        RequestQueue myrequest = Volley.newRequestQueue(mContext);
        JSONObject json = new JSONObject();
        try {
            String Sucripcion="FamiliaSihuayro";
            json.put("to", "/topics/" + Sucripcion);
            JSONObject notificacion = new JSONObject();
            notificacion.put("titulo", "Nueva Cita");
            notificacion.put("detalle", nombre);
            json.put("data", notificacion);
            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json, null, null) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAFOcMi-k:APA91bFtOu1xbHUqAtmstTQlI-VRi6Nkx-s2vBFtxIgrXsP2TXKP8S0EqVWub70qL73BWNDtypCkPhv2NP_2CzGTxMjV93q9tiICF3xJzztKc8n5Dq39VUhzF_HctlPc1-E5IVjQj3JU");
                    return header;
                }
            };
            myrequest.add(request);
        } catch (JSONException ex) {
            ex.printStackTrace();
            Log.e("err",ex.getMessage());

        }
    }
    private void  CantidaItems(String key){
        databaseReference.child("Familiares").child(key).child("Citas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxin=(snapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
                Toast.makeText(mContext, "Err "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void saveCitaFirebase(String keyfamiliar,String fecha,String hora){
        if (TextUtils.isEmpty(fecha)){
            Toast.makeText(mContext, "poner fecha", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(hora)){
            Toast.makeText(mContext, "poner Hora", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(keyfamiliar)){
            Toast.makeText(mContext, "falto id", Toast.LENGTH_SHORT).show();
        }
        else{

            CantidaItems(keyfamiliar);

            ProgressDialog progressDialog= new ProgressDialog(mContext);
            progressDialog.setMessage("Cargando..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date = new Date();

            String fecha1 = dateFormat.format(date);
            String keyCita =databaseReference.push().getKey();
            Map<Object,Object> cita= new HashMap<>();
            //maxin
            cita.put("maxin", maxin+1);
            cita.put("fecha",  tgl_daftar_date.getTime());
            cita.put("fecha1",  tgl_daftar_date.getTime());
            cita.put("hora",hora);
            cita.put("doctor","");
            cita.put("estado","");
            cita.put("observacion","");
            cita.put("key",keyCita);

            databaseReference.child("Familiares").child(keyfamiliar).child("Citas").child(keyCita).setValue(cita).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()){
                        alert.dismiss();
                        progressDialog.dismiss();
                        Notificar(NombreFamiliar);
                        DialogOk("Se ha Regisrtado Cita");
                     //   Toast.makeText(mContext, "Registrado", Toast.LENGTH_SHORT).show();
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



    public  void cargarRecycler(RecyclerView recyclerView,String keyfamiliar){
        databaseReference.child("Familiares").child(keyfamiliar).child("Citas").addValueEventListener(new ValueEventListener() {
            ArrayList<CitaModel> lista;
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                lista=new ArrayList<>();
                for (DataSnapshot item:snapshot.getChildren()){
                    CitaModel model=item.getValue(CitaModel.class);
                    lista.add(model);
                }
                Collections.sort(lista, Collections.reverseOrder());
                adapterCitas= new AdapterCitas(lista);
                recyclerView.setAdapter(adapterCitas);
                adapterCitas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String keyfam=keyfamiliar;
                        String keycita=lista.get(recyclerView.getChildAdapterPosition(v)).getKey();
                        Intent intent=new Intent(mContext, VistaDetalleCita.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("keyfamiliar",keyfam);
                        bundle.putString("keycita",keycita);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
