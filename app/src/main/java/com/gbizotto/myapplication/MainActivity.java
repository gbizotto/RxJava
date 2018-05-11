package com.gbizotto.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.first, R.id.second, R.id.third})
    public void onFirstClick(View view) {
        Class nextActivity = null;
        switch (view.getId()) {
            case R.id.first:
                nextActivity = RxJavaSimpleActivity.class;
                break;
            case R.id.second:
                nextActivity = ColorsActivity.class;
                break;
            case R.id.third:
                nextActivity = BooksActivity.class;
                break;
        }

        if (nextActivity != null) {
            startActivity(new Intent(this, nextActivity));
        }
    }
}
