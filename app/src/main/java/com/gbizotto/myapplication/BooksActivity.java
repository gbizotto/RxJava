package com.gbizotto.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BooksActivity extends AppCompatActivity {

    @BindView(R.id.books_list)
    RecyclerView booksRecyclerView;
    @BindView(R.id.loader)
    ProgressBar progressBar;
    @BindView(R.id.error_view)
    TextView errorView;

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

    private boolean shouldShowError() {
        return new Date().getTime() % 2 != 0 ? true : false;
    }

    private void createObservable() {
        Observable<List<String>> booksObservable = Observable.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                if (shouldShowError()) {
                    return restClient.getFavoriteBooksWithException();
                }
                return restClient.getFavoriteBooks();
            }
        });
        bookSubscription = booksObservable.
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        new Consumer<List<String>>() {
//                            @Override
//                            public void accept(List<String> strings) throws Exception {
//                                BooksActivity.this.displayBooks(strings);
//                            }
//                        },
//                        new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable error) throws Exception {
//                                BooksActivity.this.displayError();
//                            }
//                        }
//                );
                .subscribe(
                        strings -> displayBooks(strings),
                        error -> displayError()
                );
    }

    private void displayBooks(List<String> books) {
        stringAdapter.setContent(books, true);
        progressBar.setVisibility(View.GONE);
        booksRecyclerView.setVisibility(View.VISIBLE);
    }

    private void displayError() {
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
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
