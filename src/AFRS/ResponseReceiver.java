package AFRS;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ResponseReceiver extends ScrollPane {
    private TextArea output;
    private RequestSender subject;

    public ResponseReceiver(RequestSender subject) {
        this.subject = subject;
        setFitToWidth(true);
        output = new TextArea();
        output.setWrapText(true);
        output.setPrefColumnCount(40);
        output.setPrefRowCount(20);
        output.setFocusTraversable(false);
        output.setEditable(false);
        setContent(output);
    }

    public void update() {
        output.appendText(subject.getState());
    }
}
