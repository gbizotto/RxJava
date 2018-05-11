package com.gbizotto.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onFirstClick(View view) {
        startActivity(new Intent(this, RxJavaSimpleActivity.class));

    }

    public void onSecondClick(View view) {
        startActivity(new Intent(this, ColorsActivity.class));
    }

    public void onThirdClick(View view) {
        startActivity(new Intent(this, BooksActivity.class));
    }
}
