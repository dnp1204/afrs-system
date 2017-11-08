package AFRS;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class RequestGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        RequestController rc = new RequestController();

        final AnchorPane root = new AnchorPane();
        final TabPane tabs = new TabPane();
        final Button addButton = new Button("+");

        AnchorPane.setTopAnchor(tabs, 5.0);
        AnchorPane.setLeftAnchor(tabs, 5.0);
        AnchorPane.setRightAnchor(tabs, 5.0);
        AnchorPane.setTopAnchor(addButton, 10.0);
        AnchorPane.setLeftAnchor(addButton, 10.0);

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Tab tab = new GUITab("Client "+(tabs.getTabs().size() + 1), rc);
                tabs.getTabs().add(tab);
                tabs.getSelectionModel().select(tab);
            }
        });

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    rc.getReservationDB().shutDown();
                } catch (Exception e) {
                    System.err.println("Unable to write reservations to file");
                }
            }
        });

        root.getChildren().addAll(tabs, addButton);

        final Scene scene = new Scene(root, 800, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
