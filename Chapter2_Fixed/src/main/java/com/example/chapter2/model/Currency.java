package com.example.chapter2.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Currency extends ArrayList<Currency> {
    private String shortCode;
    private CurrencyEntity current;
    private ArrayList<CurrencyEntity> historical;
    private Boolean isWatch;
    private Double watchRate;

    // API返回的有效货币列表
    private static final List<String> VALID_CURRENCIES = Arrays.asList(
            "USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD"
            // 在此添加更多有效货币缩写...
    );

    public Currency(String shortCode) {
        this.shortCode = shortCode.toUpperCase(); // 统一将缩写转换为大写
        this.isWatch = false;
        this.watchRate = 0.0;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode.toUpperCase(); // 统一将缩写转换为大写
    }

    public CurrencyEntity getCurrent() {
        return current;
    }

    public void setCurrent(CurrencyEntity current) {
        this.current = current;
    }

    public ArrayList<CurrencyEntity> getHistorical() {
        return historical;
    }

    public void setHistorical(ArrayList<CurrencyEntity> historical) {
        this.historical = historical;
    }

    public Boolean getWatch() {
        return isWatch;
    }

    public void setWatch(Boolean watch) {
        isWatch = watch;
    }

    public Double getWatchRate() {
        return watchRate;
    }

    public void setWatchRate(Double watchRate) {
        if (watchRate <= 0) {
            this.watchRate = 0.0;
        } else {
            this.watchRate = watchRate;
        }
    }

    public void unwatch() {
        this.isWatch = false;
        this.watchRate = 0.0;
    }

    // 检查输入的货币缩写是否有效
    public boolean isValidCurrency() {
        return VALID_CURRENCIES.contains(shortCode);
    }
}
