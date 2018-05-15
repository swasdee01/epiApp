package com.example.kps.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        Button login = (Button) findViewById(R.id.button);
        Button registry = (Button) findViewById(R.id.button2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText cid = (EditText) findViewById(R.id.editText);
                final EditText pw = (EditText) findViewById(R.id.editText2);
                final String cids = cid.getText().toString();
                final String pws = pw.getText().toString();
                if(cids == null || cids.equals("")) {
                    Toast.makeText(LoginActivity.this,"คุณต้องใส่เลขประชาชน",Toast.LENGTH_LONG).show();
                }
                else if(pws == null || pws.equals("")) {
                    Toast.makeText(LoginActivity.this,"คุณต้องใส่รหัสผ่าน",Toast.LENGTH_LONG).show();
                }
                else {
                    final MyGlobal g = MyGlobal.getInstance();
                    g.setUrl("http://192.168.1.111/epiApp/");
        //            g.setUrl("http://bangwoe2.ddns.net:8088/epiApp/");
                    Ion.with(LoginActivity.this)
                            .load(g.getUrl()+"login.php")
                            .setBodyParameter("cid", cid.getText().toString())
                            .setBodyParameter("pw", pw.getText().toString())
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    if(result.endsWith("0"))    {
                                        Toast.makeText(getBaseContext(),cid.getText().toString()+" คุณยังไม่ได้ลงทะเบียน",Toast.LENGTH_LONG).show();
                                    }
                                    else if(result.endsWith("1"))    {
                                        Toast.makeText(getBaseContext(),cid.getText().toString()+" รหัสผ่านไม่ถูกค้อง",Toast.LENGTH_LONG).show();
                                    }
                                    else if(result.endsWith("2"))    {
                                        g.setCid(cids);
                                        g.setPw(pws);
                                        Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                                        startActivity(i);
                                    }
                                    else    {
                                        Toast.makeText(getBaseContext(),cid.getText().toString()+" ไม่มีข้อมูล",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }
            }
        });

        registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistryActivity.class));
            }
        });
    }

}
