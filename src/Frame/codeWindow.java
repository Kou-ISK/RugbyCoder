package Frame;

import DataObject.DataObject;
import DataObject.teamDatas;
import Logic.Logic;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static Frame.MakeFrames.directoryPath;
import static java.awt.Color.blue;
import static java.awt.Color.red;

class codeWindow extends JFrame {
    private final String Ateam;
    private final String Bteam;
    private final ArrayList<String> buttonWithQualifierList = new ArrayList<>(Arrays.asList("キックオフ", "スクラム", "ラインアウト", "トライ"));
    private final ArrayList<String> buttonWithoutQualifierList = new ArrayList<>(Arrays.asList("タックル", "22m侵入", "WTBボールタッチ", "キック", "PK"));

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
                APosButton.setForeground(red);
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
        APosButton.setBackground(blue);
        APosButton.setOpaque(true);
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
         * 各種アクションボタン(ラベルあり)を追加
         */
        buttonWithQualifierList.forEach(buttonText -> {
            cwButton aButton = new cwButton(Ateam + buttonText, logic, player, csvViewer, 400, 200, true);
            aButton.setBackground(blue);
            cwContainer.add(aButton);
            cwButton bButton = new cwButton(Bteam + buttonText, logic, player, csvViewer, 400, 200, true);
            cwContainer.add(bButton);
        });

        /**
         * 各種アクションボタン(ラベルなし)を追加
         */
        buttonWithoutQualifierList.forEach(buttonText -> {
            cwButton aButton = new cwButton(Ateam + buttonText, logic, player, csvViewer, 400, 200, false);
            aButton.setBackground(blue);
            cwContainer.add(aButton);
            cwButton bButton = new cwButton(Bteam + buttonText, logic, player, csvViewer, 400, 200, false);
            cwContainer.add(bButton);
        });

        setLayout(new GridLayout(10, 2));
    }
}
