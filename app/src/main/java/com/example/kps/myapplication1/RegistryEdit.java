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

public class RegistryEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final MyGlobal g = MyGlobal.getInstance();
        final EditText cid = (EditText) findViewById(R.id.editText1);
        final EditText pw = (EditText) findViewById(R.id.editText2);
        final EditText pw1 = (EditText) findViewById(R.id.editText3);
        final EditText email = (EditText) findViewById(R.id.editText4);
        Button sendData1 = (Button) findViewById(R.id.button);
        cid.setText(g.getCid());

        Ion.with(RegistryEdit.this)
                .load(g.getUrl()+"registerE.php")
                .setBodyParameter("cid",g.getCid())
                .setBodyParameter("mode","0")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        String[] sep = result.split("#");
                        pw.setText(sep[0]+"");
                        pw1.setText(sep[0]+"");
                        email.setText(sep[1]+"");
                    }
                });

        sendData1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Ion.with(RegistryEdit.this)
                            .load(g.getUrl()+"registerE.php")
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
                                        Toast.makeText(getBaseContext(),cid.getText().toString()+" เลขประชาชนนี้ไม่มีอยู่ในระบบ กรุณาติดต่อเจ้าหน้าที่เพื่อลงทะเบียน",Toast.LENGTH_LONG).show();
                                    }
                                    if(result.endsWith("2"))    {
                                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                    }
                                }
                            });


            }

        });
    }

}
