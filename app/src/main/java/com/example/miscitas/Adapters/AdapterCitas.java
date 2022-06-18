package com.example.miscitas.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miscitas.Modelo.CitaModel;
import com.example.miscitas.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdapterCitas  extends RecyclerView.Adapter<AdapterCitas.ViewHolderDatos> implements View.OnClickListener{


    ArrayList<CitaModel> listaCitas;
    Locale id = new Locale("in","ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy",id);
    public AdapterCitas(ArrayList<CitaModel> listaCitas) {
        this.listaCitas = listaCitas;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate( R.layout.item_citas,parent,false);
        vista.setOnClickListener(this);
        return new ViewHolderDatos(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterCitas.ViewHolderDatos holder, int position) {
       // holder.viewBind(listaCitas.get(position));
        if (holder instanceof ViewHolderDatos){
            final ViewHolderDatos datgolder =(ViewHolderDatos)holder;
            datgolder.fecha.setText(simpleDateFormat.format(listaCitas.get(position).getFecha()));
            datgolder.hora.setText(listaCitas.get(position).getHora());

        }
    }

    @Override
    public int getItemCount() {
        return listaCitas.size();
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }
    private View.OnClickListener listener;

    public  void setOnClickListener(View.OnClickListener listener)
    {
        this.listener=listener;
    }



    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView fecha,hora,nombremedico,especialidad,tvestado;
        public ViewHolderDatos(@NonNull @NotNull View itemView) {
            super(itemView);

            fecha=(TextView)itemView.findViewById( R.id.tvfecha);
            hora=(TextView)itemView.findViewById( R.id.tvhora);


         //   nombremedico=(TextView)itemView.findViewById( R.id.idtvNombreMedico);
          //  tvestado=(TextView)itemView.findViewById(R.id.tvestado);

        }
       // public void viewBind(CitaModel dataUser) {
       //     hora.setText(dataUser.getHora());
       //     fecha.setText(simpleDateFormat.format(dataUser.getFecha()));
       // }
    }
}
