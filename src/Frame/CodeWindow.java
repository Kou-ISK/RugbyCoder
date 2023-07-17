package Frame;

import DataObject.TeamData;
import DataObject.TimelineDataObject;
import Logic.Logic;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static Frame.MakeFrames.directoryPath;
import static java.awt.Color.blue;
import static java.awt.Color.red;

class CodeWindow extends JFrame {
    private final String Ateam;
    private final String Bteam;
    private final ArrayList<String> buttonWithQualifierList = new ArrayList<>(Arrays.asList("キックオフ", "スクラム", "ラインアウト", "トライ"));
    private final ArrayList<String> buttonWithoutQualifierList = new ArrayList<>(Arrays.asList("タックル", "22m侵入", "WTBボールタッチ", "キック", "PK"));

    CodeWindow(TeamData td, Logic logic, CsvViewer csvViewer, String title, MediaPlayer player, int x, int y) {
        setTitle(title);
        setSize(x, y);
        Container cwContainer = this.getContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Ateam = td.getAteam();
        Bteam = td.getBteam();

        /**
         * ポゼッション
         **/
        MyButton APosMyButton = new MyButton(Ateam, 400, 200);
        MyButton BPosButton = new MyButton(Bteam, 400, 200);
        APosMyButton.addActionListener(a -> {
            if (BPosButton.getButtonState() == 1) {
                BPosButton.doClick();
            }
            int state = APosMyButton.getButtonState();
            if (state == 0 || state == 2) {
                TimelineDataObject dto = new TimelineDataObject(logic.getTimeStamp(player.getCurrentTime()), Ateam);
                APosMyButton.setBorderPainted(false);
                APosMyButton.setDto(dto);
                APosMyButton.setForeground(red);
                APosMyButton.setButtonState(1);
            }
            if (state == 1) {
                APosMyButton.setBorderPainted(true);
                TimelineDataObject dto = APosMyButton.getDto();
                if (dto != null) {
                    dto.setEndTimeCode(logic.getTimeStamp(player.getCurrentTime()));
                    logic.csvWriter(directoryPath, dto);
                    APosMyButton.setForeground(Color.black);
                    csvViewer.addRow(dto);
                }
                APosMyButton.setButtonState(0);
            }
        });
        APosMyButton.setBackground(blue);
        APosMyButton.setOpaque(true);
        cwContainer.add(APosMyButton);
        BPosButton.addActionListener(a -> {
            if (APosMyButton.getButtonState() == 1) {
                APosMyButton.doClick();
            }
            int state = BPosButton.getButtonState();
            if (state == 0 || state == 2) {
                TimelineDataObject dto = new TimelineDataObject(logic.getTimeStamp(player.getCurrentTime()), Bteam);
                BPosButton.setBorderPainted(false);
                BPosButton.setDto(dto);
                BPosButton.setForeground(Color.red);
                BPosButton.setButtonState(1);
            }
            if (state == 1) {
                BPosButton.setBorderPainted(true);
                TimelineDataObject dto = BPosButton.getDto();
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
            CodeWindowButton aButton = new CodeWindowButton(Ateam + buttonText, logic, player, csvViewer, 400, 200, true);
            aButton.setBackground(blue);
            cwContainer.add(aButton);
            CodeWindowButton bButton = new CodeWindowButton(Bteam + buttonText, logic, player, csvViewer, 400, 200, true);
            cwContainer.add(bButton);
        });

        /**
         * 各種アクションボタン(ラベルなし)を追加
         */
        buttonWithoutQualifierList.forEach(buttonText -> {
            CodeWindowButton aButton = new CodeWindowButton(Ateam + buttonText, logic, player, csvViewer, 400, 200, false);
            aButton.setBackground(blue);
            cwContainer.add(aButton);
            CodeWindowButton bButton = new CodeWindowButton(Bteam + buttonText, logic, player, csvViewer, 400, 200, false);
            cwContainer.add(bButton);
        });

        setLayout(new GridLayout(10, 2));
    }
}
