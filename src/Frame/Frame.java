package Frame;

import DataObject.DataObject;
import Logic.Logic;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Frame {
    private Logic logic;

    public static void main(String[] args) throws Exception {
//      Creating Media Player Window
        window videoWindow = new window("Rugby Coder", 700, 450);
        //Input Video File Path
        MoviePanel mp = new MoviePanel("/Users/isakakou/Desktop/MAH00202.MP4");
        Logic logic = new Logic();
        logic.setName("mediaName");
        //JavaFX動画インスタンスとプレイヤーを取得
        Media media = mp.getMedia();
        MediaPlayer player = mp.getPlayer();

        codeWindow cWindow = new codeWindow(logic, "Code Window", player, 500, 800);
        //Loading
        for (int i = 0; player.getStatus() != MediaPlayer.Status.READY; i++) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
            if (i > 20) throw new Exception("動画の読み込みに時間がかかりすぎたため中断しました。");
        }

        //読み込み完了後なら動画サイズを取得できる
        int videoW = media.getWidth() / 3 * 2;
        int videoH = media.getHeight() / 3 * 2;

        //MoviePanelのサイズを動画に合わせてJFrameに追加
        mp.setPreferredSize(new Dimension(videoW, videoH));
        videoWindow.add(mp);

        //JFrame側のパネルサイズを動画に合わせる
        videoWindow.getContentPane().setPreferredSize(new Dimension(videoW, videoH + 100));

        //JFrameサイズをパネル全体が見えるサイズに自動調整
        videoWindow.pack();

        //中心に表示
        videoWindow.setLocationRelativeTo(null);

        //再生ボタン
        button startButton = new button("Start", 1000, 400);
        startButton.addActionListener(a -> {
            player.play();
        });
        videoWindow.add(startButton);
        button pauseButton = new button("Pause", 1000, 400);
        pauseButton.addActionListener(a ->
                player.pause());
        videoWindow.add(pauseButton);


        videoWindow.setLayout(new FlowLayout());
        videoWindow.setVisible(true);
        cWindow.setVisible(true);
        videoWindow.setLocation(0, 0);
        cWindow.setLocation(900, 0);

        //動画の再生
//指定した時間へとジャンプ
        player.seek(logic.getDataFromCsv("00:01:00"));
        player.play();
        System.out.println("Current: " + player.getCurrentTime());
        System.out.println(player.getStopTime());
    }
}


class window extends JFrame {
    window(String title, int x, int y) {
        setTitle(title);
        setSize(x, y);
    }
}

class codeWindow extends JFrame {
    codeWindow(Logic logic, String title, MediaPlayer player, int x, int y) {
        setTitle(title);
        setSize(x, y);
        Container cwContainer = this.getContentPane();
//            停止ボタン
        button tackleButton = new button("Tackle", 400, 200);
        tackleButton.addActionListener(a -> {
            DataObject dto = new DataObject(logic.getTimeStamp(player.getCurrentTime()), "Tackle");
            logic.csvWriter(dto);
        });
        cwContainer.add(tackleButton);
        button scrumButton = new button("Scrum", 400, 200);
        scrumButton.addActionListener(a ->
        {
            DataObject dto = new DataObject(logic.getTimeStamp(player.getCurrentTime()), "Scrum");
            logic.csvWriter(dto);
        });
        cwContainer.add(scrumButton);
        button lineOutButton = new button("Lineout", 400, 200);
        lineOutButton.addActionListener(a ->
        {
            DataObject dto = new DataObject(logic.getTimeStamp(player.getCurrentTime()), "Lineout");
            logic.csvWriter(dto);
        });
        cwContainer.add(lineOutButton);
        setLayout(new FlowLayout());
    }
}

class button extends JButton {
    button(String title, int x, int y) {
        setText(title);
        setSize(x, y);
    }
}

//動画再生パネルクラス
class MoviePanel extends JFXPanel {
    private Media media;
    private MediaPlayer player;


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

    Media getMedia() {
        return media;
    }

    MediaPlayer getPlayer() {
        return player;
    }


}
