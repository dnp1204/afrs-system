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

    public String getUUID() {
        return UUID;
    }

    private String request;
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

    public void processRequest() {
        request = input.getText().substring(0, input.getText().length() - 1);
        input.setText("");
        response = new ArrayList<>();
        response.add("> "+request);
        pushUpdate();
        if (request.length() == 0 || !request.substring(request.length() - 1).equals(";")) {
            response = new ArrayList<>();
            response.add("partial-request");
            pushUpdate();
        } else {
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

    public void pushUpdate() {
        observer.update();
    }

    public ArrayList<String> getState() {
        return response;
    }
}
