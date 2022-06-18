package com.example.miscitas.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miscitas.Interface.InterfaceCita;
import com.example.miscitas.Modelo.CitaModel;
import com.example.miscitas.Presentador.PresenterDetalleCita;
import com.example.miscitas.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VistaDetalleCita extends AppCompatActivity  implements View.OnClickListener {

    PresenterDetalleCita presenterDetalleCita;
    private DatabaseReference reference;
    String keyfamiliar,keyCita;

    TextView txfecha,txhora;
    EditText tvdoctor,tvobservacion;
    Button btnGuardar,btneditfecha,btnfinish;
    String fechaoriginal;
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);
    Date tgl_daftar_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_detalle_cita);

        keyfamiliar=getIntent().getStringExtra("keyfamiliar");
        keyCita=getIntent().getStringExtra("keycita");
        reference= FirebaseDatabase.getInstance().getReference();
        presenterDetalleCita = new PresenterDetalleCita(this,reference);
        InitInputs();
        btnGuardar.setOnClickListener(this);
        btneditfecha.setOnClickListener(this);
        btnfinish.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        infoCita(keyfamiliar,keyCita);
    }

    private void  InitInputs(){
        txfecha=(TextView)findViewById(R.id.txfechaCita);
        txhora=(TextView)findViewById(R.id.txHoracita);
        tvdoctor=(EditText)findViewById(R.id.etdoctor);
        tvobservacion=(EditText)findViewById(R.id.etobervacion);
        btnGuardar=(Button)findViewById(R.id.btnguardar);
        btneditfecha=(Button)findViewById(R.id.btnedifecha);
        btnfinish=(Button)findViewById(R.id.btnfinish1);

    }

    private  void  infoCita(String keyfami,String keyCi){
        presenterDetalleCita.infoCita(new InterfaceCita() {
            @Override
            public void onCallback(CitaModel value) {
                txfecha.setText(simpleDateFormat.format(value.getFecha()));
                fechaoriginal=simpleDateFormat.format(value.getFecha());

                txhora.setText(value.getHora());
                tvdoctor.setText(value.getDoctor());
                tvobservacion.setText(value.getObservacion());
            }
        },keyfami,keyCi);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnguardar:
                String doctor =tvdoctor.getText().toString();
                String obver=tvobservacion.getText().toString();
                String fechacambiar  =txfecha.getText().toString();
                boolean cambiofecha =false;
                if (!fechaoriginal.equals(fechacambiar)){
                    cambiofecha=true;
                }
                presenterDetalleCita.saveInfoCitaFirebase (keyfamiliar,keyCita,doctor,obver,cambiofecha);
                break;
            case R.id.btnedifecha:
                presenterDetalleCita.EditFecha(txfecha);
                break;
            case R.id.btnfinish1:
                finish();
                break;
        }
    }
}