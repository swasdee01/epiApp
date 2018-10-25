package com.example.kps.myapplication1;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Gallery extends AppCompatActivity {

    public static final int REQUEST_GALLERY = 1;
    Bitmap bitmap;
    ImageView imageView;
    String dataS = "", dateSer = "",imageStoragePath;
    MyGlobal g = MyGlobal.getInstance();
    Uri uri;
    File imgFile;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);

        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        dateSer = bundle.getString("dateSer");
        dataS = bundle.getString("dataS");
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        openGallery();

        Button btnGallery = findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        Button btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData(imageStoragePath);
            }
        });
    }

    public void uploadData(String mImagePath)    {
        imgFile = new File(mImagePath);
        Ion.with(Gallery.this)
                .load("POST", g.getUrl()+"upload.php")
                .uploadProgressBar(progressBar)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                    }
                })
                .setMultipartFile("image","image/jpeg",imgFile)
                .setMultipartParameter("cid",g.getCid())
                .setMultipartParameter("hcode",g.gethcode())
                .setMultipartParameter("dateSer",dateSer)
                .setMultipartParameter("dataS",dataS)
                .setMultipartParameter("mode","pic")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        progressBar.setProgress(0);
                        final Dialog dialog = new Dialog(Gallery.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        if (result.endsWith("1")) {
                            dialog.setContentView(R.layout.dialog1);
                        }
                        else {
                            dialog.setContentView(R.layout.dialog2);
                        }
                        dialog.show();
                        Button bdd = dialog.findViewById(R.id.button1);
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

    public void openK()   {
        Intent i = new Intent(getApplicationContext(),Knowledge.class);
        startActivity(i);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)  {
        if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK)   {
            uri = data.getData();
            try {
                TextView textView = findViewById(R.id.textView);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imageStoragePath = getPathFromIntent(data);textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
            }   catch (FileNotFoundException e) {
                e.printStackTrace();
            }   catch (IOException e)   {
                e.printStackTrace();
            }
        }
    }

    public void openGallery()   {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),REQUEST_GALLERY);
    }

    public String getPathFromIntent(Intent data)    {
        String realPath;
        if (Build.VERSION.SDK_INT < 19)
            realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
        else
            realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
        return realPath;
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


 class RealPathUtil {

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);
        String id = wholeID.split(":")[1];
        String[] column = { MediaStore.Images.Media.DATA };
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
