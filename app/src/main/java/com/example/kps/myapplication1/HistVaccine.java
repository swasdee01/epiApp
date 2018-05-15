package com.example.kps.myapplication1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class HistVaccine extends AppCompatActivity {

    MyGlobal g = MyGlobal.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hist_vaccine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        TextView name = (TextView) findViewById(R.id.textView7);
        name.setText(g.getFullName());

        addAll();

    }

    public void addAll()    {

        final android.widget.LinearLayout layout1 = (android.widget.LinearLayout) findViewById(R.id.layout1);
        final android.widget.LinearLayout layout2 = (android.widget.LinearLayout) findViewById(R.id.layout2);

                Ion.with(HistVaccine.this)
                .load(g.getUrl()+"histVaccine.php")
                .setBodyParameter("cid", g.getCid())
                .setBodyParameter("hospcode", g.gethcode())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (result.endsWith("0")) {
                            Toast.makeText(getBaseContext(), "ไม่มีประวัติวัคซีน", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            String[] sepR = result.split("#");
                            int numR = sepR.length;
                            for (int i = 0; i < numR; i++) {
                                TextView newWord = new TextView(HistVaccine.this);
                                newWord.setText(sepR[i]);
                                newWord.setTextSize(18);
                                newWord.setTextColor(Color.BLACK);
                                if(i%2==1)  {  layout1.addView(newWord);    }
                                else  {  layout2.addView(newWord);    }
                            }
                        }
                    }
                });

    }



}
