package com.example.kps.myapplication1;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import io.blackbox_vision.datetimepickeredittext.view.DatePickerEditText;

public class NewVaccine extends AppCompatActivity {

    private DatePickerEditText mDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vaccine);
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
        mDatePicker = (DatePickerEditText) findViewById(R.id.datepicker);
        mDatePicker.setManager(getSupportFragmentManager());
        DateFormat formatter = new SimpleDateFormat("d MMMM yyyy", new Locale("th","TH"));

//        final Calendar calendar = Calendar.getInstance(new Locale("th", "TH"));
        final Calendar calendar = Calendar.getInstance();
        Toast.makeText(NewVaccine.this, formatter.format(new Date()),Toast.LENGTH_LONG).show();
        mDatePicker.setDate(calendar);
//        ((DateFormat) format).format(mDatePicker);
//        mDatePicker.setDateFormat((DateFormat) formatter);


        Button bt = (Button) findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            final MyGlobal g = MyGlobal.getInstance();
            String dataS = "";
            CheckBox dtph1 = (CheckBox) findViewById(R.id.checkBox);
            CheckBox opv1 = (CheckBox) findViewById(R.id.checkBox_1);
            CheckBox dtph2 = (CheckBox) findViewById(R.id.checkBox1);
            CheckBox opv2 = (CheckBox) findViewById(R.id.checkBox1_1);
            CheckBox ipv = (CheckBox) findViewById(R.id.checkBox2);
            CheckBox dtph3 = (CheckBox) findViewById(R.id.checkBox3);
            CheckBox opv3 = (CheckBox) findViewById(R.id.checkBox3_1);
            CheckBox mmr1 = (CheckBox) findViewById(R.id.checkBox4);
            CheckBox jel1 = (CheckBox) findViewById(R.id.checkBox5);
            CheckBox dtp4 = (CheckBox) findViewById(R.id.checkBox6);
            CheckBox opv4 = (CheckBox) findViewById(R.id.checkBox6_1);
            CheckBox mmr2 = (CheckBox) findViewById(R.id.checkBox7);
            CheckBox jel2 = (CheckBox) findViewById(R.id.checkBox8);
            CheckBox dtp5 = (CheckBox) findViewById(R.id.checkBox9);
            CheckBox opv5 = (CheckBox) findViewById(R.id.checkBox9_1);


            @Override
            public void onClick(View view) {
                if(dtph1.isChecked())   {
                    dataS = "091";
                }
                if(opv1.isChecked())   {
                    if(dataS=="")   {   dataS = "081";  }
                    else    {   dataS = dataS+",081";   }
                }
                if(dtph2.isChecked())   {
                        if(dataS=="")   {   dataS = "092";  }
                        else    {   dataS = dataS+",092";   }
                }
                if(opv2.isChecked())   {
                    if(dataS=="")   {   dataS = "082";  }
                    else    {   dataS = dataS+",082";   }
                }
                if(ipv.isChecked())   {
                    if(dataS=="")   {   dataS = "401";  }
                    else    {   dataS = dataS+",401";   }
                }
                if(dtph3.isChecked())   {
                    if(dataS=="")   {   dataS = "093";  }
                    else    {   dataS = dataS+",093";   }
                }
                if(opv3.isChecked())   {
                    if(dataS=="")   {   dataS = "083";  }
                    else    {   dataS = dataS+",083";   }
                }
                if(mmr1.isChecked())   {
                    if(dataS=="")   {   dataS = "061";  }
                    else    {   dataS = dataS+",061";   }
                }
                if(jel1.isChecked())   {
                    if(dataS=="")   {   dataS = "J11";  }
                    else    {   dataS = dataS+",J11";   }
                }
                if(dtp4.isChecked())   {
                    if(dataS=="")   {   dataS = "034";  }
                    else    {   dataS = dataS+",034";   }
                }
                if(opv4.isChecked())   {
                    if(dataS=="")   {   dataS = "084";  }
                    else    {   dataS = dataS+",084";   }
                }
                if(mmr2.isChecked())   {
                    if(dataS=="")   {   dataS = "073";  }
                    else    {   dataS = dataS+",073";   }
                }
                if(jel2.isChecked())   {
                    if(dataS=="")   {   dataS = "J12";  }
                    else    {   dataS = dataS+",J12";   }
                }
                if(dtp5.isChecked())   {
                    if(dataS=="")   {   dataS = "035";  }
                    else    {   dataS = dataS+",035";   }
                }
                if(opv5.isChecked())   {
                    if(dataS=="")   {   dataS = "085";  }
                    else    {   dataS = dataS+",085";   }
                }

                if(dataS=="") {
                    android.widget.Toast.makeText(getBaseContext(), "คุณไม่ได้เลือกวัคซีน" , android.widget.Toast.LENGTH_LONG).show();
                }
                else    {
                    java.util.Calendar cal = mDatePicker.getDate();
                    final String dateSer = cal.get(cal.YEAR)+"-"+(cal.get(cal.MONTH)+1)+"-"+cal.get(cal.DATE);

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
                                    if (result.endsWith("1")) {
                                        android.widget.Toast.makeText(getBaseContext(), "บันทึกข้อมูลวัคซีนเรียบร้อยแล้ว" , android.widget.Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    else    {
                                        android.widget.Toast.makeText(getBaseContext(), "ไม่สามารถบันทึกวัคซีนได้" , android.widget.Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        findViewById(R.id.butt).setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), datepickerD.class));
            }
        });
/*
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDatePicker.getDate() == null ) {
                    return;
                }

                java.util.Calendar cal = mDatePicker.getDate();
                String date = cal.get(cal.DATE) + "-" + (cal.get(cal.MONTH) + 1) + "-" + cal.get(cal.YEAR);

                android.widget.Toast.makeText(getBaseContext(), date , android.widget.Toast.LENGTH_LONG).show();
            }
        });
*/

    }


}
