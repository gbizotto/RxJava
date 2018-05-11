package com.gbizotto.myapplication;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxJavaSimpleActivity extends AppCompatActivity {

    @BindView(R.id.button)
    View button;
    @BindView(R.id.toastbutton)
    View toastButton;
    @BindView(R.id.resultView)
    TextView resultTextView;

    CompositeDisposable disposable = new CompositeDisposable();
    private int value = 0;

    final Observable<Integer> serverDownloadObservable = Observable.create(emitter -> {
        SystemClock.sleep(10000); // simulate delay
        emitter.onNext(value * 5);
        emitter.onComplete();
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_simple);

        ButterKnife.bind(this);
    }

    private void updateTheUserInterface(int integer) {
        updateTheUserInterface(String.valueOf(integer));
    }

    private void updateTheUserInterface(String value) {
        resultTextView.setText(value);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @OnClick(R.id.toastbutton)
    public void onToastClick(View view) {
        Toast.makeText(this, "Still active " + value++, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button)
    public void onCallServerClick(View serverButton) {
        disableButtons();

        updateTheUserInterface("...");

        Disposable subscribe = serverDownloadObservable.
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                subscribe(integer -> {
                    updateTheUserInterface(integer);
                    enableButtons();
                });

        disposable.add(subscribe);
    }

    private void disableButtons() {
        button.setEnabled(false);
        toastButton.setEnabled(false);
    }

    private void enableButtons() {
        button.setEnabled(true);
        toastButton.setEnabled(true);
    }

}
