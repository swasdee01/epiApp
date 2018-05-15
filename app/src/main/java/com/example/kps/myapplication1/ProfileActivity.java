package com.example.kps.myapplication1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        final MyGlobal g = MyGlobal.getInstance();
        final String cid = g.getCid();
        final TextView tv1 = (TextView) findViewById(R.id.textView3);
        final TextView tv2 = (TextView) findViewById(R.id.textView4);
        final TextView tv3 = (TextView) findViewById(R.id.textView5);
        final TextView tv4 = (TextView) findViewById(R.id.textView6);
  //      final TextView tv5 = (TextView) findViewById(R.id.textView7);
        Button histVaccine = (Button) findViewById(R.id.button4);
        Button newVaccine = (Button) findViewById(R.id.button5);

        Ion.with(ProfileActivity.this)
                .load(g.getUrl()+"profile.php")
                .setBodyParameter("cid", cid)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(result.endsWith("0")) {
                            Toast.makeText(getBaseContext(), "ไม่มีข้อมูล", Toast.LENGTH_LONG).show();
                        }
                        else    {
                            String[] sep = result.split("#");
                            String str1 = "";
                            if(sep[5].equals("003"))   str1 = "ด.ช."+sep[6]+"  "+sep[7];
                            if(sep[5].equals("004"))   str1 = "ด.ญ."+sep[6]+"  "+sep[7];

                            tv1.setText(str1);
                            tv2.setText("เลขประชาชน "+sep[0]);
                            tv3.setText("เกิด "+sep[1]);
                            tv4.setText("อายุ  "+sep[2]+" ปี "+sep[3]+" เดือน "+sep[4]+" วัน");
                            g.setHcode(sep[8]);
                            g.setFullName(str1);
             //               tv5.setText(result);
                            int nr = sep.length-9;
                            if(nr > 0)  {
                                LinearLayout layout0 = (android.widget.LinearLayout) findViewById(R.id.layout0);
             /*                   TextView headWord = new TextView(ProfileActivity.this);
                                headWord.setText("รายการวัคซีนที่ควรได้รับ");
                                headWord.setTextSize(18);
                                headWord.setTextColor(Color.RED);
                                layout0.addView(headWord);          */
                                for(int i = 9; i < 9+(nr/3); i++)   {
                                    TextView newWord = new TextView(ProfileActivity.this);
                                    newWord.setText("ควรได้รับ "+sep[i]+" อายุ "+sep[i+1]+" - "+sep[i+2]+" เดือน");
                                    newWord.setTextSize(18);
                                    newWord.setTextColor(Color.RED);
                                    newWord.setTypeface(null, Typeface.BOLD);
                                    layout0.addView(newWord);
                                }
                            }
                        }
                    }
                });

        histVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),HistVaccine.class);
                startActivity(i);
            }
        });

        newVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),NewVaccine.class);
                startActivity(i);
            }
        });

    }



}
