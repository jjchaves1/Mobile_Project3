package com.example.jeffrey.mapactivityrecognition;

import android.content.BroadcastReceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Jeffrey on 2/9/2017.
 */

public class MyReceiver extends BroadcastReceiver {

    private Bitmap bm;
    private ImageView image;
    private TextView textView;
    private boolean prepared = false;

    public MyReceiver(ImageView i, TextView t, Context context) {
        MapsActivity.mediaPlayer = MediaPlayer.create(context, R.raw.beat_02);
        MapsActivity.mediaPlayer.setLooping(true);

        MapsActivity.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                prepared = true;
            }
        });

        image = i;
        textView = t;
    }
    public MyReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        System.out.println("RECEIVED BROADCAST");


        if (bundle != null) {
            int imageFile = (int) bundle.get(ActivityRecognizedService.IMAGE);
            int text = (int) bundle.get(ActivityRecognizedService.TEXT);
            textView.setText(context.getResources().getText(text));
            bm = BitmapFactory.decodeResource(context.getResources(), imageFile);
            image.setImageBitmap(bm);

            // Play the music if we are walking or runnning
            if (context.getResources().getText(text).equals("You are Running") || context.getResources().getText(text).equals("You are Walking")) {
                if (MapsActivity.mediaPlayer.isPlaying() == false) {
                    if(prepared) {
                        MapsActivity.mediaPlayer.start();
                    }
                }
            } else {
                if (MapsActivity.mediaPlayer.isPlaying()) {
                    MapsActivity.mediaPlayer.pause();
                }
            }



        }
    }

}
