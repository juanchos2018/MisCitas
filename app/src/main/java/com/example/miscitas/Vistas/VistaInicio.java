package com.example.miscitas.Vistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.example.miscitas.Presentador.PresenterFamiliar;
import com.example.miscitas.Presentador.PresenterIncio;
import com.example.miscitas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class VistaInicio extends AppCompatActivity  implements View.OnClickListener {

   private FirebaseAuth auth;
    private DatabaseReference reference;
    private PresenterIncio presenterIncio;
    private StorageReference storageReference;
    private EditText etfiltrar;
    FloatingActionButton fab;
    private  PresenterFamiliar presenterFamiliar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_inicio);

        fab= findViewById(R.id.fab1);

        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        presenterIncio = new PresenterIncio(this,reference,auth);
        etfiltrar=(EditText)findViewById(R.id.idetbusfamiliar);
        SharedPreferences preferences= getSharedPreferences("datosuser", Context.MODE_PRIVATE);

        String infouse=preferences.getString("dni",null);
        if (infouse==null){
            startActivity(new Intent(this,VistaLogin.class));
        }
        presenterFamiliar= new PresenterFamiliar(this,reference,storageReference);
        fab.setOnClickListener(this);

        etfiltrar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // searchPeopleProfile(etbuscarnombre.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString().toLowerCase());
            }
        });

    }

    private  void  filtrar(String texto){
        presenterIncio.filtrar(texto);
    }
    private  void initRecycler(){
        GridView gridView=findViewById(R.id.gridViewfamiliares);
        presenterIncio.cargarGriview(gridView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initRecycler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab1:
                presenterFamiliar.cargarDialogo();
                break;
        }
    }
}