package Frame;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
//importが上手くいかないのは要チェック
public class MoviePanel extends JFXPanel {
    Media media;
    MediaPlayer player;

    MoviePanel(String filePath) {

        //JavaFXルートパネル
        StackPane root = new StackPane();

        // 動画ファイルのパスを取得
        File f = new File(filePath);

        // 動画再生クラスをインスタンス化
        media = new Media(f.toURI().toString());
        player = new MediaPlayer(media);
        MediaView mediaView = new MediaView(player);
        root.getChildren().add(mediaView);

        //JavaFXScene
        Scene scene = new Scene(root);

        //JFXPanelにSceneをセット
        setScene(scene);
    }

    public Media getMedia() {
        return media;
    }

    public MediaPlayer getPlayer() {
        return player;
    }
}