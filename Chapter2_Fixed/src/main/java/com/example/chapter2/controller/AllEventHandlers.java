package com.example.chapter2.controller;

import com.example.chapter2.Launcher;
import com.example.chapter2.model.Currency;
import com.example.chapter2.model.CurrencyEntity;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AllEventHandlers {
    public static void onRefresh() {
         try {
             Launcher.refreshPane();
             } catch (Exception e) {
             e.printStackTrace();
             }
         }
    public static void onAdd() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Currency");
            dialog.setContentText("Currency code:");
            dialog.setHeaderText(null);
            dialog.setGraphic(null);
            Optional<String> code = dialog.showAndWait();
            if (code.isPresent()) {
                ArrayList<Currency> currency_list = Launcher.getCurrencyList();
                Currency c = new Currency(code.get());
                ArrayList<CurrencyEntity> c_list = FetchData.fetch_range(Launcher.getBaseCurrency(),c.getShortCode() ,8);
                c.setHistorical(c_list);
                c.setCurrent(c_list.get(c_list.size() - 1));
                currency_list.add(c);
                Launcher.setCurrencyList(currency_list);
                Launcher.refreshPane();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Exercise 5
    public static void addError() throws ExecutionException, InterruptedException {
        try{
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Invalid Currency");
        dialog.setHeaderText("Invalid currency short code");
        dialog.setContentText("Please enter a valid currency short code:");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        Optional<String> code = dialog.showAndWait();
        if (code.isPresent()) {
            ArrayList<Currency> currency_list = Launcher.getCurrencyList();
            Currency c = new Currency(code.get());
            ArrayList<CurrencyEntity> c_list = FetchData.fetch_range(Launcher.getBaseCurrency(),c.getShortCode() ,8);
            c.setHistorical(c_list);
            c.setCurrent(c_list.get(c_list.size() - 1));
            currency_list.add(c);
            Launcher.setCurrencyList(currency_list);
            Launcher.refreshPane();
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (ExecutionException e) {
        e.printStackTrace();
    }
    }
    public static void onDelete(String code) {
         try {
             ArrayList<Currency> currency_list = Launcher.getCurrencyList();
             int index = -1;
             for(int i=0 ; i<currency_list.size() ; i++) {
                 if (currency_list.get(i).getShortCode().equals(code) ) {
                   index = i;
                   break;
                   }
                }
         if (index != -1) {
            currency_list.remove(index);
               Launcher.setCurrencyList(currency_list);
                Launcher.refreshPane();
                 }
             } catch (InterruptedException e) {
             e.printStackTrace();
             } catch (ExecutionException e) {
             e.printStackTrace();
             }
        }
    public static void onWatch(String code) {
         try {
             ArrayList<Currency> currency_list = Launcher.getCurrencyList();
             int index = -1;
             for(int i=0 ; i<currency_list.size() ; i++) {
                 if (currency_list.get(i).getShortCode().equals(code) ) {
                     index = i;
                     break;
                     }
                 }
             if (index != -1) {
                 TextInputDialog dialog = new TextInputDialog();
                 dialog.setTitle("Add Watch");
                 dialog.setContentText("Rate:");
                 dialog.setHeaderText(null);
                 dialog.setGraphic(null);
                 Optional<String> retrievedRate = dialog.showAndWait();
             if (retrievedRate.isPresent()){
             double rate = Double.parseDouble(retrievedRate.get());
                  currency_list.get(index).setWatch(true);
                    currency_list.get(index).setWatchRate(rate);
                    Launcher.setCurrencyList(currency_list);
                     Launcher.refreshPane();
                    }
                 Launcher.setCurrencyList(currency_list);
                Launcher.refreshPane();
                }
            } catch (InterruptedException e) {
            e.printStackTrace();
            } catch (ExecutionException e) {
            e.printStackTrace();
             }
         }
    public static void onChangeBaseCurrency(String newBaseCurrency) {
        try {
            ArrayList<Currency> currencyList = Launcher.getCurrencyList();
            ArrayList<CurrencyEntity> baseCurrencyList = FetchData.fetch_range(newBaseCurrency, newBaseCurrency, 8);

            for (Currency currency : currencyList) {
                ArrayList<CurrencyEntity> cList = FetchData.fetch_range(newBaseCurrency, currency.getShortCode(), 8);
                currency.setHistorical(cList);
                currency.setCurrent(cList.get(cList.size() - 1));
            }

            // 设置新的基础货币的历史汇率信息
            currencyList.get(0).setHistorical(baseCurrencyList);
            currencyList.get(0).setCurrent(baseCurrencyList.get(baseCurrencyList.size() - 1));

            Launcher.setCurrencyList(currencyList);
            Launcher.refreshPane();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
