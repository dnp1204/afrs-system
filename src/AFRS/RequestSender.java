package AFRS;

import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;

public class RequestSender extends BorderPane {
    private ResponseReceiver observer;
    private RequestController parser;
    private TextArea input;
    private String UUID;
    private ArrayList<String> response;

    public RequestSender(RequestController parser) {
        this.parser = parser;
        input = new TextArea();
        input.setPrefColumnCount(40);
        input.setPrefRowCount(1);
        input.setWrapText(false);
        setCenter(input);

        response = parser.parse("connect;");
        UUID = response.get(0).split(",")[1];

        input.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                processRequest();
            }
        });
    }

    private void processRequest() {
        String request = input.getText().substring(0, input.getText().length() - 1);
        response = new ArrayList<>();
        response.add("> "+request);
        pushUpdate();
        if (request.length() == 0 || !request.substring(request.length() - 1).equals(";")) {
            response = new ArrayList<>();
            response.add("partial-request");
            input.setText(request);
            pushUpdate();
        } else {
            input.setText("");
            response = parser.parse(UUID + "," + request);
            if(response.get(0).contains(",")) {
                String firstResponse = response.get(0).substring(response.get(0).indexOf(",") + 1);
                response.set(0, firstResponse);
            }
            pushUpdate();
        }
    }

    public void attach(ResponseReceiver observer) {
        this.observer = observer;
    }

    private void pushUpdate() {
        observer.update();
    }

    public ArrayList<String> getState() {
        return response;
    }

    public String getUUID() {
        return UUID;
    }
}
