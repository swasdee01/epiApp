package com.example.kps.myapplication1;

import android.content.Context;
import android.os.Build;
import android.speech.tts.UtteranceProgressListener;

import java.util.Locale;

public class TextToSpeech extends UtteranceProgressListener implements android.speech.tts.TextToSpeech.OnInitListener, android.speech.tts.TextToSpeech.OnUtteranceCompletedListener {

    public static TextToSpeech myTTS;

    public static TextToSpeech getInstance(Context context) {
        if (myTTS == null) {
            myTTS = new TextToSpeech(context);
        }
        return myTTS;
    }

    private Context context;
    private android.speech.tts.TextToSpeech tts;
    private Locale locale = Locale.getDefault();
    private String enginePackageName;
    private String message;
    private boolean isRunning;
    private int speakCount;

    public TextToSpeech(Context context) {
        this.context = context;
    }

    public void speak(String message) {
        this.message = message;

        if (tts == null || !isRunning) {
            speakCount = 0;

            if (enginePackageName != null && !enginePackageName.isEmpty()) {
                tts = new android.speech.tts.TextToSpeech(context, this, enginePackageName);
            } else {
                tts = new android.speech.tts.TextToSpeech(context, this);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                tts.setOnUtteranceProgressListener(this);
            } else {
                tts.setOnUtteranceCompletedListener(this);
            }
            isRunning = true;
        } else {
            startSpeak();
        }
    }

    public TextToSpeech setEngine(String packageName) {
        enginePackageName = packageName;
        return this;
    }

    public TextToSpeech setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    private void startSpeak() {
        speakCount++;

        if (locale != null) {
            tts.setLanguage(locale);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(message, android.speech.tts.TextToSpeech.QUEUE_ADD, null, "");
        } else {
            tts.speak(message, android.speech.tts.TextToSpeech.QUEUE_ADD, null);
        }
    }

    private void clear() {
        speakCount--;
        if (speakCount == 0) {
            tts.shutdown();
            isRunning = false;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == android.speech.tts.TextToSpeech.SUCCESS) {
            startSpeak();
        }
    }

    @Override
    public void onStart(String utteranceId) {
    }

    @Override
    public void onDone(String utteranceId) {
        clear();
    }

    @Override
    public void onError(String utteranceId) {
        clear();
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        clear();
    }
}
