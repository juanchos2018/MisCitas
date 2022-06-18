package com.example.miscitas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.miscitas.Modelo.FamiliarModel;
import com.example.miscitas.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterFamiliar extends ArrayAdapter<FamiliarModel> {


    Context context;
    //   RecyclerView.ViewHolder viewHolder;
    ViewHolder viewHolder;
    ArrayList<FamiliarModel> al_menu = new ArrayList<>();

    public AdapterFamiliar(Context context, ArrayList<FamiliarModel> al_menu) {
        super(context, R.layout.item_familiares, al_menu);
        this.al_menu = al_menu;
        this.context = context;
    }

    @Override
    public int getCount() {
        return al_menu.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        if (al_menu.size() > 0) {
            return al_menu.size();
        } else {
            return 1;
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView== null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_familiares, parent, false);
            viewHolder.tv_foldern= (TextView) convertView.findViewById(R.id.textView);
            viewHolder.imgfoto=(ImageView)convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (al_menu.get(position).getUrlfoto().equals("default_image")){
            viewHolder.imgfoto.setImageResource(R.drawable.default_profile_image);
        }
        else{
            Picasso.get().load(al_menu.get(position).getUrlfoto()).fit().centerCrop().into(viewHolder.imgfoto);
          //  Glide.with(context)
          //          .load(al_menu.get(position).getUrlfoto())
          //          .placeholder(R.drawable.default_profile_image)
          //          .fitCenter()
          //          .error(R.drawable.default_profile_image)
          //          .centerCrop()
          //          .into(viewHolder.imgfoto);

        }
        viewHolder.tv_foldern.setText(al_menu.get(position).getNombres()+" "+al_menu.get(position).getApellidos());
        return convertView;

    }


    public  void filtrarFamiliar(ArrayList<FamiliarModel> filtro){
        this.al_menu=filtro;
        notifyDataSetChanged();
    }
    public static class ViewHolder {
        TextView tv_foldern, tv_foldersize;
        ImageView imgfoto;

    }


}
