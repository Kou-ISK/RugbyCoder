package Frame;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Frame{
    public static void main(String[] args) throws Exception{
//        ビデオ再生用ウィンドウ
        window videoWindow = new window("Rugby Coder", 700,450);
        codeWindow cWindow = new codeWindow("Code Window",800,500);
        //ムービー再生パネル
        //引数にはファイルのパスを指定してください。
        MoviePanel mp = new MoviePanel("");

        //JavaFX動画インスタンスとプレイヤーを取得
        Media media = mp.getMedia();
        MediaPlayer player = mp.getPlayer();

        //読み込み待ち
        for(int i = 0;player.getStatus() != MediaPlayer.Status.READY;i++) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
            if ( i > 20 ) throw new Exception("動画の読み込みに時間がかかりすぎたため中断しました。");
        }

        //読み込み完了後なら動画サイズを取得できる
        int videoW = media.getWidth();
        int videoH = media.getHeight();

        //MoviePanelのサイズを動画に合わせてJFrameに追加
        mp.setPreferredSize(new Dimension(videoW,videoH));
        videoWindow.add(mp);

        //JFrame側のパネルサイズを動画に合わせる
        videoWindow.getContentPane().setPreferredSize(new Dimension(videoW,videoH));

        //JFrameサイズをパネル全体が見えるサイズに自動調整
        videoWindow.pack();

        //中心に表示
        videoWindow.setLocationRelativeTo(null);

        videoWindow.setVisible(true);
        cWindow.setVisible(true);
cWindow.setLocation(750,0);
        //動画の再生
        player.play();
    }
}

class window extends JFrame {
        window(String title, int x, int y) {
            setTitle(title);
            setSize(x,y);
        }
    }

class codeWindow extends JFrame{
        codeWindow(String title, int x, int y) {
            setTitle(title);
            setSize(x,y);
            Container cwContainer = this.getContentPane();
            button startButton = new button("Start",400,100);
            cwContainer.add(startButton);
            button endButton = new button("End",400,100);
            cwContainer.add(endButton);
            button tackleButton = new button("Tackle",400,100);
            cwContainer.add(tackleButton);
            button scrumButton = new button("Scrum",400,100);
            cwContainer.add(scrumButton);
            button lineOutButton = new button("Lineout",400,100);
            cwContainer.add(lineOutButton);
            setLayout(new FlowLayout());
        }
    }

class button extends JButton {
        button(String title, int x, int y) {
        setText(title);
        setSize(x,y);
        }
    }

//動画再生パネルクラス
class MoviePanel extends JFXPanel {
    Media media;
    MediaPlayer player;
    MoviePanel(String filePath){

        //JavaFXルートパネル
        StackPane root = new StackPane();

        // 動画ファイルのパスを取得
        File f = new File( filePath );

        // 動画再生クラスをインスタンス化
        media = new Media( f.toURI().toString() );
        player = new MediaPlayer(media);
        MediaView mediaView = new MediaView(player);
        root.getChildren().add(mediaView);

        //JavaFXScene
        Scene scene = new Scene( root );

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