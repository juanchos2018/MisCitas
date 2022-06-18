package com.example.miscitas.Presentador;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.example.miscitas.Interface.InterfaceCita;
import com.example.miscitas.Interface.InterfaceFamiliar;
import com.example.miscitas.Interface.InterfaceResponse;
import com.example.miscitas.Modelo.CitaModel;
import com.example.miscitas.Modelo.FamiliarModel;
import com.example.miscitas.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PresenterDetalleCita {

    private Context mContext;
    private DatabaseReference databaseReference;
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);
    Date tgl_daftar_date;
    public final Calendar c = Calendar.getInstance();
    private static final String CERO = "0";
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    android.app.AlertDialog.Builder builder1;
    AlertDialog alert;
    private Boolean editarFechadialogo=false;
    private final int frames = 9;
    private int currentAnimationFrame = 0;
    private LottieAnimationView animationView;


    public PresenterDetalleCita(Context mContext, DatabaseReference databaseReference) {
        this.mContext = mContext;
        this.databaseReference = databaseReference;
    }


    public void infoCita(InterfaceCita interfaceCita, String keyfamiliar, String keycita) {
        databaseReference.child("Familiares").child(keyfamiliar).child("Citas").child(keycita).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    CitaModel userModel = snapshot.getValue(CitaModel.class);
                    interfaceCita.onCallback(userModel);
                }
            }
            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
                Toast.makeText(mContext, "Err "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveInfoCitaFirebase(String keyfamiliar,String keycita,String doctor,String observacion,Boolean cambiofehca){
        if (TextUtils.isEmpty(keyfamiliar)){
            Toast.makeText(mContext, "poner keyfamiliar", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(keycita)){
            Toast.makeText(mContext, "falto keycita", Toast.LENGTH_SHORT).show();
        }
        else{
            Map<String,Object> cita= new HashMap<>();
            cita.put("doctor",doctor);
            cita.put("estado","");
            cita.put("observacion",observacion);

            if (cambiofehca){
                if (tgl_daftar_date.getTime()!=0){
                    cita.put("fecha", tgl_daftar_date.getTime());
                    DialoQuestion(new InterfaceResponse() {
                        @Override
                        public void onResponse(boolean resp) {
                            if (resp){
                                ProgressDialog progressDialog= new ProgressDialog(mContext);
                                progressDialog.setMessage("Cargando..");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                databaseReference.child("Familiares").child(keyfamiliar).child("Citas").child(keycita).updateChildren(cita).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            Toast.makeText(mContext, "Actualizado", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(mContext, "err "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });;
                            }
                        }
                    },"Desea Cambiar la Fecha");

                }
            }else{
                ProgressDialog progressDialog= new ProgressDialog(mContext);
                progressDialog.setMessage("Cargando..");
                progressDialog.setCancelable(false);
                progressDialog.show();
                databaseReference.child("Familiares").child(keyfamiliar).child("Citas").child(keycita).updateChildren(cita).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(mContext, "Actualizado", Toast.LENGTH_SHORT).show();
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
    }

    public void EditFecha(TextView etfecha){
        DatePickerDialog recogerFecha = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                c.set(year, month, dayOfMonth);
                etfecha.setText(simpleDateFormat.format(c.getTime()));
                tgl_daftar_date = c.getTime();

            }
        },anio, mes, dia);
        recogerFecha.show();
    }
    private void  DialoQuestion(InterfaceResponse response,String mensaje){
        builder1 = new AlertDialog.Builder(mContext);
        Button btcerrrar,btnsi;
        TextView tvestado;
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialogo_pregunta, null);
        animationView = v.findViewById(R.id.animation_question);
        resetAnimationView();
        animationView.playAnimation();
        builder1.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.btncerrareditar);
        btnsi=(Button)v.findViewById(R.id.btneditar);
        tvestado=(TextView)v.findViewById(R.id.idestado);
        tvestado.setText(mensaje);
        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                response.onResponse(false);
            }
        });
        btnsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                editarFechadialogo=true;
                response.onResponse(true);
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
}
