package com.example.miscitas;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.miscitas.Vistas.VistaInicio;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class Fcm extends FirebaseMessagingService{
        private static final String TAG = "Fcm";
        @Override
        public void onNewToken(String s) {
            super.onNewToken(s);

            Log.e(TAG, "onNewToken: "+ s);
            guardartoken(s);
        }

        public void guardartoken(String s) {
            //  DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("token");
            //ref.child("matias").setValue(s);

            SharedPreferences preferences=getSharedPreferences("mitoken", Context.MODE_PRIVATE);
            String token=s;
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("token",token);
            editor.commit();

        }

        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            super.onMessageReceived(remoteMessage);
            String from = remoteMessage.getFrom();
            //  Log.e(TAG, "onMessageReceived: "+from );
            if (remoteMessage.getData().size()>0) {
                String tiutlo=remoteMessage.getData().get("titulo");
                String detalle=remoteMessage.getData().get("detalle");
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    mayorqueore(tiutlo,detalle);
                }
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O){

                    menorqueoreo();
                }
            }
        }

        private void menorqueoreo() {
        }
        private void  mayorqueore(String titulo,String detalle){
            String id = "mensaaje";
            NotificationManager nm= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder= new NotificationCompat.Builder(this,id);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel nc= new NotificationChannel(id,"neuvo",NotificationManager.IMPORTANCE_HIGH);
                nc.setShowBadge(true);
                assert nm!=null;
                nm.createNotificationChannel(nc);
            }
            builder.setAutoCancel(true).setWhen(System.currentTimeMillis()).setContentTitle(titulo).setSmallIcon(R.mipmap.ic_launcher).setContentText(detalle).setContentInfo("nuevo ").setContentIntent(clikcnoti());

            Random random= new Random();
            int idNotify=random.nextInt(8000);
            assert nm != null;
            nm.notify(idNotify,builder.build());
        }

        public PendingIntent clikcnoti(){
            Intent intent= new Intent(getApplicationContext(), VistaInicio.class);
            intent.putExtra("color","rojo");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            return PendingIntent.getActivity(this,0,intent,0);
        }
}

