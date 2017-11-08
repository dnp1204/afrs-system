package AFRS;

import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;

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
        for (String s:
             subject.getState()) {
            output.appendText(s+"\n");
            if(s.equals("quit")) {
                System.exit(0);
            }
        }
    }
}
