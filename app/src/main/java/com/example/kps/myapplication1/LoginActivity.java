package com.example.kps.myapplication1;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends AppCompatActivity {

//    String urlServer = "http://10.0.2.2/epiApp/";
//    String urlServer = "http://192.168.1.144/epiApp/";
    String urlServer = "http://183.88.213.112:8888/epiApp/";
//    String urlServer = "http://bangwoe3.ddns.net:8088/epiApp/";
//    String urlServer = "http://1.0.187.152:8088/epiApp/";
    MyGlobal g = MyGlobal.getInstance();
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    EditText cid, pw;
    String cids , pws , spcid, sppw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return;
        }

        if(urlServer.equals("http://183.88.213.112:8888/epiApp/"))  {
            if(!isConnected("http://183.88.213.112:8888/epiApp/",1000))  {
 //  not work             urlServer = "http://bangwoe3.ddns.net:8088/epiApp/";
            }
        }

        Button login = findViewById(R.id.button);
        Button registry = findViewById(R.id.button2);
        Button editProfile = findViewById(R.id.button3);

        g.setUrl(urlServer);
        cid = findViewById(R.id.editText);
        pw = findViewById(R.id.editText2);

        sp = getSharedPreferences("getSP", Context.MODE_PRIVATE);
        spcid = sp.getString("spcid", "0");
        sppw = sp.getString("sppw","0");
        if(!spcid.equals("0"))  {
            cid.setText(spcid);
            pw.setText(sppw);
            loginC();
        }

            login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginC();
            }
        });

        registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),RegistryActivity.class);
                i.putExtra("mode","1");
                startActivity(i);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cids = cid.getText().toString();
                if(cids.equals("")) {
                    Toast.makeText(LoginActivity.this,"คุณต้องใส่เลขประชาชน",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent i = new Intent(getApplicationContext(),RegistryEdit.class);
                    i.putExtra("mode","0");
                    g.setCid(cids);
                    startActivity(i);
                }
            }
        });

    }

    public void loginC()    {
        cids = cid.getText().toString();
        pws = pw.getText().toString();
        if(cids == null || cids.equals("")) {
            Toast.makeText(LoginActivity.this,"คุณต้องใส่เลขประชาชน",Toast.LENGTH_LONG).show();
        }
        else if(pws == null || pws.equals("")) {
            Toast.makeText(LoginActivity.this,"คุณต้องใส่รหัสผ่าน",Toast.LENGTH_LONG).show();
        }
        else {
            g.setUrl(urlServer);
            Ion.with(LoginActivity.this)
                    .load(g.getUrl()+"login.php")
                    .setBodyParameter("cid", cids)
                    .setBodyParameter("pw", pws)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if(result.endsWith("0"))    {
                                Toast.makeText(getBaseContext(),cids+" คุณยังไม่ได้ลงทะเบียน",Toast.LENGTH_LONG).show();
                            }
                            else if(result.endsWith("1"))    {
                                Toast.makeText(getBaseContext(),cids+" รหัสผ่านไม่ถูกค้อง",Toast.LENGTH_LONG).show();
                            }
                            else if(result.endsWith("2"))    {
                                g.setCid(cids);
                                g.setPw(pws);
                                Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                                startActivity(i);
                                if(spcid.equals("0"))  {
                                    editor = sp.edit();
                                    editor.putString("spcid",cids);
                                    editor.putString("sppw",pws);
                                    editor.commit();
                                }
                            }
                            else    {
                                Toast.makeText(getBaseContext(),cids+" ไม่มีข้อมูล",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public boolean isConnected (String url, int timeout)    {
        try {
            URL myurl = new URL(url);
            URLConnection connection = myurl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        }   catch (Exception e) {
            return false;
        }
    }
}
