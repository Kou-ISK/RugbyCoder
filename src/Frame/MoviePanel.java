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
    private Pane mediaPane;
    private BorderPane rootPane;
    private MediaView mediaView;

    MoviePanel(String filePath) {
        // 動画ファイルのパスを取得
        File f = new File(filePath);
        // 動画再生クラスをインスタンス化
        media = new Media(f.toURI().toString());
        player = new MediaPlayer(media);

        mediaView = new MediaView(player);

        //JavaFXルートパネル
        rootPane = new BorderPane();
        mediaPane = new Pane();
        rootPane.autosize();

        // コントローラーを呼び出し
        mc = new MediaController(player);
        mc.setFocusTraversable(false);
        int totalTime = (int) player.getTotalDuration().toSeconds();
        slider = new Slider(0, totalTime, 0);
        slider.setBlockIncrement(10);

        mediaPane.getChildren().add(mediaView);
        rootPane.setCenter(mediaPane);
        rootPane.setBottom(mc);

        int rawTime = (int) media.getDuration().toSeconds();
        int second = rawTime % 60;
        int minute = ((rawTime % 3600) / 60);
        int hour = rawTime / 3600;

        //HH:mm:ss形式で時間を取得
        String time = String.format("%02d:%02d:%02d",
                hour, minute, second);
        System.out.println(time);
        //JavaFXScene
        Scene scene = new Scene(rootPane);
        //JFXPanelにSceneをセット
        setScene(scene);
    }

    Media getMedia() {
        return media;
    }

    MediaPlayer getPlayer() {
        return player;
    }

    MediaView getMediaView() {
        return mediaView;
    }
}
