package Frame;

import DataObject.TeamData;
import Logic.Logic;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MakeFrames {
    static String filePath;
    static String directoryPath;
    static KeyListener kl;
    private String mediaName;

    void makeFrames(String directoryPath, String filePath, String mediaName, TeamData td) throws Exception {
        this.filePath = filePath;
        this.mediaName = mediaName;
        MakeFrames.directoryPath = directoryPath;
        Logic logic = new Logic();
        String fileName;
//      Creating Media Player Window
        Window videoWindow = new Window("Rugby Coder");
        //Input Video File Path
        MoviePanel mp = new MoviePanel(filePath);
        logic.setMediaName(directoryPath, mediaName);
        CsvViewer csvViewer = new CsvViewer(directoryPath, mediaName);

        fileName = csvViewer.getFileName();
        //JavaFX動画インスタンスとプレイヤーを取得
        Media media = mp.getMedia();
        MediaPlayer player = mp.getPlayer();

        JTable table = csvViewer.getTable();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int rowNumber = table.rowAtPoint(evt.getPoint());
                String timeCode = (String) table.getValueAt(rowNumber, 0);
                player.seek(logic.getDataFromCsv(timeCode));
            }
        });

        table.addKeyListener(new KeyListener() {
                                 @Override
                                 public void keyTyped(KeyEvent e) {

                                 }

                                 @Override
                                 public void keyPressed(KeyEvent e) {
                                     switch (e.getKeyCode()) {
                                         case KeyEvent.VK_BACK_SPACE -> {
                                             int removeRowNumber = table.getSelectedRow();
                                             if (removeRowNumber != -1) {
                                                 csvViewer.removeRow(removeRowNumber);
                                             }
                                         }
                                     }
                                 }

                                 @Override
                                 public void keyReleased(KeyEvent e) {

                                 }
                             }
        );

        CodeWindow cWindow = new CodeWindow(td, logic, csvViewer, "Code Window", player, 200, 300);
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
        // Windowのサイズ変更イベントを処理するリスナーを追加
        videoWindow.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // JFrameのサイズが変更された際にMoviePanelのサイズを更新する
                Dimension size = videoWindow.getSize();
                mp.getMediaView().resize(size.height, size.width);
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
        int videoW = (int) (media.getWidth() * 0.9);
        int videoH = (int) ((media.getHeight() + 50) * 0.9);

        //MoviePanelのサイズを動画に合わせてJFrameに追加
        mp.setPreferredSize(new Dimension(videoW, videoH));
        videoWindow.add(mp);

        //JFrame側のパネルサイズを動画に合わせる
//        videoWindow.getContentPane().setPreferredSize(new Dimension(videoW, videoH));
        mp.getRootPane().setSize(videoWindow.getSize());

        //JFrameサイズをパネル全体が見えるサイズに自動調整
        videoWindow.pack();
        kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                        //右キー
                        player.setRate(0.5);
                        break;
                    case KeyEvent.VK_SPACE:
                        //スペースキー
                        if (player.getStatus() == MediaPlayer.Status.PLAYING) {
                            player.pause();
                        } else {
                            player.setRate(1.0);
                            player.play();
                        }
                        break;
                    case KeyEvent.VK_SHIFT:
                        player.setRate(2.0);
                        break;
                    case KeyEvent.VK_ENTER:
                        player.setRate(6.0);
                        break;
                    case KeyEvent.VK_LEFT:
                        Double d = player.getCurrentTime().toSeconds() - 5;
                        player.seek(Duration.seconds(d));
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                        //右キー
                    case KeyEvent.VK_SPACE:
                    case KeyEvent.VK_ENTER:
                    case KeyEvent.VK_SHIFT:
                        player.setRate(1.0);
                        break;
                }
            }
        };
        cWindow.addKeyListener(kl);
        cWindow.setFocusable(true);
        cWindow.setAutoRequestFocus(true);
        cWindow.setFocusableWindowState(true);
        cWindow.requestFocus();

        //中心に表示
        videoWindow.setLocationRelativeTo(null);
        videoWindow.setLayout(new FlowLayout());
        videoWindow.setVisible(true);
        cWindow.setVisible(true);
        videoWindow.setLocation(0, 0);
        cWindow.setLocation(900, 0);

        player.play();
        System.out.println("Current: " + player.getCurrentTime());
    }
}


