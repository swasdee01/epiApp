package com.example.kps.myapplication1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewVaccine extends AppCompatActivity {

    Calendar cal;
    int mYear, mMonth, mDay;
    String QRvacc, spvacc, dateSer , dataS = "";
    String[] monthThai = {"มกราคม","กุมภาพันธ์","มีนาคม","เมษายน","พฤษภาคม","มิถุนายน","กรกฎาคม","สิงหาคม","กันยายน","ตุลาคม","พฤศจิกายน","ธันวาคม"};
    DatePickerDialog picker;
    MyGlobal g = MyGlobal.getInstance();
    CheckBox dtph1, opv1, dtph2, opv2, ipv, dtph3, opv3, mmr1, jel1, dtp4, opv4, mmr2, jel2, dtp5, opv5;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vaccine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Toast.makeText(getBaseContext(), "device type : "+getResources().getString(R.string.device_type), Toast.LENGTH_LONG).show();

        cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH)+1;
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        sp = getSharedPreferences("getSP", Context.MODE_PRIVATE);
        spvacc = sp.getString("pvacc", "0");
        QRvacc = sp.getString("QRvacc", "0");

        chkSP();

        dtph1 = findViewById(R.id.checkBox0);
        opv1 = findViewById(R.id.checkBox1);
        dtph2 = findViewById(R.id.checkBox2);
        opv2 = findViewById(R.id.checkBox3);
        ipv = findViewById(R.id.checkBox4);
        dtph3 = findViewById(R.id.checkBox5);
        opv3 = findViewById(R.id.checkBox6);
        mmr1 = findViewById(R.id.checkBox7);
        jel1 = findViewById(R.id.checkBox8);
        dtp4 = findViewById(R.id.checkBox9);
        opv4 = findViewById(R.id.checkBox10);
        mmr2 = findViewById(R.id.checkBox11);
        jel2 = findViewById(R.id.checkBox12);
        dtp5 = findViewById(R.id.checkBox13);
        opv5 = findViewById(R.id.checkBox14);

        checkVaccQR();
        checkVaccSP();

        Button btnGallery = (Button) findViewById(R.id.button2);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVaccine();
                if (dataS.equals("")) {
                    Toast.makeText(getBaseContext(), "คุณไม่ได้เลือกวัคซีน", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(getApplicationContext(), Gallery.class);
                    i.putExtra("dataS", dataS);
                    i.putExtra("dateSer", dateSer);
                    startActivity(i);
                }
            }
        });

        Button btnQRCode = (Button) findViewById(R.id.button0);
        btnQRCode.setOnClickListener(new View.OnClickListener()    {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewVaccine.this, ScanQR.class);
                intent.putExtra("SCAN_MODE","QR_CODE_MODE");
                startActivity(intent);
            }
        });

        Button btnCamera = (Button) findViewById(R.id.button1);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVaccine();
                if (dataS.equals("")) {
                    Toast.makeText(getBaseContext(), "คุณไม่ได้เลือกวัคซีน", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(getApplicationContext(), CameraA.class);
                    i.putExtra("dataS", dataS);
                    i.putExtra("dateSer", dateSer);
                    startActivity(i);
                }
            }
        });

        final EditText edT1 = (EditText) findViewById(R.id.editText1);
        edT1.setText(mDay+" "+monthThai[mMonth-1]+" "+(mYear+543));

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        dateSer = dateFormat.format(date);

        edT1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                picker = new DatePickerDialog(NewVaccine.this, android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edT1.setText(dayOfMonth+" "+monthThai[month]+" "+year);
                        dateSer = (year-543)+"-"+(month+1)+"-"+dayOfMonth;
                    }
                },mYear, mMonth, mDay);
                picker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cal.set(Calendar.YEAR,(mYear-541));
                picker.getDatePicker().setMinDate(cal.getTimeInMillis());
                cal.set(Calendar.YEAR,(mYear+544));
                picker.getDatePicker().setMaxDate(cal.getTimeInMillis());
                cal.set(Calendar.YEAR,(mYear));
                picker.updateDate((mYear+543),(mMonth-1),mDay);
                picker.show();
          }
        });

        Button bt = (Button) findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                checkVaccine();
                if (dataS.equals("")) {
                    Toast.makeText(getBaseContext(), "คุณไม่ได้เลือกวัคซีน", Toast.LENGTH_LONG).show();
                } else {
                    Ion.with(NewVaccine.this)
                            .load(g.getUrl() + "newVaccine.php")
                            .setBodyParameter("cid", g.getCid())
                            .setBodyParameter("dataS", dataS)
                            .setBodyParameter("hcode", g.gethcode())
                            .setBodyParameter("dateSer", dateSer)
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    final Dialog dialog = new Dialog(NewVaccine.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    if (result.endsWith("1")) {
                                        dialog.setContentView(R.layout.dialog1);
                                    }
                                    else {
                                        dialog.setContentView(R.layout.dialog2);
                                    }
                                    dialog.show();
                                    Button bdd = (Button) dialog.findViewById(R.id.button1);
                                    bdd.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                            openK();
                                            finish();
                                        }
                                    });
                                    Runnable run = new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            openK();
                                            finish();
                                        }
                                    };
                                    new Handler().postDelayed(run,3000);
                                }
                            });
                }
            }
        });
    }
    public void openK()   {
        Intent i = new Intent(getApplicationContext(),Knowledge.class);
        startActivity(i);
    }
/*
    public  void onActivityResult(int requestCode, int resultCode, Intent intent)   {
        if(requestCode == 0 && resultCode == RESULT_OK)   {
            String contents = intent.getStringExtra("SCAN_RESULT");
            Toast.makeText(getBaseContext(),"code = "+contents,Toast.LENGTH_LONG).show();
        }
    }
*/
    public void chkSP()     {
        Ion.with(NewVaccine.this)
                .load(g.getUrl()+"chkVaccine.php")
                .setBodyParameter("cid",g.getCid())
                .setBodyParameter("hcode",g.gethcode())
                .setBodyParameter("chkVacc",spvacc)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(!result.endsWith("0"))    {
                            String[] sepR = result.split("#");
                            int nr = sepR.length;
                            for(int i = 0; i < nr; i++)     {
                                spvacc = spvacc.replace(sepR[i], "");
                            }   //      spvacc="";
                            spvacc = spvacc.replace("##", "#");
                            editor = sp.edit();
                            editor.putString("pvacc",spvacc);
                            editor.commit();
                        }
                    }
                });

        if(spvacc.length() < 3)   {
            spvacc = "";
            editor = sp.edit();
            editor.putString("pvacc",spvacc);
            editor.commit();
        }
    }

    public void checkVaccQR()   {
        if(QRvacc.contains("091"))  {   dtph1.setChecked(true);  }
        if(QRvacc.contains("081"))  {   opv1.setChecked(true);   }
        if(QRvacc.contains("092"))  {   dtph2.setChecked(true);  }
        if(QRvacc.contains("082"))  {   opv2.setChecked(true);   }
        if(QRvacc.contains("401"))  {   ipv.setChecked(true);   }
        if(QRvacc.contains("093"))  {   dtph3.setChecked(true); }
        if(QRvacc.contains("083"))  {   opv3.setChecked(true);  }
        if(QRvacc.contains("061"))  {   mmr1.setChecked(true);  }
        if(QRvacc.contains("J11"))  {   jel1.setChecked(true);  }
        if(QRvacc.contains("034"))  {   dtp4.setChecked(true);  }
        if(QRvacc.contains("084"))  {   opv4.setChecked(true);  }
        if(QRvacc.contains("073"))  {   mmr2.setChecked(true);  }
        if(QRvacc.contains("J12"))  {   jel2.setChecked(true);  }
        if(QRvacc.contains("035"))  {   dtp5.setChecked(true);  }
        if(QRvacc.contains("085"))  {   opv5.setSelected(true); }
        editor = sp.edit();
        editor.putString("QRvacc","");
        editor.commit();
    }

    public void checkVaccSP()   {
        if(spvacc.contains("091"))  {   dtph1.setEnabled(false);   }
        else    {   dtph1.setEnabled(true);    }
        if(spvacc.contains("081"))  {   opv1.setEnabled(false); }
        else    {   opv1.setEnabled(true);    }
        if(spvacc.contains("092"))  {   dtph2.setEnabled(false);   }
        else    {   dtph2.setEnabled(true);    }
        if(spvacc.contains("082"))  {   opv2.setEnabled(false);  }
        else    {   opv2.setEnabled(true);    }
        if(spvacc.contains("401"))  {   ipv.setEnabled(false);  }
        else    {   ipv.setEnabled(true);    }
        if(spvacc.contains("093"))  {   dtph3.setEnabled(false);    }
        else    {   dtph3.setEnabled(true);    }
        if(spvacc.contains("083"))  {   opv3.setEnabled(false); }
        else    {   opv3.setEnabled(true);    }
        if(spvacc.contains("061"))  {   mmr1.setEnabled(false); }
        else    {   mmr1.setEnabled(true);    }
        if(spvacc.contains("J11"))  {   jel1.setEnabled(false); }
        else    {   jel1.setEnabled(true);    }
        if(spvacc.contains("034"))  {   dtp4.setEnabled(false); }
        else    {   dtp4.setEnabled(true);    }
        if(spvacc.contains("084"))  {   opv4.setEnabled(false); }
        else    {   opv4.setEnabled(true);    }
        if(spvacc.contains("073"))  {   mmr2.setEnabled(false); }
        else    {   mmr2.setEnabled(true);    }
        if(spvacc.contains("J12"))  {   jel2.setEnabled(false); }
        else    {   jel2.setEnabled(true);    }
        if(spvacc.contains("035"))  {   dtp5.setEnabled(false); }
        else    {   dtp5.setEnabled(true);    }
        if(spvacc.contains("085"))  {   opv5.setEnabled(false); }
        else    {   opv5.setEnabled(true);    }
    }

    public void checkVaccine()    {
        if(dtph1.isChecked())   {
            if(dataS.equals(""))   {   dataS = "091";  }
            else    {   dataS = dataS+"#091";   }
        }
        if(opv1.isChecked())   {
            if(dataS.equals(""))   {   dataS = "081";  }
            else    {   dataS = dataS+"#081";   }
        }
        if(dtph2.isChecked())   {
            if(dataS.equals(""))   {   dataS = "092";  }
            else    {   dataS = dataS+"#092";   }
        }
        if(opv2.isChecked())   {
            if(dataS.equals(""))   {   dataS = "082";  }
            else    {   dataS = dataS+"#082";   }
        }
        if(ipv.isChecked())   {
            if(dataS.equals(""))   {   dataS = "401";  }
            else    {   dataS = dataS+"#401";   }
        }
        if(dtph3.isChecked())   {
            if(dataS.equals(""))   {   dataS = "093";  }
            else    {   dataS = dataS+"#093";   }
        }
        if(opv3.isChecked())   {
            if(dataS.equals(""))   {   dataS = "083";  }
            else    {   dataS = dataS+"#083";   }
        }
        if(mmr1.isChecked())   {
            if(dataS.equals(""))   {   dataS = "061";  }
            else    {   dataS = dataS+"#061";   }
        }
        if(jel1.isChecked())   {
            if(dataS.equals(""))   {   dataS = "J11";  }
            else    {   dataS = dataS+"#J11";   }
        }
        if(dtp4.isChecked())   {
            if(dataS.equals(""))   {   dataS = "034";  }
            else    {   dataS = dataS+"#034";   }
        }
        if(opv4.isChecked())   {
            if(dataS.equals(""))   {   dataS = "084";  }
            else    {   dataS = dataS+"#084";   }
        }
        if(mmr2.isChecked())   {
            if(dataS.equals(""))   {   dataS = "073";  }
            else    {   dataS = dataS+"#073";   }
        }
        if(jel2.isChecked())   {
            if(dataS.equals(""))   {   dataS = "J12";  }
            else    {   dataS = dataS+"#J12";   }
        }
        if(dtp5.isChecked())   {
            if(dataS.equals(""))   {   dataS = "035";  }
            else    {   dataS = dataS+"#035";   }
        }
        if(opv5.isChecked())   {
            if(dataS.equals(""))   {   dataS = "085";  }
            else    {   dataS = dataS+"#085";   }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())   {
            case R.id.knowledge :
                startActivity(new Intent(getApplicationContext(), Knowledge.class).putExtra("cid",g.getCid()));
                return true;
            case R.id.histVaccine :
                startActivity(new Intent(getApplicationContext(), HistVaccine.class).putExtra("cid",g.getCid()));
                return true;
            case R.id.newVaccine :
                startActivity(new Intent(getApplicationContext(), NewVaccine.class).putExtra("cid",g.getCid()));
                return  true;
            case R.id.exit :
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Exit me", true);
                startActivity(intent);
                finish();
            default: return super.onOptionsItemSelected(item);
        }
    }

}
