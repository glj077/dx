package com.example.dx_4g;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener {
   private TextView textView;
   private boolean isb=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

    }

    @Override
    public void onClick(View v) {


            Toast.makeText(Main3Activity.this,"1111",Toast.LENGTH_LONG).show();




    }
}
