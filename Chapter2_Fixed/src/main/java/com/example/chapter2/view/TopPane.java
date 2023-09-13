package com.example.chapter2.view;

import com.example.chapter2.Launcher;
import com.example.chapter2.controller.AllEventHandlers;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import org.json.JSONException;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

public class TopPane extends FlowPane {
    private Button refresh;
    private Button add;
    private Label update;
    private TextField baseCurrencyTextField; // 添加文本输入框

    public TopPane() {
        this.setPadding(new Insets(10));
        this.setHgap(10);
        this.setPrefSize(640, 20);

        add = new Button("Add");
        refresh = new Button("Refresh");

        refresh.setOnAction(event -> {
            AllEventHandlers.onRefresh();
        });

        add.setOnAction(event -> {
            try {
                AllEventHandlers.onAdd();
            }
            catch (JSONException e) {
                System.out.println("Invalid currency short code. Please enter a valid one.");
                try {
                    AllEventHandlers.addError();
                } catch (ExecutionException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        update = new Label();
        baseCurrencyTextField = new TextField(); // 初始化文本输入框

        // 监听文本输入框的回车事件，当用户按下回车时更新基础货币
        baseCurrencyTextField.setOnAction(event -> {
            String newBaseCurrency = baseCurrencyTextField.getText().trim().toUpperCase();
            if (!newBaseCurrency.isEmpty()) {
                Launcher.setBaseCurrency(newBaseCurrency); // 更新基础货币
                AllEventHandlers.onChangeBaseCurrency(newBaseCurrency);
            }
            baseCurrencyTextField.clear();
            refreshPane(); // 更新顶部面板以显示新的基础货币
        });

        refreshPane();
        this.getChildren().addAll(refresh, add, update, new Label("Set Base Currency:"), baseCurrencyTextField); // 添加文本输入框
    }

    public void refreshPane() {
        update.setText(String.format("Last update: %s | Base Currency: %s", LocalDateTime.now().toString(), Launcher.getBaseCurrency()));
    }
}