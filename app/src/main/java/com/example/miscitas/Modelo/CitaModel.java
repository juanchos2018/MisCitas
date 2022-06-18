package com.example.miscitas.Modelo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class CitaModel implements  Comparable<CitaModel> {

//implements  Comparable<CitaModel>
    private Integer maxin;
    private String key;
    private String hora;
    private String estado;
    private String doctor;
    private String observacion;
    private long fecha;
    private long  fecha1;


    Locale id = new Locale("in","ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy",id);


    public  CitaModel(){

    }

    public CitaModel(int maxin,String key, String hora, String estado, String doctor, String observacion, long fecha) {
        this.maxin=maxin;
        this.key = key;
        this.hora = hora;
        this.estado = estado;
        this.doctor = doctor;
        this.observacion = observacion;
        this.fecha = fecha;
     //   this.fecha1 = fecha1;
    }

    public int getMaxin() {
        return maxin;
    }

    public void setMaxin(int maxin) {
        this.maxin = maxin;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

   public long getFecha1() {
       return fecha1;
   }
//
    public void setFecha1(long fecha1) {
        this.fecha1 = fecha1;
    }

// public void setFecha1(long fecha1) {
  //     this.fecha1 = fecha1;
  // }

@Override
    public int compareTo(CitaModel o) {
        // return nombre.compareTo(o.getNombre());

    //Date d = new Date(o.getFecha1());

    //return   simpleDateFormat.format(fecha).compareTo(simpleDateFormat.format(o.getFecha())); //fecha.compareTo(o.getFecha());
    return maxin.compareTo(o.getMaxin());
        //simpleDateFormat.format(dataUser.getFecha()
        //  return  String.valueOf( cantidad_pendiente).compareTo(String.valueOf(o.cantidad_pendiente));
        //  return cantidad_pendiente.compareTo(o.getCantidad_pendiente());
    }
}
