package Frame;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

//動画再生パネルクラス
class MoviePanel extends JFXPanel {
    private static MediaController mc;
    private final Media media;
    private final MediaPlayer player;
    private final Slider slider;
    private int sliderTime;

    MoviePanel(String filePath) {
        // 動画ファイルのパスを取得
        File f = new File(filePath);
        // 動画再生クラスをインスタンス化
        media = new Media(f.toURI().toString());
        player = new MediaPlayer(media);

        MediaView mediaView = new MediaView(player);


        //JavaFXルートパネル
        BorderPane root = new BorderPane();
        Pane mpane = new Pane();

        // コントローラーを呼び出し
        mc = new MediaController(player);
        mc.setFocusTraversable(false);
        int totalTime = (int) player.getTotalDuration().toSeconds();
        slider = new Slider(0, totalTime, 0);
        slider.setBlockIncrement(10);

        mpane.getChildren().add(mediaView);
        root.setCenter(mpane);
        root.setBottom(mc);

        int rawTime = (int) media.getDuration().toSeconds();
        int second = rawTime % 60;
        int minute = ((rawTime % 3600) / 60);
        int hour = rawTime / 3600;

        //HH:mm:ss形式で時間を取得
        String time = String.format("%02d:%02d:%02d",
                hour, minute, second);
        System.out.println(time);
        //JavaFXScene
        Scene scene = new Scene(root);
        //JFXPanelにSceneをセット
        setScene(scene);

    }

    int getSliderTime() {
        this.sliderTime = (int) slider.getValue();
        return this.sliderTime;
    }

    Media getMedia() {
        return media;
    }

    MediaPlayer getPlayer() {
        return player;
    }

    Slider getSlider() {
        return slider;
    }
}
