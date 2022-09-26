package Frame;

import DataObject.DataObject;
import DataObject.teamDatas;
import Logic.Logic;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

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

    void makeFrames(String directoryPath, String filePath, String mediaName, teamDatas td) throws Exception {
        this.filePath = filePath;
        this.mediaName = mediaName;
        MakeFrames.directoryPath = directoryPath;
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

        codeWindow cWindow = new codeWindow(td, logic, csvViewer, "Code Window", player, 500, 800);
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
        int videoW = media.getWidth();
        int videoH = media.getHeight();

        //MoviePanelのサイズを動画に合わせてJFrameに追加
        mp.setPreferredSize(new Dimension(videoW, videoH));
        videoWindow.add(mp);


        //JFrame側のパネルサイズを動画に合わせる
        videoWindow.getContentPane().setPreferredSize(new Dimension(videoW, videoH + 100));

        //JFrameサイズをパネル全体が見えるサイズに自動調整
        videoWindow.pack();

        //中心に表示
        videoWindow.setLocationRelativeTo(null);

        button fastForward = new button("Fast Forward", 1000, 400);
        fastForward.addActionListener(a -> {
            player.setRate(8.0);
        });
        videoWindow.add(fastForward);

        videoWindow.setLayout(new FlowLayout());
        videoWindow.setVisible(true);
        cWindow.setVisible(true);
        videoWindow.setLocation(0, 0);
        cWindow.setLocation(900, 0);


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

class cwButton extends JButton {
    private DataObject dto;
    private int buttonState;
//    0:not pushed, 1:pushed, 2:not yet

    cwButton(String title, Logic logic, MediaPlayer player, csvViewer csvViewer, int x, int y) {
        setText(title);
        setSize(x, y);
        buttonState = 2;
        addActionListener(a -> {
            int state = this.getButtonState();
            if (state == 0 || state == 2) {
                DataObject dto = new DataObject(logic.getTimeStamp(player.getCurrentTime()), title);
                this.setBorderPainted(false);
                this.setDto(dto);
                this.setForeground(Color.red);
                this.setButtonState(1);
                qualifierWindow qw = new qualifierWindow(dto);
                qw.setVisible(true);
            }
            if (state == 1) {
                this.setBorderPainted(true);
                DataObject dto = this.getDto();
                if (dto != null) {
                    dto.setEndTimeCode(logic.getTimeStamp(player.getCurrentTime()));
                    logic.csvWriter(directoryPath, dto);
                    this.setForeground(Color.black);
                    csvViewer.addRow(dto);
                }
                this.setButtonState(0);
            }
        });
    }

    cwButton(String title, Logic logic, MediaPlayer player, csvViewer csvViewer, int x, int y, boolean bool) {
        if (!bool) {
            setText(title);
            setSize(x, y);
            buttonState = 2;
            addActionListener(a -> {
                int state = this.getButtonState();
                if (state == 0 || state == 2) {
                    DataObject dto = new DataObject(logic.getTimeStamp(player.getCurrentTime()), title);
                    this.setBorderPainted(false);
                    this.setDto(dto);
                    this.setForeground(Color.red);
                    this.setButtonState(1);
                }
                if (state == 1) {
                    this.setBorderPainted(true);
                    DataObject dto = this.getDto();
                    if (dto != null) {
                        dto.setEndTimeCode(logic.getTimeStamp(player.getCurrentTime()));
                        logic.csvWriter(directoryPath, dto);
                        this.setForeground(Color.black);
                        csvViewer.addRow(dto);
                    }
                    this.setButtonState(0);
                }
            });
        }
    }

    private int getButtonState() {
        return this.buttonState;
    }

    private void setButtonState(int buttonState) {
        this.buttonState = buttonState;
    }

    private void setDto(DataObject dto) {
        this.dto = dto;
    }

    private DataObject getDto() {
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
        BorderPane root = new BorderPane();
        Pane mpane = new Pane();
        // 動画ファイルのパスを取得
        File f = new File(filePath);

        // 動画再生クラスをインスタンス化
        media = new Media(f.toURI().toString());
        player = new MediaPlayer(media);
        MediaView mediaView = new MediaView(player);
        mediaController mc = new mediaController(player);
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

class csvViewer extends JFrame {
    private final String fileName;
    private final String[] header = {"startTime", "endTime", "Action", "Qualifier"};
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
        Object[] dataList = {dto.getStartTimeCode(), dto.getEndTimeCode(), dto.getActionName(), dto.getActionQualifier()};
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
    private String Ateam;
    private String Bteam;

    codeWindow(teamDatas td, Logic logic, csvViewer csvViewer, String title, MediaPlayer player, int x, int y) {
        setTitle(title);
        setSize(x, y);
        Container cwContainer = this.getContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Ateam = td.getAteam();
        Bteam = td.getBteam();

//        cwButton tackleButton = new cwButton("Tackle", logic, player, csvViewer, 1000, 200);
//        cwContainer.add(tackleButton);
        /**
         * ポゼッション
         **/
        button APosButton = new button(Ateam, 400, 200);
        button BPosButton = new button(Bteam, 400, 200);
        APosButton.addActionListener(a -> {
            if (BPosButton.getButtonState() == 1) {
                BPosButton.doClick();
            }
            int state = APosButton.getButtonState();
            if (state == 0 || state == 2) {
                DataObject dto = new DataObject(logic.getTimeStamp(player.getCurrentTime()), Ateam);
                APosButton.setBorderPainted(false);
                APosButton.setDto(dto);
                APosButton.setForeground(Color.red);
                APosButton.setButtonState(1);
            }
            if (state == 1) {
                APosButton.setBorderPainted(true);
                DataObject dto = APosButton.getDto();
                if (dto != null) {
                    dto.setEndTimeCode(logic.getTimeStamp(player.getCurrentTime()));
                    logic.csvWriter(directoryPath, dto);
                    APosButton.setForeground(Color.black);
                    csvViewer.addRow(dto);
                }
                APosButton.setButtonState(0);
            }
        });
        cwContainer.add(APosButton);
        BPosButton.addActionListener(a -> {
            if (APosButton.getButtonState() == 1) {
                APosButton.doClick();
            }
            int state = BPosButton.getButtonState();
            if (state == 0 || state == 2) {
                DataObject dto = new DataObject(logic.getTimeStamp(player.getCurrentTime()), Bteam);
                BPosButton.setBorderPainted(false);
                BPosButton.setDto(dto);
                BPosButton.setForeground(Color.red);
                BPosButton.setButtonState(1);
            }
            if (state == 1) {
                BPosButton.setBorderPainted(true);
                DataObject dto = BPosButton.getDto();
                if (dto != null) {
                    dto.setEndTimeCode(logic.getTimeStamp(player.getCurrentTime()));
                    logic.csvWriter(directoryPath, dto);
                    BPosButton.setForeground(Color.black);
                    csvViewer.addRow(dto);
                }
                BPosButton.setButtonState(0);
            }
        });
        cwContainer.add(BPosButton);
        /**
         * キックオフ
         **/
        cwButton AKickOffButton = new cwButton(Ateam + "キックオフ", logic, player, csvViewer, 400, 200, false);
        cwContainer.add(AKickOffButton);
        cwButton BKickOffButton = new cwButton(Bteam + "キックオフ", logic, player, csvViewer, 400, 200, false);
        cwContainer.add(BKickOffButton);
        /**
         * スクラム
         **/
        cwButton AScrumButton = new cwButton(Ateam + "スクラム", logic, player, csvViewer, 400, 200);
        cwContainer.add(AScrumButton);
        cwButton BScrumButton = new cwButton(Bteam + "スクラム", logic, player, csvViewer, 400, 200);
        cwContainer.add(BScrumButton);
        /**
         * ラインアウト
         **/
        cwButton ALineOutButton = new cwButton(Ateam + "ラインアウト", logic, player, csvViewer, 400, 200);
        cwContainer.add(ALineOutButton);
        cwButton BLineOutButton = new cwButton(Bteam + "ラインアウト", logic, player, csvViewer, 400, 200);
        cwContainer.add(BLineOutButton);
        /**
         * キック
         **/
        cwButton AKickButton = new cwButton(Ateam + "キック", logic, player, csvViewer, 400, 200, false);
        cwContainer.add(AKickButton);
        cwButton BKickButton = new cwButton(Bteam + "キック", logic, player, csvViewer, 400, 200, false);
        cwContainer.add(BKickButton);
        /**
         * ペナルティ
         **/
        cwButton APenButton = new cwButton(Ateam + "PK", logic, player, csvViewer, 400, 200, false);
        cwContainer.add(APenButton);
        cwButton BPenButton = new cwButton(Bteam + "PK", logic, player, csvViewer, 400, 200, false);
        cwContainer.add(BPenButton);
        /**
         * トライ
         **/
        cwButton ATryButton = new cwButton(Ateam + "トライ", logic, player, csvViewer, 400, 200, false);
        cwContainer.add(ATryButton);
        cwButton BTryButton = new cwButton(Bteam + "トライ", logic, player, csvViewer, 400, 200, false);
        cwContainer.add(BTryButton);
        setLayout(new GridLayout(8, 2));
    }
}

class QButton extends JButton {
    private final Container parent;

    QButton(String title, DataObject dto) {
        setText(title);
        this.parent = getParent();
        addActionListener(a -> {
            dto.setActionQualifier(title);
            Component c = (Component) a.getSource();
            Window w = SwingUtilities.getWindowAncestor(c);
            w.dispose();
        });
    }
}

class qualifierWindow extends JFrame {
    private final DataObject dto;
    private boolean close;

    qualifierWindow(DataObject dto) {
        setTitle("Qualifier");
        this.dto = dto;
        setSize(350, 100);
        setLocation(900, 100);
        setLayout(new GridLayout(1, 5));
        add(new QButton("Won", dto));
        add(new QButton("Stolen", dto));
        add(new QButton("Success", dto));
        add(new QButton("Miss", dto));
        add(new QButton("Again", dto));
        toFront();
    }
}

