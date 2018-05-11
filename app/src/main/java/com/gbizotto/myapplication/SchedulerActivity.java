package com.gbizotto.myapplication;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SchedulerActivity extends AppCompatActivity {

    private Disposable subscription;
    private ProgressBar progressBar;
    private TextView messagearea;
    private View button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        configureLayout();
    }

    private void configureLayout() {
        setContentView(R.layout.activity_scheduler);
        progressBar = findViewById(R.id.progressBar);
        messagearea = findViewById(R.id.messagearea);
        button  = findViewById(R.id.scheduleLongRunningOperation);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
                Observable.fromCallable(callable).
                        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                        doOnSubscribe(disposable ->
                                {
                                    progressBar.setVisibility(View.VISIBLE);
                                    button.setEnabled(false);
                                    messagearea.setText(messagearea.getText().toString() +"\n" +"Progressbar set visible" );
                                }
                        ).
                        subscribe(getDisposableObserver());
            }
        });
    }

    Callable<String> callable = new Callable<String>() {
        @Override
        public String call() throws Exception {
            return doSomethingLong();
        }
    };

    public String doSomethingLong(){
        SystemClock.sleep(1000);
        return "Hello";
    }

    /**
     * Observer
     * Handles the stream of data:
     */
    private DisposableObserver<String> getDisposableObserver() {
        return new DisposableObserver<String>() {

            @Override
            public void onComplete() {
                messagearea.setText(messagearea.getText().toString() +"\n" +"OnComplete" );
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                messagearea.setText(messagearea.getText().toString() +"\n" +"Hidding Progressbar" );
            }

            @Override
            public void onError(Throwable e) {
                messagearea.setText(messagearea.getText().toString() +"\n" +"OnError" );
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                messagearea.setText(messagearea.getText().toString() +"\n" +"Hidding Progressbar" );
            }

            @Override
            public void onNext(String message) {
                messagearea.setText(messagearea.getText().toString() +"\n" +"onNext " + message );
            }
        };
    }


    @Override
    protected void onStop() {
        super.onStop();
        disposeSubscription();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposeSubscription();
    }

    private void disposeSubscription() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }
}
