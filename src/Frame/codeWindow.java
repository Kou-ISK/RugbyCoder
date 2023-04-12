package Frame;

import DataObject.DataObject;
import DataObject.teamDatas;
import Logic.Logic;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;

import static Frame.MakeFrames.directoryPath;

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

        /**
         * タックル
         */
        cwButton ATackleButton = new cwButton(Ateam + "タックル", logic, player, csvViewer, 400, 200, false);
        cwContainer.add(ATackleButton);
        setLayout(new GridLayout(8, 2));
    }
}
