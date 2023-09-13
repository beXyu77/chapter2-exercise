package com.example.chapter2.view;

import com.example.chapter2.controller.AllEventHandlers;
import com.example.chapter2.controller.draw.DrawGraphTask;
import com.example.chapter2.model.Currency;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;

import java.util.Optional;
import java.util.concurrent.*;

public class CurrencyPane extends BorderPane {
    private Currency currency;
    private Button watch;
    private Button unwatch; // 新增Unwatch按钮
    private Button delete;

    public CurrencyPane(Currency currency) {
        this.watch = new Button("Watch");
        this.unwatch = new Button("Unwatch"); // 初始化Unwatch按钮
        this.delete = new Button("Delete");

        this.watch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandlers.onWatch(currency.getShortCode());
            }
        });

        this.unwatch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                unwatchCurrency(); // 处理取消监视的逻辑
            }
        });

        this.delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandlers.onDelete(currency.getShortCode());
            }
        });

        this.setPadding(new Insets(0));
        this.setPrefSize(640, 300);
        this.setStyle("-fx-border-color: black");
        try {
            this.refreshPane(currency);
        } catch (ExecutionException e) {
            System.out.println("Encountered an execution exception.");
        } catch (InterruptedException e) {
            System.out.println("Encountered an interrupted exception.");
        }
    }

    public void refreshPane(Currency currency) throws ExecutionException, InterruptedException {
        this.currency = currency;
        Pane currencyInfo = genInfoPane();


        //

        // 创建 Callable 对象
        Callable<VBox> drawGraphCallable = new DrawGraphTask(currency);

        // 创建 ExecutorService
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // 提交任务给 ExecutorService 并获取 Future 对象
        Future<VBox> future = executorService.submit(drawGraphCallable);

        // 获取结果
        VBox currencyGraph = future.get();

        // 关闭 ExecutorService
        executorService.shutdown();

        Pane topArea = genTopArea();
        this.setTop(topArea);
        this.setLeft(currencyInfo);
        this.setCenter(currencyGraph);
    }

    private Pane genInfoPane() {
        VBox currencyInfoPane = new VBox(10);
        currencyInfoPane.setPadding(new Insets(5, 25, 5, 25));
        currencyInfoPane.setAlignment(Pos.CENTER);
        Label exchangeString = new Label("");
        Label watchString = new Label("");
        exchangeString.setStyle("-fx-font-size: 20;");
        watchString.setStyle("-fx-font-size: 14;");
        if (this.currency != null) {
            exchangeString.setText(String.format("%s: %.4f", this.currency.getShortCode(), this.currency.getCurrent().getRate()));
            if (this.currency.getWatch()) {
                watchString.setText(String.format("(Watch @%.4f)", this.currency.getWatchRate()));
            }
        }
        currencyInfoPane.getChildren().addAll(exchangeString, watchString);
        return currencyInfoPane;
    }

    private HBox genTopArea() {
        HBox topArea = new HBox(10);
        topArea.setPadding(new Insets(5));
        topArea.getChildren().addAll(watch, unwatch, delete); // 添加Unwatch按钮
        topArea.setAlignment(Pos.CENTER_RIGHT);
        return topArea;
    }

    private void unwatchCurrency() {
        // 处理取消监视的逻辑
        if (this.currency != null) {
            this.currency.setWatch(false);
            this.currency.setWatchRate(0.0); // 或者将watch rate设置为其他合适的值
            try {
                refreshPane(currency); // 刷新面板以反映更改
            } catch (ExecutionException e) {
                System.out.println("Encountered an execution exception.");
            } catch (InterruptedException e) {
                System.out.println("Encountered an interrupted exception.");
            }
        }
    }
}

