package Frame;

import DataObject.teamDatas;
import Logic.Logic;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MakeFrames {

    static String directoryPath;
    static KeyListener kl;

    void makeFrames(String directoryPath, String filePath, String filePath2, String mediaName, teamDatas td) throws Exception {
        MakeFrames.directoryPath = directoryPath;
        Logic logic = new Logic();
        String fileName;
//      Creating Media Player Window
        window videoWindow = new window("Rugby Coder");
        //Input Video File Path
        MoviePanel mp = new MoviePanel(filePath, filePath2);
        logic.setMediaName(directoryPath, mediaName);
        csvViewer csvViewer = new csvViewer(directoryPath, mediaName);

        fileName = csvViewer.getFileName();
        //JavaFX動画インスタンスとプレイヤーを取得

        MediaPlayer player = mp.getPlayer();
        MediaPlayer player2 = mp.getPlayer();

        JTable table = csvViewer.getTable();
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                String timeCode = (String) table.getValueAt(row, 0);
                player.seek(logic.getDataFromCsv(timeCode));
                player2.seek(logic.getDataFromCsv(timeCode));
            }
        });

        codeWindow cWindow = new codeWindow(td, logic, csvViewer, "Code Window", player, 200, 300);
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

        videoWindow.add(mp);

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
                        player2.setRate(0.5);
                        break;
                    case KeyEvent.VK_SPACE:
                        //スペースキー
                        if (player.getStatus() == MediaPlayer.Status.PLAYING) {
                            player.pause();
                            player2.pause();
                        } else {
                            player.setRate(1.0);
                            player2.setRate(1.0);
                            player.play();
                            player2.play();
                        }
                        break;
                    case KeyEvent.VK_SHIFT:
                        player.setRate(2.0);
                        player2.setRate(2.0);
                        break;
                    case KeyEvent.VK_ENTER:
                        player.setRate(6.0);
                        player2.setRate(6.0);
                        break;
                    case KeyEvent.VK_LEFT:
                        Double d = player.getCurrentTime().toSeconds() - 5;
                        player.seek(Duration.seconds(d));
                        player2.seek(Duration.seconds(d));
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
                        player2.setRate(1.0);
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
        player2.play();

        System.out.println("Current: " + player.getCurrentTime());
        System.out.println(player.getStopTime());
    }
}


