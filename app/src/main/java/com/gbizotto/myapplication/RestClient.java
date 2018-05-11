package com.gbizotto.myapplication;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;

public class RestClient {

    public List<String> getFavoriteBooks() {
        SystemClock.sleep(8000);// "Simulate" the delay of network.
        return createBooks();
    }

    public List<String> getFavoriteBooksWithException() throws Exception {
        SystemClock.sleep(2000);// "Simulate" the delay of network.
        throw new Exception("Failed to load");
    }

    private List<String> createBooks() {
        List<String> books = new ArrayList<>();
        books.add("Lord of the Rings");
        books.add("The dark elf");
        books.add("Eclipse Introduction");
        books.add("History book");
        books.add("Der kleine Prinz");
        books.add("7 habits of highly effective people");
        books.add("Other book 1");
        books.add("Other book 2");
        books.add("Other book 3");
        books.add("Other book 4");
        books.add("Other book 5");
        books.add("Other book 6");
        return books;
    }
}
