package com.example.kps.myapplication1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class RegistryActivity extends AppCompatActivity {

    final MyGlobal g = MyGlobal.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText cid = (EditText) findViewById(R.id.editText3);
        final EditText pw = (EditText) findViewById(R.id.editText4);
        final EditText email = (EditText) findViewById(R.id.editText5);
        final EditText pw1 = (EditText) findViewById(R.id.editText6);
        final String cids = g.getCid();
        Button sendData1 = (Button) findViewById(R.id.button3);

        sendData1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(cids == null || cids.equals("")) {
                Toast.makeText(RegistryActivity.this,"คุณต้องใส่เลขประชาชน",Toast.LENGTH_LONG).show();
            }
            else if(pw.getText().toString() == null || pw.getText().toString().equals("")) {
                Toast.makeText(RegistryActivity.this,"คุณต้องใส่เรหัสผ่าน",Toast.LENGTH_LONG).show();
            }
            else if(!pw.getText().toString().equals(pw1.getText().toString()))   {
                Toast.makeText(RegistryActivity.this,"คุณพิมพ์รหัสผ่านไม่ตรงกัน",Toast.LENGTH_LONG).show();
            }
            else if(email.getText().toString() == null || email.getText().toString().equals("")) {
                Toast.makeText(RegistryActivity.this,"คุณต้องใส่ email",Toast.LENGTH_LONG).show();
            }
            else    {
                Ion.with(RegistryActivity.this)
                        .load(g.getUrl()+"register.php")
                        .setBodyParameter("cid", cid.getText().toString())
                        .setBodyParameter("pw", pw.getText().toString())
                        .setBodyParameter("email", email.getText().toString())
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                if(result.endsWith("0"))    {
                                    Toast.makeText(getBaseContext(),cid.getText().toString()+" คุณเคยลงทะเบียนแล้ว",Toast.LENGTH_LONG).show();
                                }
                                if(result.endsWith("1"))    {
                                    Toast.makeText(getBaseContext(),cid.getText().toString()+" เลขประชาชนนี้ไม่มีอยู่ในระบบ",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                if(result.endsWith("2"))    {
                                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                }
                            }
                        });
            }

            }

        });

    }

}
