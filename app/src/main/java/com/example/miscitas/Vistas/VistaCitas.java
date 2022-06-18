package com.example.miscitas.Vistas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.miscitas.Interface.InterfaceFamiliar;

import com.example.miscitas.Presentador.PresenterFamiliar;
import com.example.miscitas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class VistaCitas extends AppCompatActivity implements View.OnClickListener  {

    String keyfamiliar;
    PresenterFamiliar presenterFamiliar;
    private DatabaseReference reference;
    private StorageReference storageReference;
    TextView txnombre;
    FloatingActionButton fab;
    ImageView imgperfil;
    private final int PICK_PHOTO=1;

    private Button btngaleria,btnfinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_citas);

        keyfamiliar=getIntent().getStringExtra("key");
        reference= FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        presenterFamiliar= new PresenterFamiliar(this,reference,storageReference);

        fab= findViewById(R.id.fab2);
        txnombre=(TextView)findViewById(R.id.txnombre);
        imgperfil=(ImageView)findViewById(R.id.imgperfil);
        btngaleria=findViewById(R.id.btnupdateimage);
        btnfinish=(Button)findViewById(R.id.btnfinish);
        fab.setOnClickListener(this);
        btngaleria.setOnClickListener(this);
        btnfinish.setOnClickListener(this);
        InfoUser(keyfamiliar);

    }

    private void InfoUser(String keyfamiliar) {
        presenterFamiliar.infoFamiliar(value -> {
            txnombre.setText(value.getNombres()+" "+value.getApellidos());
            if (value.getUrlfoto().equals("default_image")){
                imgperfil.setImageResource(R.drawable.default_profile_image);
            }else{
                Picasso.get().load(value.getUrlfoto()).fit().centerCrop().into(imgperfil);
            }
        }, keyfamiliar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ListaCitas();
    }
    private  void  ListaCitas(){
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclercitas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        presenterFamiliar.cargarRecycler(recyclerView,keyfamiliar);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab2:
                presenterFamiliar.cargarDialogoCita(keyfamiliar);
                break;
            case R.id.btnupdateimage:
                abrirGaleria();
                break;
            case  R.id.btnfinish:
                finish();
                break;
        }
    }
    private void abrirGaleria(){
       Intent intent  = new Intent();
       intent.setType("image/*");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent,"seleccione imagen"),PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_PHOTO && resultCode==RESULT_OK && data!=null && data.getData()!=null){

            Uri uri =data.getData();
            try {
                Bitmap bitmap =MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imgperfil.setImageBitmap(bitmap);
                presenterFamiliar.saveImageFirebase(keyfamiliar,uri);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}