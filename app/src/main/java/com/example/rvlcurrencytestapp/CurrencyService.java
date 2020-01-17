package com.example.rvlcurrencytestapp;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyService {

    private static final String BASE_URL = "https://revolut.duckdns.org/";

    private RvlApi rvlApi;

    CurrencyService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        rvlApi = retrofit.create(RvlApi.class);

    }

    public Observable<Map<String, String>> getCurrencyRatesApi(String baseCurrency){

        return rvlApi.getCurrencyData(baseCurrency)
                .subscribeOn(Schedulers.io())
                .map(respond -> {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                    Map<String, String> map = mapper.convertValue(respond.getRates(), Map.class);

                    return map;
                });

    }
}
