package com.example.rvlcurrencytestapp;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RvlApi {
    @GET("latest")
    Observable<CurrencyData> getCurrencyData(@Query("base") String baseCurrency);
}
