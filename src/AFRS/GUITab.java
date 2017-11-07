package AFRS;

import AFRS.RequestSender;
import AFRS.ResponseReceiver;

import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class GUITab extends Tab {
    public GUITab(String text, RequestController rc) {
        setText(text);
        BorderPane pane = new BorderPane();
        RequestSender sender = new RequestSender(rc);
        ResponseReceiver receiver = new ResponseReceiver(sender);
        sender.attach(receiver);

        pane.setTop(receiver);
        pane.setBottom(sender);

        setContent(pane);
    }
}
