package com.github.crowmisia.aptsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.crowmisia.sample.annotation.Sample;

@Sample
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new MainActivity$Sample();
    }
}
