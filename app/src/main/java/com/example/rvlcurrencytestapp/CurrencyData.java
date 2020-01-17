package com.example.rvlcurrencytestapp;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class CurrencyData {
    private String base;
    private Rates rates;

    @Data
    @Getter
    public class Rates {
        private String AUD;
        private String BGN;
        private String BRL;
        private String CAD;
        private String CHF;
        private String CNY;
        private String CZK;
        private String DKK;
        private String EUR;
        private String GBP;
        private String HKD;
        private String HRK;
        private String HUF;
        private String IDR;
        private String ILS;
        private String INR;
        private String ISK;
        private String JPY;
        private String KRW;
        private String MXN;
        private String MYR;
        private String NOK;
        private String NZD;
        private String PHP;
        private String PLN;
        private String RON;
        private String RUB;
        private String SEK;
        private String SGD;
        private String THB;
        private String TRY;
        private String USD;
        private String ZAR;
    }
}
