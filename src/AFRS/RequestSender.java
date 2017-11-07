package AFRS;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RequestSender extends BorderPane {
    private ResponseReceiver observer;
    private RequestController parser;
    private TextArea input;
    private String lastKeyTyped;

    public RequestSender(RequestController parser) {
        this.parser = parser;
        input = new TextArea();
        input.setPrefColumnCount(40);
        input.setPrefRowCount(1);
        input.setWrapText(false);
        setCenter(input);

        input.setOnKeyTyped(e -> {
            lastKeyTyped = e.getCharacter();
            sendKey();
        });

        input.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) {
                System.out.println("focus changed: " + oldValue + ", " +
                        newValue);
                if (!newValue) {
                    input.requestFocus();
                }
            }
        });
    }

    public void attach(ResponseReceiver observer) {
        this.observer = observer;
    }

    public void sendKey() {
        observer.update();
    }

    public String getState() {
        return lastKeyTyped;
    }
}
