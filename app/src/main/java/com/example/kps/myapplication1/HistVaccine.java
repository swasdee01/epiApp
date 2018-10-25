package com.example.kps.myapplication1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView name = findViewById(R.id.textView7);
        name.setText(g.getFullName());

        addAll();
    }

    public void addAll()    {

        final android.widget.LinearLayout layout1 = findViewById(R.id.layout1);
        final android.widget.LinearLayout layout2 = findViewById(R.id.layout2);

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
                                newWord.setHeight(70);
                                if(i%2==1)  {  layout1.addView(newWord);    }
                                else  {  layout2.addView(newWord);    }
                            }
                        }
                    }
                });
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
