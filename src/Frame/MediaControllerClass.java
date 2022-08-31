package Frame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;

public class MediaControllerClass extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        BorderPane p = new BorderPane();
        Scene scene = new Scene(p, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        //ファイルを読み込み
        Media m = new Media(new File("/Users/isakakou/Desktop/sample2.mp3").toURI().toString());

        //動画の再生等の操作を実行できるオブジェクト
        MediaPlayer mp = new MediaPlayer(m);

        //動画パネルの挿入
        MediaView view = new MediaView(mp);
        p.setCenter(view);

        //再生速度変更
        mp.setRate(2);

        //自動再生開始命令
        mp.setAutoPlay(true);
    }

}