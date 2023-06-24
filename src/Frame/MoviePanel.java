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
    private final Media media;
    private final MediaPlayer player;
    private final Media media2;
    private final MediaPlayer player2;
    private int sliderTime;
    private final Slider slider;
    private static mediaController mc;

    MoviePanel(String filePath, String filePath2) {
        // 動画ファイルのパスを取得
        File f = new File(filePath);
        File f2 = new File(filePath2);
        // 動画再生クラスをインスタンス化
        media = new Media(f.toURI().toString());
        media2 = new Media(f2.toURI().toString());
        player = new MediaPlayer(media);
        player2 = new MediaPlayer(media2);

        MediaView mediaView = new MediaView(player);
        MediaView mediaView2 = new MediaView(player2);


        //JavaFXルートパネル
        BorderPane root = new BorderPane();
        Pane tight = new Pane();
        Pane wide = new Pane();

        // コントローラーを呼び出し
        mc = new mediaController(player, player2);
        mc.setFocusTraversable(false);
        int totalTime = (int) player.getTotalDuration().toSeconds();
        slider = new Slider(0, totalTime, 0);
        slider.setBlockIncrement(10);
        
        tight.getChildren().add(mediaView);
        tight.resize(root.getWidth() * 0.25, root.getHeight() * 0.25);
        wide.getChildren().add(mediaView2);
        wide.resize(root.getWidth() * 0.25, root.getHeight() * 0.25);
        root.setLeft(tight);
        root.setRight(wide);
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

    Media getMedia2() {
        return media2;
    }

    MediaPlayer getPlayer() {
        return player;
    }

    MediaPlayer getPlayer2() {
        return player2;
    }

    Slider getSlider() {
        return slider;
    }
}
