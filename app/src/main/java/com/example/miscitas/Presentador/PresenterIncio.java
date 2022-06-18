package com.example.miscitas.Presentador;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miscitas.Adapters.AdapterFamiliar;
import com.example.miscitas.Modelo.FamiliarModel;
import com.example.miscitas.Modelo.UserModel;
import com.example.miscitas.Vistas.VistaCitas;
import com.example.miscitas.Vistas.VistaDetalleCita;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PresenterIncio {

    private Context mContext;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private AdapterFamiliar adapterFamiliar;
  //  private GridView gridView;
   ArrayList<FamiliarModel> lista;
    public PresenterIncio(Context mContext, DatabaseReference databaseReference, FirebaseAuth firebaseAuth) {
        this.mContext = mContext;
        this.databaseReference = databaseReference;
        this.firebaseAuth = firebaseAuth;
    }

    public  void cargarGriview(GridView gridView){
        databaseReference.child("Familiares").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                lista=new ArrayList<>();
                for (DataSnapshot item:snapshot.getChildren()){
                    FamiliarModel model=item.getValue(FamiliarModel.class);
                    lista.add(model);
                }
                adapterFamiliar= new AdapterFamiliar(mContext,lista);
                gridView.setAdapter(adapterFamiliar);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Intent intent=new Intent(mContext, VistaCitas.class);
                        Bundle bundle= new Bundle();
                        bundle.putString("key",lista.get(position).getKey());
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                        // Toast.makeText(ListaArchivos3Activity.this,  + position + " " + birdList.get(position).getId_carpeta(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void filtrar(String texto) {
        ArrayList<FamiliarModel> filtradatos= new ArrayList<>();
        for(FamiliarModel item :lista){
            if (item.getNombres().toLowerCase().contains(texto)){
                filtradatos.add(item);
            }
            adapterFamiliar.filtrarFamiliar(filtradatos);
        }
    }

    public  void  welComeMessage(){

        databaseReference.child("Usuarios").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                UserModel userModel =snapshot.getValue(UserModel.class);
                Toast.makeText(mContext, "a"+userModel.getNombres(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
    }


}
