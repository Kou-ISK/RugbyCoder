package Frame;

import DataObject.DataObject;
import DataObject.timeObject;
import Logic.Logic;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class Frame {
    public static void main(String[] args) throws Exception {
        String mediaName = "mediaName";
        String filePath = "/Users/isakakou/Desktop/ドラゴンクエストⅤ 序曲のマーチ.mp4";
        String fileName;
//      Creating Media Player Window
        window videoWindow = new window("Rugby Coder", 700, 450);
        //Input Video File Path
        MoviePanel mp = new MoviePanel(filePath);

        Logic logic = new Logic();
        logic.setName(mediaName);
        csvViewer csvViewer = new csvViewer(mediaName);
        fileName = csvViewer.getFileName();
        //JavaFX動画インスタンスとプレイヤーを取得
        Media media = mp.getMedia();
        MediaPlayer player = mp.getPlayer();

        codeWindow cWindow = new codeWindow(logic, csvViewer, "Code Window", player, 500, 800);
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
        Slider s = mp.getSlider();
        timeObject to = new timeObject();
        s.valueProperty().addListener((
                ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) -> {
            to.setNow(new_val.intValue());
            System.out.println(to.getNow());
            int now = to.getNow();
            if (isNull(now)) {
                player.seek(logic.getDataFromCsv("00:00:00"));
            } else {
                int rawTime = to.getNow();
                int second = rawTime % 60;
                int minute = ((rawTime % 3600) / 60);
                int hour = rawTime / 3600;
                String time = String.format("%02d:%02d:%02d",
                        hour, minute, second);
                player.seek(logic.getDataFromCsv(time));

            }
        });


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
    private csvViewer csvViewer;

    codeWindow(Logic logic, csvViewer csvViewer, String title, MediaPlayer player, int x, int y) {
        setTitle(title);
        setSize(x, y);
        Container cwContainer = this.getContentPane();
//            停止ボタン
        button tackleButton = new button("Tackle", 400, 200);
        tackleButton.addActionListener(a -> {
            DataObject dto = new DataObject(logic.getTimeStamp(player.getCurrentTime()), "Tackle");
            logic.csvWriter(dto);
            csvViewer.readIn();
        });
        cwContainer.add(tackleButton);
        button scrumButton = new button("Scrum", 400, 200);
        scrumButton.addActionListener(a ->
        {
            DataObject dto = new DataObject(logic.getTimeStamp(player.getCurrentTime()), "Scrum");
            logic.csvWriter(dto);
            csvViewer.readIn();
        });
        cwContainer.add(scrumButton);
        button lineOutButton = new button("Lineout", 400, 200);
        lineOutButton.addActionListener(a ->
        {
            DataObject dto = new DataObject(logic.getTimeStamp(player.getCurrentTime()), "Lineout");
            logic.csvWriter(dto);
            csvViewer.readIn();
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
    private int sliderTime;
    private Slider slider;

    int getSliderTime() {
        this.sliderTime = (int) slider.getValue();
        return this.sliderTime;
    }

    MoviePanel(String filePath) {

        //JavaFXルートパネル
        StackPane root = new StackPane();

        // 動画ファイルのパスを取得
        File f = new File(filePath);

        // 動画再生クラスをインスタンス化
        media = new Media(f.toURI().toString());
        player = new MediaPlayer(media);
        MediaView mediaView = new MediaView(player);
        root.getChildren().add(0, mediaView);
        int totalTime = (int) player.getTotalDuration().toSeconds();
        slider = new Slider(0, totalTime, 0);
        slider.increment();
        int sliderTime = (int) slider.getValue();
        root.getChildren().add(1, slider);
        root.setAlignment(slider, Pos.BOTTOM_CENTER);


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

    Slider getSlider() {
        return slider;
    }
}

class csvViewer extends JFrame {
    private final String fileName;
    private final JTable table = new JTable();
    private final JScrollPane jScrollPane = new JScrollPane(table);


    csvViewer(String filename) throws IOException {
        super(filename + " - CSVViewer");
        fileName = "/Users/isakakou/Desktop/" + filename + ".csv";

        readIn();
        getContentPane().add(jScrollPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        show();
    }

    String getFileName() {
        return fileName;
    }

    void readIn() {
        Pattern pattern = Pattern.compile(",");
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            for (int row = 0; (line = reader.readLine()) != null; row++) {
                String[] items = pattern.split(line);
                model.setRowCount(row + 1);
                for (int column = 0; column < items.length; column++) {
                    if (model.getColumnCount() <= column) {
                        model.setColumnCount(column + 1);
                    }
                    table.setValueAt(items[column], row, column);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}