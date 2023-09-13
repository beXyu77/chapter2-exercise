package com.example.chapter2.controller;

import com.example.chapter2.model.Currency;
import com.example.chapter2.model.CurrencyEntity;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.Optional;

public class Initialize {
     public static ArrayList<Currency> initialize_app() {
          TextInputDialog dialog = new TextInputDialog("USD"); // Set default base currency to USD
          dialog.setTitle("Base Currency");
          dialog.setHeaderText("Enter the base currency code:");
          dialog.setContentText("Base Currency:");
          Optional<String> result = dialog.showAndWait();
          String baseCurrency = result.orElse("USD"); // If the user cancels, use USD as default

          Currency c = new Currency(baseCurrency);
          ArrayList<CurrencyEntity> c_list = FetchData.fetch_range(baseCurrency, c.getShortCode(), 8);
          c.setHistorical(c_list);
          c.setCurrent(c_list.get(c_list.size() - 1));
          ArrayList<Currency> currencyList = new ArrayList<>();
          currencyList.add(c);
          return currencyList;
     }
}
