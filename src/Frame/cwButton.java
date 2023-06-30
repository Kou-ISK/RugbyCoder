package Frame;

import DataObject.DataObject;
import Logic.Logic;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;

import static Frame.MakeFrames.directoryPath;
import static Frame.MakeFrames.kl;

class cwButton extends JButton {
    private DataObject dto;
    private int buttonState;
//    0:not pushed, 1:pushed, 2:not yet

    cwButton(String title, Logic logic, MediaPlayer player, csvViewer csvViewer, int x, int y, boolean needQualifier) {
        setText(title);
        setSize(x, y);
        setFocusable(false);
        setOpaque(true);
        setRequestFocusEnabled(false);
        addKeyListener(kl);
        buttonState = 2;
        addActionListener(a -> {
            int state = this.getButtonState();
            if (state == 0 || state == 2) {
                DataObject dto = new DataObject(logic.getTimeStamp(player.getCurrentTime()), title);
                this.setBorderPainted(false);
                this.setDto(dto);
                this.setForeground(Color.red);
                this.setButtonState(1);
                if (needQualifier == true) {
                    qualifierWindow qw = new qualifierWindow(dto);
                    qw.setVisible(true);
                }
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
