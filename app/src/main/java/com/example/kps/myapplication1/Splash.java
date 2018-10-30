package com.example.kps.myapplication1;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Locale;

public class Splash extends AppCompatActivity {

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Runnable run = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        textToSpeech();
        new Handler().postDelayed(run,3000);
    }

    public void textToSpeech()  {
        final String string = "My Baby & Vaccine Application";
        tts = new android.speech.tts.TextToSpeech(getApplicationContext(), new android.speech.tts.TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS)    {
                    tts.setLanguage(Locale.US);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tts.speak(string, TextToSpeech.QUEUE_FLUSH, null, "");
                    } else {
                        tts.speak(string, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });
    }
}
