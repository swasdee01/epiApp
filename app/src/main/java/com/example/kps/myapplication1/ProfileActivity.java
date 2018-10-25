package com.example.kps.myapplication1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ProfileActivity extends AppCompatActivity {

    MyGlobal g = MyGlobal.getInstance();
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp = getSharedPreferences("getSP", Context.MODE_PRIVATE);
        String spvacc = sp.getString("pvacc", "0");
        final String cid = g.getCid();
        final TextView tv1 = (TextView) findViewById(R.id.textView3);
        final TextView tv2 = (TextView) findViewById(R.id.textView4);
        final TextView tv3 = (TextView) findViewById(R.id.textView5);
        final TextView tv4 = (TextView) findViewById(R.id.textView6);
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
                            int nr = sep.length-9;
                            if(nr > 0)  {
                                final Dialog dd = new Dialog(ProfileActivity.this);
                                dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dd.setContentView(R.layout.dialog);
                                LinearLayout layout1 = (android.widget.LinearLayout) dd.findViewById(R.id.layout1);
                                Button bdd = (Button) dd.findViewById(R.id.button1);
                                bdd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dd.dismiss();
                                    }
                                });
                                String s1 = "";
                                int a = 9;
                                for(int i = 9; i < 9+(nr/3); i++)   {
                                    TextView newWord = new TextView(ProfileActivity.this);
                                    s1 = "  "+sep[a]+" อายุ "+sep[a+1]+" - "+sep[a+2]+" เดือน  ";
                                    newWord.setText(s1);
                                    newWord.setTextSize(18);
                                    newWord.setTextColor(Color.RED);
                                    newWord.setTypeface(null, Typeface.BOLD);
                                    layout1.addView(newWord);
                                    a = a+3;
                                }
                                dd.show();
                                Runnable run = new Runnable() {
                                    @Override
                                    public void run() {
                                        dd.dismiss();
                                    }
                                };
                                new Handler().postDelayed(run,5000);
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)  {
        int map = 101;
        if(menu.findItem(map) == null )   {
            MenuItem addItem = menu.add(Menu.NONE, map,2, "แผนที่");
            addItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                    return true;
                }
            });
        }
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())   {
            case R.id.knowledge :
                startActivity(new Intent(getApplicationContext(), Knowledge.class));
                return true;
            case R.id.histVaccine :
                startActivity(new Intent(getApplicationContext(), HistVaccine.class).putExtra("cid",g.getCid()));
                return true;
            case R.id.newVaccine :
                startActivity(new Intent(getApplicationContext(), NewVaccine.class).putExtra("cid",g.getCid()));
                return  true;   /*
            case R.id.map :
                startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("cid",g.getCid()));
                return  true;   */
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
