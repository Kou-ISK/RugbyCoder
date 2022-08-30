package Frame;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MediaPlayer {
    @Override
    public void start(Stage primaryStage) {
        BorderPane p = new BorderPane();
        Scene scene = new Scene(p, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
}
