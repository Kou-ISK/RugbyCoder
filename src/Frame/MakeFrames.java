package Frame;

import DataObject.DataObject;
import DataObject.timeObject;
import Logic.Logic;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.regex.Pattern;

import static Frame.MakeFrames.directoryPath;

class MakeFrames {
    private String filePath;
    private String mediaName;
    static String directoryPath;

    void makeFrames(String directoryPath, String filePath, String mediaName) throws Exception {
        this.filePath = filePath;
        this.mediaName = mediaName;
        this.directoryPath = directoryPath;
        Logic logic = new Logic();
        String fileName;
//      Creating Media Player Window
        window videoWindow = new window("Rugby Coder", 700, 450);
        //Input Video File Path
        MoviePanel mp = new MoviePanel(filePath);

        logic.setMediaName(directoryPath, mediaName);
        csvViewer csvViewer = new csvViewer(directoryPath, mediaName);

        fileName = csvViewer.getFileName();
        //JavaFX動画インスタンスとプレイヤーを取得
        Media media = mp.getMedia();
        MediaPlayer player = mp.getPlayer();

        JTable table = csvViewer.getTable();
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                String timeCode = (String) table.getValueAt(row, 0);
                player.seek(logic.getDataFromCsv(timeCode));
            }
        });

        codeWindow cWindow = new codeWindow(logic, csvViewer, "Code Window", player, 500, 800);
//        保存して終了
        cWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                csvViewer.saveCsvFile();
                System.exit(0);
            }
        });
        //        保存して終了
        csvViewer.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                csvViewer.saveCsvFile();
                System.exit(0);
            }
        });
        //        保存して終了
        videoWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                csvViewer.saveCsvFile();
                System.exit(0);
            }
        });

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
        // 指定した時間へとジャンプ
        Slider s = mp.getSlider();
        timeObject to = new timeObject();

        ChangeListener<? super Duration> playListener = (ov, old, current) ->
        {
            // スライダを移動
            s.setValue(player.getCurrentTime().toSeconds());
        };
        player.currentTimeProperty().addListener(playListener);
        // スライダを操作するとシークする
        EventHandler<MouseEvent> sliderHandler = (e) ->
        {
            // スライダを操作すると、シークする
            player.seek(Duration.seconds(s.getValue()));
        };
        s.addEventFilter(MouseEvent.MOUSE_RELEASED, sliderHandler);
        player.play();
        System.out.println("Current: " + player.getCurrentTime());
        System.out.println(player.getStopTime());
    }
}

class window extends JFrame {
    window(String title, int x, int y) {
        setTitle(title);
        setSize(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}


class button extends JButton {
    private DataObject dto;
    private int buttonState;
//    0:not pushed, 1:pushed, 2:not yet


    button(String title, int x, int y) {
        setText(title);
        setSize(x, y);
        buttonState = 2;
    }

    int getButtonState() {
        return this.buttonState;
    }

    void setButtonState(int buttonState) {
        this.buttonState = buttonState;
    }

    void setDto(DataObject dto) {
        this.dto = dto;
    }

    DataObject getDto() {
        return dto;
    }
}

//動画再生パネルクラス
class MoviePanel extends JFXPanel {
    private final Media media;
    private final MediaPlayer player;
    private int sliderTime;
    private final Slider slider;

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
        StackPane.setAlignment(slider, Pos.BOTTOM_CENTER);


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

class csvViewer extends JFrame {
    private final String fileName;
    private final String[] header = {"startTime", "endTime", "Action", "Detail"};
    private final DefaultTableModel tableModel = new DefaultTableModel(null, header);
    private final JTable table = new JTable(tableModel);
    private final JScrollPane jScrollPane = new JScrollPane(table);

    csvViewer(String directoryPath, String filename) throws IOException {
        super(filename + " - TimeLine");
        fileName = directoryPath + "/" + filename + ".csv";

        readIn();
        getContentPane().add(jScrollPane);
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        pack();
        show();
    }

    String getFileName() {
        return fileName;
    }

    void addRow(DataObject dto) {
        Object[] dataList = {dto.getStartTimeCode(), dto.getEndTimeCode(), dto.getActionName(), ""};
        tableModel.addRow(dataList);
    }

    void saveCsvFile() {
        File file = new File(fileName);
        if (!file.canWrite()) {
            // 書き込み可能に変更
            file.setWritable(true);
        }

        try (FileWriter fw = new FileWriter(file, false)) {
            // PrintWriterクラスのオブジェクトを生成
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < table.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < table.getColumnCount(); j++) {
                    sb.append(table.getValueAt(i, j)).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                bw.write(sb.toString());
                bw.newLine();
                bw.flush();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("あかんわ");
            throw new RuntimeException(e);
        }
        System.out.println("OK");
    }


    JTable getTable() {
        return table;
    }


    private void readIn() {
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
            table.setAutoCreateRowSorter(true);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tackleButton.addActionListener(a -> {
            int state = tackleButton.getButtonState();
            if (state == 0 || state == 2) {
                DataObject dto = new DataObject(logic.getTimeStamp(player.getCurrentTime()), "Tackle");
                tackleButton.setBorderPainted(false);
                tackleButton.setDto(dto);
                tackleButton.setForeground(Color.red);
                tackleButton.setButtonState(1);
            }
            if (state == 1) {
                tackleButton.setBorderPainted(true);
                DataObject dto = tackleButton.getDto();
                if (dto != null) {
                    dto.setEndTimeCode(logic.getTimeStamp(player.getCurrentTime()));
                    logic.csvWriter(directoryPath, dto);
                    tackleButton.setForeground(Color.black);
                    csvViewer.addRow(dto);
                }
                tackleButton.setButtonState(0);
            }
        });
        cwContainer.add(tackleButton);
        button scrumButton = new button("Scrum", 400, 200);
        scrumButton.addActionListener(a -> {
            int state = scrumButton.getButtonState();
            if (state == 0 || state == 2) {
                DataObject dto = new DataObject(logic.getTimeStamp(player.getCurrentTime()), "Scrum");
                scrumButton.setBorderPainted(false);
                scrumButton.setDto(dto);
                scrumButton.setForeground(Color.red);
                scrumButton.setButtonState(1);
            }
            if (state == 1) {
                scrumButton.setBorderPainted(true);
                DataObject dto = scrumButton.getDto();
                if (dto != null) {
                    dto.setEndTimeCode(logic.getTimeStamp(player.getCurrentTime()));
                    logic.csvWriter(directoryPath, dto);
                    scrumButton.setForeground(Color.black);
                    csvViewer.addRow(dto);
                }
                scrumButton.setButtonState(0);
            }
        });
        cwContainer.add(scrumButton);
        button lineOutButton = new button("Lineout", 400, 200);
        lineOutButton.addActionListener(a -> {
            int state = lineOutButton.getButtonState();
            if (state == 0 || state == 2) {
                DataObject dto = new DataObject(logic.getTimeStamp(player.getCurrentTime()), "Lineout");
                lineOutButton.setBorderPainted(false);
                lineOutButton.setDto(dto);
                lineOutButton.setForeground(Color.red);
                lineOutButton.setButtonState(1);
            }
            if (state == 1) {
                lineOutButton.setBorderPainted(true);
                DataObject dto = lineOutButton.getDto();
                if (dto != null) {
                    dto.setEndTimeCode(logic.getTimeStamp(player.getCurrentTime()));
                    logic.csvWriter(directoryPath, dto);
                    lineOutButton.setForeground(Color.black);
                    csvViewer.addRow(dto);
                }
                lineOutButton.setButtonState(0);
            }
        });
        cwContainer.add(lineOutButton);
        setLayout(new GridLayout(3, 1));
    }
}

