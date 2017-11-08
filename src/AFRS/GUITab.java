package AFRS;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

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

        setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                rc.parse(sender.getUUID()+",disconnect;");
            }
        });
    }
}
