package com.example.varut.workshop1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.varut.workshop1.util.ImageModel;

/**
 * Created by Varut on 04/28/2015.
 */
public class SecondActivity extends ActionBarActivity {

    ImageView imageView;
    TextView textView1,textView2,textView3,textView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        String size = extras.getString("size");
        String date = extras.getString("date");
        String type = extras.getString("type");
        String imageUrl = extras.getString("imageUrl");
        Toast.makeText(this, imageUrl, Toast.LENGTH_SHORT).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.imageView);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);

        Glide.with(getApplicationContext())
                .load(imageUrl)
                .thumbnail(0.1f)
                .into(imageView);
        textView1.setText("fileName : " + name);
        textView2.setText("fileSize : " + size);
        textView3.setText("fileDate : " + date);
        textView4.setText("fileType : " + type);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
