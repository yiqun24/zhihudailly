package com.example.zhihudailly.INTERNET;

import okhttp3.OkHttpClient;

public class Client {
    private static OkHttpClient instance;
    private Client() {

    }

    public static OkHttpClient getInstance() {
        if (instance == null) {
            instance = new OkHttpClient.Builder().build();
        }
        return instance;
    }
}

