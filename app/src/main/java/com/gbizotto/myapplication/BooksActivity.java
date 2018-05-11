package com.gbizotto.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BooksActivity extends AppCompatActivity {

    @BindView(R.id.books_list)
    RecyclerView booksRecyclerView;
    @BindView(R.id.loader)
    ProgressBar progressBar;

    private Disposable bookSubscription;
    private SimpleStringAdapter stringAdapter;
    private RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restClient = new RestClient(this);
        configureLayout();
        createObservable();
    }

    private void configureLayout() {
        setContentView(R.layout.activity_books);
        ButterKnife.bind(this);

        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stringAdapter = new SimpleStringAdapter(this);
        booksRecyclerView.setAdapter(stringAdapter);
    }

    private void createObservable() {
        Observable<List<String>> booksObservable = Observable.fromCallable(() -> restClient.getFavoriteBooks());
        bookSubscription = booksObservable.
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(strings -> displayBooks(strings));
    }

    private void displayBooks(List<String> books) {
        stringAdapter.setIsBook(true);
        stringAdapter.setStrings(books);
        progressBar.setVisibility(View.GONE);
        booksRecyclerView.setVisibility(View.VISIBLE);
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
        if (bookSubscription != null && !bookSubscription.isDisposed()) {
            bookSubscription.dispose();
        }
    }
}
