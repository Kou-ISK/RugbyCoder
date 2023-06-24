package Frame;

import com.sun.java.accessibility.util.AWTEventMonitor;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;

import static Frame.MakeFrames.kl;

class mediaController extends HBox {
    // introducing Sliders
    private Slider slider = new Slider(); // Slider for time

    private static Button PlayButton = new Button("||"); // For pausing the mp
    private static Slider volumeSlider = new Slider(0, 100, 100);
    private MediaPlayer player;
    private MediaPlayer player2;

    mediaController(MediaPlayer player, MediaPlayer player2) {
        // Default constructor taking
        // the MediaPlayer object
        this.player = player;
        this.player2 = player2;

        setAlignment(Pos.CENTER); // setting the HBox to center
        setPadding(new Insets(5, 10, 5, 10));
        HBox.setHgrow(slider, Priority.ALWAYS);
        PlayButton.setPrefWidth(30);

        AWTEventMonitor.addKeyListener(kl);
        // Adding the components to the bottom

        getChildren().add(PlayButton); // Playbutton
        getChildren().add(volumeSlider);
        getChildren().add(slider); // time slider
        // Adding Functionality
        // to play the media mp

        //動画の再生
        // 指定した時間へとジャンプ
        // Inorder to jump to the certain part of video
        slider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (slider.isPressed()) { // It would set the time
                    // as specified by user by pressing
                    player.seek(player.getMedia().getDuration().multiply(slider.getValue() / 100));
                    player2.seek(player2.getMedia().getDuration().multiply(slider.getValue() / 100));
                }
            }
        });
        StackPane trackPane = (StackPane) slider.lookup(".track");
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (volumeSlider.isPressed()) {
                    player.setVolume(volumeSlider.getValue() / 100.0);
                    player2.setVolume(volumeSlider.getValue() / 100.0);
                }
            }
        });
        // Providing functionality to time slider
        player.currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                updatesValues();
            }
        });

        // Adding Functionality
        // to play the media player
        PlayButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                MediaPlayer.Status status = player.getStatus(); // To get the status of Player
                MediaPlayer.Status status2 = player2.getStatus();
                if (status == status.PLAYING) {

                    // If the status is Video playing
                    if (player.getCurrentTime().greaterThanOrEqualTo(player.getTotalDuration())) {

                        // If the player is at the end of video
                        player.seek(player.getStartTime()); // Restart the video
                        player.play();
                        player.setRate(1.0);
                        player2.seek(player2.getStartTime()); // Restart the video
                        player2.play();
                        player2.setRate(1.0);
                    } else {
                        // Pausing the player
                        player.pause();
                        player.setRate(1.0);
                        player2.pause();
                        player2.setRate(1.0);
                        PlayButton.setText(">");
                    }
                } // If the video is stopped, halted or paused
                if (status == MediaPlayer.Status.HALTED || status == MediaPlayer.Status.STOPPED || status == MediaPlayer.Status.PAUSED) {
                    player.play(); // Start the video
                    player.setRate(1.0);
                    player2.play(); // Start the video
                    player2.setRate(1.0);
                    PlayButton.setText("||");
                }
            }
        });
    }

    // Outside the constructor
    private void updatesValues() {
        Platform.runLater(new Runnable() {
            public void run() {
                // Updating to the new time value
                // This will move the slider while running your video
                slider.setValue(player.getCurrentTime().toMillis()
                        / player.getTotalDuration()
                        .toMillis()
                        * 100);
            }
        });
    }
}


